# encoding: UTF-8
require "json"
require "net/http"
require "uri"

BASE = URI("http://127.0.0.1:4004")

login = Net::HTTP.post(
  BASE + "/login/jwt",
  JSON.generate(email: "user@example.com", password: "password"),
  "Content-Type" => "application/json"
)
token = JSON.parse(login.body)["token"]
abort login.body unless token

request = Net::HTTP::Get.new("/me")
request["Authorization"] = "Bearer #{token}"
response = Net::HTTP.start(BASE.host, BASE.port) { |http| http.request(request) }
puts response.body
