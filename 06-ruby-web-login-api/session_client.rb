# encoding: UTF-8
require "json"
require "net/http"
require "uri"

BASE = URI("http://127.0.0.1:4004")

login = Net::HTTP.post(
  BASE + "/login/session",
  JSON.generate(email: "user@example.com", password: "password"),
  "Content-Type" => "application/json"
)
cookie = login["Set-Cookie"]
abort login.body unless cookie

request = Net::HTTP::Get.new("/me")
request["Cookie"] = cookie
response = Net::HTTP.start(BASE.host, BASE.port) { |http| http.request(request) }
puts response.body
