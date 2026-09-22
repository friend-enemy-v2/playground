# 13 Java Mini SNS

## Goal
09〜12で見た部品を「小さい実アプリ」にまとめる。

- GET /posts: 誰でも読める
- POST /posts: ログイン必須
- DELETE /posts/{id}: 自分の投稿だけ削除

保存はあえてメモリ。ここでDB設計まで広げず、HTTP + Spring Boot + Security + userとpostの関係を一度つなげる。

## Flow
Client → Spring Security → Controller → posts

投稿時はPrincipalからログインuser名を取得する。つまりrequest bodyにauthorを書かせない。本人情報は認証結果から取る。

## Try
```bash
mvn spring-boot:run
curl http://localhost:8080/posts
curl -u yuki:playground -H 'Content-Type: application/json' -d '{"text":"hello"}' http://localhost:8080/posts
curl -u yuki:playground -X DELETE http://localhost:8080/posts/1
```

## Failure experiment
認証なしPOSTは401。存在しない投稿、または自分の投稿でないIDのDELETEは404。

## Compare with Ruby/Rails
07では部品を手で組み、08ではRailsがrouting/model/auth前処理を隠した。13ではSpringのannotationとSecurity Filterを使って同じ「Webアプリの部品がどう接続されるか」を見る。
