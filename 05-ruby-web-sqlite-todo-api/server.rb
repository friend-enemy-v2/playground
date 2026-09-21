# encoding: UTF-8
require "json"
require "socket"
require "sqlite3"

HOST = "127.0.0.1"
PORT = 4003
DB_PATH = File.join(__dir__, "todos.db")

db = SQLite3::Database.new(DB_PATH)
db.results_as_hash = true
db.execute <<~SQL
  CREATE TABLE IF NOT EXISTS todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    done INTEGER NOT NULL DEFAULT 0
  )
SQL

server = TCPServer.new(HOST, PORT)
puts "SQLite Todo API listening on http://#{HOST}:#{PORT}"

loop do
  client = server.accept

  request_line = client.gets
  next client.close unless request_line

  method, path, = request_line.split(" ")
  headers = {}

  while (line = client.gets)
    break if line == "\r\n"

    name, value = line.split(":", 2)
    headers[name.downcase] = value.strip if name && value
  end

  length = headers.fetch("content-length", "0").to_i
  request_body = length.positive? ? client.read(length) : ""

  status = "200 OK"
  response_body = nil

  case [method, path]
  when ["GET", "/todos"]
    rows = db.execute("SELECT id, title, done FROM todos ORDER BY id")
    response_body = rows.map do |row|
      { id: row["id"], title: row["title"], done: row["done"] == 1 }
    end
  when ["POST", "/todos"]
    begin
      params = JSON.parse(request_body)
      title = params["title"].to_s.strip

      if title.empty?
        status = "400 Bad Request"
        response_body = { error: "title is required" }
      else
        db.execute("INSERT INTO todos (title, done) VALUES (?, 0)", title)
        id = db.last_insert_row_id
        response_body = { id: id, title: title, done: false }
        status = "201 Created"
      end
    rescue JSON::ParserError
      status = "400 Bad Request"
      response_body = { error: "invalid JSON" }
    end
  else
    status = "404 Not Found"
    response_body = { error: "not found" }
  end

  json = JSON.generate(response_body)
  response = [
    "HTTP/1.1 #{status}",
    "Content-Type: application/json; charset=utf-8",
    "Content-Length: #{json.bytesize}",
    "Connection: close",
    "",
    json
  ].join("\r\n")

  client.write(response)
  client.close
end
