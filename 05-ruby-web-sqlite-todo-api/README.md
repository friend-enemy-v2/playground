# 05 Ruby SQLite Todo API

04 REST APIのTodoを、メモリ上の配列ではなくSQLiteへ保存する。

## Setup

```bash
bundle install
bundle exec ruby server.rb
```

## Try

一覧:

```bash
curl http://127.0.0.1:4003/todos
```

追加:

```bash
curl -X POST http://127.0.0.1:4003/todos \
  -H 'Content-Type: application/json' \
  -d '{"title":"SQLiteを学ぶ"}'
```

サーバーを停止して再起動してもTodoが残ることを確認する。

## 見るポイント

- プロセス内メモリとDB永続化の違い
- `CREATE TABLE`
- `SELECT` / `INSERT`
- SQLのプレースホルダー `?`
- DBが自動でIDを採番する仕組み

本番ではマイグレーション、接続管理、ORM、トランザクションなどの仕組みを使うことが多い。
