class UsersController < ApplicationController
  def create
    user = User.new(email: params[:email], password: params[:password])

    if user.save
      render json: { id: user.id, email: user.email }, status: :created
    else
      render json: { errors: user.errors.full_messages }, status: :unprocessable_entity
    end
  end
end
