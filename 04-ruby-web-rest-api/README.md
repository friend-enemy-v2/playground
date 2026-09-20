# 04 Ruby REST API

HTTPを自分で処理して、JSONのTodo APIを作る。

## Run

```bash
ruby server.rb
```

一覧:

```bash
curl http://127.0.0.1:4002/todos
```

追加:

```bash
curl -X POST http://127.0.0.1:4002/todos \
  -H 'Content-Type: application/json' \
  -d '{"title":"Rubyを学ぶ"}'
```

見るポイントはHTTP method、path、status code、JSON、`Content-Length`。
