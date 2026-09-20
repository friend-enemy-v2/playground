# 02 Ruby TCP Echo

TCPで送った文字列を、そのまま返す最小のサーバーとクライアント。

## Run

Terminal 1:

```bash
ruby server.rb
```

Terminal 2:

```bash
ruby client.rb
```

サーバーは `127.0.0.1:4000` で待ち受ける。

見るポイントは `TCPServer`、`TCPSocket`、`accept`、`gets` / `write`。
