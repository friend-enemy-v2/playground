# encoding: UTF-8
require "bcrypt"
require "json"
require "jwt"
require "socket"
require "sqlite3"
require "time"

HOST = "127.0.0.1"
PORT = 4005
DB_PATH = File.join(__dir__, "sns.db")
JWT_SECRET = "learning-secret"

db = SQLite3::Database.new(DB_PATH)
db.results_as_hash = true
db.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL UNIQUE, password_digest TEXT NOT NULL)")
db.execute("CREATE TABLE IF NOT EXISTS posts (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, body TEXT NOT NULL, created_at TEXT NOT NULL)")

def read_request(client)
  line = client.gets
  return unless line
  method, path, = line.split(" ")
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

def http_response(status, body)
  json = JSON.generate(body)
  ["HTTP/1.1 #{status}", "Content-Type: application/json; charset=utf-8",
   "Content-Length: #{json.bytesize}", "Connection: close", "", json].join("\r\n")
end

def current_user(db, headers)
  auth = headers["authorization"].to_s
  return unless auth.start_with?("Bearer ")
  payload, = JWT.decode(auth.delete_prefix("Bearer "), JWT_SECRET, true, algorithm: "HS256")
  db.get_first_row("SELECT id, email FROM users WHERE id = ?", payload["sub"])
rescue JWT::DecodeError
  nil
end

server = TCPServer.new(HOST, PORT)
puts "Mini SNS listening on http://#{HOST}:#{PORT}"

loop do
  client = server.accept
  request = read_request(client)
  next client.close unless request
  method, path, headers, request_body = request
  status = "200 OK"
  body = {}

  begin
    case [method, path]
    when ["POST", "/users"]
      params = JSON.parse(request_body)
      email = params["email"].to_s.strip
      password = params["password"].to_s
      if email.empty? || password.empty?
        status, body = "400 Bad Request", { error: "email and password are required" }
      else
        digest = BCrypt::Password.create(password)
        db.execute("INSERT INTO users (email, password_digest) VALUES (?, ?)", email, digest)
        status, body = "201 Created", { id: db.last_insert_row_id, email: email }
      end
    when ["POST", "/login"]
      params = JSON.parse(request_body)
      user = db.get_first_row("SELECT * FROM users WHERE email = ?", params["email"].to_s)
      if user && BCrypt::Password.new(user["password_digest"]) == params["password"].to_s
        token = JWT.encode({ sub: user["id"], exp: Time.now.to_i + 3600 }, JWT_SECRET, "HS256")
        body = { token: token }
      else
        status, body = "401 Unauthorized", { error: "invalid credentials" }
      end
    when ["GET", "/posts"]
      rows = db.execute("SELECT posts.id, posts.body, posts.created_at, users.id AS user_id, users.email FROM posts JOIN users ON users.id = posts.user_id ORDER BY posts.id DESC")
      body = rows.map { |r| { id: r["id"], body: r["body"], created_at: r["created_at"], user: { id: r["user_id"], email: r["email"] } } }
    when ["POST", "/posts"]
      user = current_user(db, headers)
      if user.nil?
        status, body = "401 Unauthorized", { error: "authentication required" }
      else
        params = JSON.parse(request_body)
        text = params["body"].to_s.strip
        if text.empty?
          status, body = "400 Bad Request", { error: "body is required" }
        else
          created_at = Time.now.utc.iso8601
          db.execute("INSERT INTO posts (user_id, body, created_at) VALUES (?, ?, ?)", user["id"], text, created_at)
          status, body = "201 Created", { id: db.last_insert_row_id, body: text, created_at: created_at, user: { id: user["id"], email: user["email"] } }
        end
      end
    else
      status, body = "404 Not Found", { error: "not found" }
    end
  rescue JSON::ParserError
    status, body = "400 Bad Request", { error: "invalid JSON" }
  rescue SQLite3::ConstraintException
    status, body = "409 Conflict", { error: "email already exists" }
  end

  client.write(http_response(status, body))
  client.close
end
