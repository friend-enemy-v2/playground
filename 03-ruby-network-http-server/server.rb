# encoding: UTF-8
require "socket"

HOST = "127.0.0.1"
PORT = 4001

server = TCPServer.new(HOST, PORT)
puts "Open http://#{HOST}:#{PORT}"

loop do
  client = server.accept

  request_line = client.gets
  next client.close unless request_line

  method, path, = request_line.split(" ")

  while (line = client.gets)
    break if line == "\r\n"
  end

  body = "Hello from Ruby HTTP server!\nmethod=#{method}\npath=#{path}\n"

  response = [
    "HTTP/1.1 200 OK",
    "Content-Type: text/plain; charset=utf-8",
    "Content-Length: #{body.bytesize}",
    "Connection: close",
    "",
    body
  ].join("\r\n")

  client.write(response)
  client.close
end
