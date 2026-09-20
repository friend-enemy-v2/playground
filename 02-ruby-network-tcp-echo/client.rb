# encoding: UTF-8
require "socket"

HOST = "127.0.0.1"
PORT = 4000

socket = TCPSocket.new(HOST, PORT)

puts "Connected to #{HOST}:#{PORT}. Type a message or Ctrl-D to exit."

while (message = STDIN.gets)
  socket.write(message)
  puts "echo: #{socket.gets}"
end

socket.close
