# 07 Ruby Mini SNS

05のSQLiteと06の認証を組み合わせた最小SNS。

## 機能

- ユーザー登録
- JWTログイン
- 投稿作成
- 投稿一覧

## API

- `POST /users`
- `POST /login`
- `GET /posts`
- `POST /posts`（JWT必須）

## 見るポイント

`users` と `posts` を `user_id` で関連付ける。
投稿一覧では `JOIN` して投稿者情報も取得する。
投稿時はJWTから現在のユーザーを特定し、そのIDを投稿へ保存する。

06ではJWTとSessionを比較したが、07はSNS全体の流れを小さく保つためJWTに絞る。
