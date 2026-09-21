# encoding: UTF-8
require "bcrypt"
require "json"
require "jwt"
require "securerandom"
require "socket"
require "sqlite3"

HOST = "127.0.0.1"
PORT = 4004
DB_PATH = File.join(__dir__, "users.db")
JWT_SECRET = "learning-secret"

db = SQLite3::Database.new(DB_PATH)
db.results_as_hash = true
db.execute <<~SQL
  CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL UNIQUE,
    password_digest TEXT NOT NULL
  )
SQL

sessions = {}

def parse_request(client)
  request_line = client.gets
  return unless request_line

  method, path, = request_line.split(" ")
  headers = {}

  while (line = client.gets)
    break if line == "\r\n"
    name, value = line.split(":", 2)
    headers[name.downcase] = value.strip if name && value
  end

  length = headers.fetch("content-length", "0").to_i
  body = length.positive? ? client.read(length) : ""
  [method, path, headers, body]
end

def json_response(status, body, extra_headers = [])
  json = JSON.generate(body)
  [
    "HTTP/1.1 #{status}",
    "Content-Type: application/json; charset=utf-8",
    "Content-Length: #{json.bytesize}",
    *extra_headers,
    "Connection: close",
    "",
    json
  ].join("\r\n")
end

def authenticate_password(db, email, password)
  user = db.get_first_row("SELECT id, email, password_digest FROM users WHERE email = ?", email)
  return unless user
  return unless BCrypt::Password.new(user["password_digest"]) == password

  user
end

def bearer_user(db, headers)
  auth = headers["authorization"].to_s
  return unless auth.start_with?("Bearer ")

  token = auth.delete_prefix("Bearer ")
  payload, = JWT.decode(token, JWT_SECRET, true, algorithm: "HS256")
  db.get_first_row("SELECT id, email FROM users WHERE id = ?", payload["sub"])
rescue JWT::DecodeError
  nil
end

def session_user(db, headers, sessions)
  cookie = headers["cookie"].to_s
  session_id = cookie.split(";").map(&:strip).find { |part| part.start_with?("session_id=") }&.split("=", 2)&.last
  return unless session_id

  user_id = sessions[session_id]
  return unless user_id

  db.get_first_row("SELECT id, email FROM users WHERE id = ?", user_id)
end

server = TCPServer.new(HOST, PORT)
puts "Login API listening on http://#{HOST}:#{PORT}"

loop do
  client = server.accept
  request = parse_request(client)
  next client.close unless request

  method, path, headers, request_body = request
  status = "200 OK"
  body = nil
  extra_headers = []

  case [method, path]
  when ["POST", "/users"]
    begin
      params = JSON.parse(request_body)
      email = params["email"].to_s.strip
      password = params["password"].to_s

      if email.empty? || password.empty?
        status = "400 Bad Request"
        body = { error: "email and password are required" }
      else
        digest = BCrypt::Password.create(password)
        db.execute("INSERT INTO users (email, password_digest) VALUES (?, ?)", email, digest)
        body = { id: db.last_insert_row_id, email: email }
        status = "201 Created"
      end
    rescue JSON::ParserError
      status = "400 Bad Request"
      body = { error: "invalid JSON" }
    rescue SQLite3::ConstraintException
      status = "409 Conflict"
      body = { error: "email already exists" }
    end
  when ["POST", "/login/jwt"]
    begin
      params = JSON.parse(request_body)
      user = authenticate_password(db, params["email"].to_s, params["password"].to_s)
      if user
        token = JWT.encode({ sub: user["id"], exp: Time.now.to_i + 3600 }, JWT_SECRET, "HS256")
        body = { token: token }
      else
        status = "401 Unauthorized"
        body = { error: "invalid credentials" }
      end
    rescue JSON::ParserError
      status = "400 Bad Request"
      body = { error: "invalid JSON" }
    end
  when ["POST", "/login/session"]
    begin
      params = JSON.parse(request_body)
      user = authenticate_password(db, params["email"].to_s, params["password"].to_s)
      if user
        session_id = SecureRandom.hex(24)
        sessions[session_id] = user["id"]
        extra_headers << "Set-Cookie: session_id=#{session_id}; HttpOnly; SameSite=Lax"
        body = { logged_in: true }
      else
        status = "401 Unauthorized"
        body = { error: "invalid credentials" }
      end
    rescue JSON::ParserError
      status = "400 Bad Request"
      body = { error: "invalid JSON" }
    end
  when ["GET", "/me"]
    user = bearer_user(db, headers) || session_user(db, headers, sessions)
    if user
      body = { id: user["id"], email: user["email"] }
    else
      status = "401 Unauthorized"
      body = { error: "authentication required" }
    end
  else
    status = "404 Not Found"
    body = { error: "not found" }
  end

  client.write(json_response(status, body, extra_headers))
  client.close
end
