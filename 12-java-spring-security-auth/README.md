# 12 Authentication / Spring Security

## これは何？
APIの手前で「誰なのか」と「このAPIを使ってよいか」を確認する仕組み。

- Authentication（認証）= あなたは誰？
- Authorization（認可）= あなたはこれをしてよい？

リクエストはControllerへ直行せず、先にSpring Securityを通る。

`Client → SecurityFilterChain → Controller`

## コードは何をしている？
`/public` はログイン不要、`/me` はログイン必須にしている。

SecurityConfigの長いチェーンは、左から順に「URLごとのルール → HTTP Basic → 設定完成」と読めばよい。

## 実務ではどこで使う？
ログイン必須API、管理者だけのAPI、本人だけが操作できる更新・削除など。

## よくあるミス：公開範囲を広げすぎる
`MistakeSecurityConfig.java.example` を参照。

「public APIが増えたからまとめて許可しよう」と `/api/**` を `permitAll()` にすると、その配下に後から追加した管理APIまで認証なしで通る可能性がある。

コード自体は正常に動くので気付きにくい。これは認証認可で特に危険な種類のバグ。

**対策:** 公開するURLだけを狭く指定し、基本は `anyRequest().authenticated()` 側へ倒す。

## 次に何につながる？
JWT / Cookie Session / Role・権限管理 / OAuth・OIDC。

## Run
```bash
mvn spring-boot:run
curl http://localhost:8080/public
curl -i http://localhost:8080/me
curl -u yuki:playground http://localhost:8080/me
```
