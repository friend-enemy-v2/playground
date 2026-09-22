# 12 Authentication / Spring Security

## Goal
11で作ったAPIに「誰が使っているか」の境界を追加する。

今回は仕組みを小さく見るため **HTTP Basic + Spring Security** を使う。JWTは「資格情報を毎回送る」という点は似るが、署名付きtokenを検証する方式。まずFilter Chainを理解してからJWTへ進む方がSpring Securityの役割が見えやすい。

## Mental model
リクエストはControllerへ直行しない。

Client → SecurityFilterChain → 認証OK? → Controller

- Authentication: あなたは誰？
- Authorization: あなたはこれをしてよい？
- PasswordEncoder: passwordそのものではなくbcrypt hashで照合する

## Run
Java 17+ / Maven。

```bash
mvn spring-boot:run
curl http://localhost:8080/public
curl -i http://localhost:8080/me
curl -u yuki:playground http://localhost:8080/me
```

2つ目は401、3つ目はuser名を返す。

## Failure experiment
passwordをわざと間違える。Controllerにbreakpointを置くと、認証失敗時はControllerまで来ないことを確認できる。

## Production difference
サンプルなのでuserはメモリ固定。実運用ではDB、登録処理、権限、token/session、秘密情報管理などが必要。06 Ruby Login APIのJWT/Sessionと比較して「認証方式」と「Spring Securityという入口の仕組み」を分けて考える。
