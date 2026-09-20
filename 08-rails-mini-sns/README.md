# 08 Rails Mini SNS

07のMini SNSをRails APIとして書き直し、フレームワークが何を隠すか比較する。

## Setup

```bash
bundle install
bin/rails db:migrate
bin/rails server -p 4006
```

## 07との対応

- 手書きのmethod/path判定 -> `config/routes.rb`
- 手書きSQL -> Active Record
- bcrypt直接操作 -> `has_secure_password`
- users/postsの関連 -> `has_many` / `belongs_to`
- HTTPレスポンス組み立て -> `render json:`
- 認証前処理 -> `before_action`

APIは07と同じく、ユーザー登録、JWTログイン、投稿一覧、認証付き投稿作成。

## 見るポイント

Railsではコード量が減る代わりに、規約とフレームワーク内部の処理が増える。
07で手書きした仕組みがどこへ移ったのかを追うことが目的。
