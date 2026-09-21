class ApplicationController < ActionController::API
  JWT_SECRET = ENV.fetch("JWT_SECRET", "learning-secret")

  private

  def current_user
    header = request.headers["Authorization"].to_s
    return unless header.start_with?("Bearer ")

    token = header.delete_prefix("Bearer ")
    payload, = JWT.decode(token, JWT_SECRET, true, algorithm: "HS256")
    User.find_by(id: payload["sub"])
  rescue JWT::DecodeError
    nil
  end

  def require_user
    return if current_user

    render json: { error: "authentication required" }, status: :unauthorized
  end
end
