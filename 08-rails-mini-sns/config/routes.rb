Rails.application.routes.draw do
  post "/users", to: "users#create"
  post "/login", to: "sessions#create"
  get "/posts", to: "posts#index"
  post "/posts", to: "posts#create"
end
