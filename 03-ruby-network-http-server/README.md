# 03 Ruby HTTP Server

Rubyの `TCPServer` だけでHTTPレスポンスを返す。

## Run

```bash
ruby server.rb
```

ブラウザで `http://127.0.0.1:4001/hello` を開く。

見るポイントは、HTTPがTCP接続上でリクエスト行・ヘッダー・ボディをやり取りするプロトコルであること。
