# encoding: UTF-8
require "socket"

HOST = "127.0.0.1"
PORT = 4000

server = TCPServer.new(HOST, PORT)
puts "Listening on #{HOST}:#{PORT}"

loop do
  client = server.accept
  puts "Connected: #{client.peeraddr[3]}:#{client.peeraddr[1]}"

  while (message = client.gets)
    client.write(message)
  end

  client.close
  puts "Disconnected"
end
