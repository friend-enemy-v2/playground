class SessionsController < ApplicationController
  def create
    user = User.find_by(email: params[:email])

    if user&.authenticate(params[:password])
      token = JWT.encode(
        { sub: user.id, exp: 1.hour.from_now.to_i },
        ApplicationController::JWT_SECRET,
        "HS256"
      )
      render json: { token: token }
    else
      render json: { error: "invalid credentials" }, status: :unauthorized
    end
  end
end
