# encoding: UTF-8
require "json"
require "socket"

HOST = "127.0.0.1"
PORT = 4002

todos = []
next_id = 1
server = TCPServer.new(HOST, PORT)

puts "REST API listening on http://#{HOST}:#{PORT}"

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
    response_body = todos
  when ["POST", "/todos"]
    begin
      params = JSON.parse(request_body)
      title = params["title"].to_s.strip

      if title.empty?
        status = "400 Bad Request"
        response_body = { error: "title is required" }
      else
        todo = { id: next_id, title: title, done: false }
        next_id += 1
        todos << todo
        status = "201 Created"
        response_body = todo
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
