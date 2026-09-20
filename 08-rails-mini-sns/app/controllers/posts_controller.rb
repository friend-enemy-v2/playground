class PostsController < ApplicationController
  before_action :require_user, only: :create

  def index
    posts = Post.includes(:user).order(id: :desc)
    render json: posts.map { |post|
      {
        id: post.id,
        body: post.body,
        created_at: post.created_at,
        user: { id: post.user.id, email: post.user.email }
      }
    }
  end

  def create
    post = current_user.posts.new(body: params[:body])

    if post.save
      render json: {
        id: post.id,
        body: post.body,
        created_at: post.created_at,
        user: { id: current_user.id, email: current_user.email }
      }, status: :created
    else
      render json: { errors: post.errors.full_messages }, status: :unprocessable_entity
    end
  end
end
