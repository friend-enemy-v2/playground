# 06 Ruby Login API

同じユーザー認証を、JWTとCookie/Sessionの2方式で比較する。

## Setup

```bash
bundle install
bundle exec ruby server.rb
```

まずユーザーを作る:

```bash
curl -X POST http://127.0.0.1:4004/users \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"password"}'
```

## JWT

```bash
ruby jwt_client.rb
```

`POST /login/jwt` でJWTを受け取り、`Authorization: Bearer ...` で `GET /me` を呼ぶ。

## Cookie / Session

```bash
ruby session_client.rb
```

`POST /login/session` で `Set-Cookie` を受け取り、そのCookieで `GET /me` を呼ぶ。

## 見るポイント

- パスワードはbcryptのハッシュとして保存する
- JWTはクライアント側がトークンを保持する
- Sessionはサーバー側がsession IDとuser IDの対応を保持する
- CookieはSession IDをHTTPで運ぶ
- どちらも最終的には「このリクエストは誰か」を判定している

学習用のためJWT secretは固定値、Sessionはメモリ保存。本番ではsecret管理、HTTPS、期限・失効、CSRF対策、永続Sessionストアなどが必要。
