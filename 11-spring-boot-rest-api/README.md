# 11 Spring Boot REST API

09のJava REST APIをSpring Bootで書き直し、フレームワークが何を隠すかを見る。

## Run

Java 17+ / Maven。

\`\`\`bash
cd 11-spring-boot-rest-api
mvn spring-boot:run
\`\`\`

\`\`\`bash
curl http://localhost:8080/todos
curl -X POST http://localhost:8080/todos -H 'Content-Type: application/json' -d '{"title":"Spring Bootを学ぶ"}'
\`\`\`

## 09 → 11で消えたもの

09ではHTTP serverの生成、status、Content-Type、bodyのbyte変換、JSON文字列生成まで自分で扱った。

Spring Bootでは \`@RestController\`, \`@GetMapping\`, \`@PostMapping\`, \`@RequestBody\` を宣言すると、Spring MVCと組み込みWeb serverがその定型処理を担当する。

\`\`\`text
Client
  ↓ HTTP
Embedded Web Server
  ↓
Spring MVC
  ↓ routing / binding
TodoController
  ↓
Java object
  ↓
JSON response
\`\`\`

## SpringとSpring Bootは別

**Spring Framework** の中心はIoC / DIをはじめとした柔軟なJavaアプリケーション基盤。初期Springは、当時のJ2EEの複雑さに対する応答として生まれた。

**Spring Boot** はSpringの上に「まず動くデフォルト」を足す。Spring公式はBootを opinionated と説明し、auto-configuration、starter dependencies、embedded serverなどで設定量を減らしている。

この違いは重要。

- Spring Framework: 選択肢・柔軟性を重視する
- Spring Boot: よくある選択をデフォルト化して開始を速くする
- 必要になればBootのデフォルトから外れてSpringの柔軟性を使える

## Railsとの比較

共通点は **Convention over Configurationによって、普通のWebアプリで毎回必要な設定を減らす** こと。

Railsはフレームワーク全体として規約が非常に強く、RubyのDSLやActive Recordなどを組み合わせて「Rails way」を提供する。

Spring Bootは既存の巨大なSpring ecosystemの上にopinionated defaultsを置く。Spring Framework自体はRailsほど「この方法で作れ」とは決めず、Bootが使いやすい入口を作る。

\`\`\`text
Rails
  Ruby + MVC + Active Record
  強い規約
       │
       └── 高速なWeb開発体験

Spring Framework
  IoC / DI + 柔軟な部品群
       │
       ▼
Spring Boot
  opinionated defaults
  auto configuration
  starters
  embedded server
       │
       └── 高速なSpring開発体験
\`\`\`

## 何から影響を受けたか

Spring Bootチームは、Rails、Grails、Spring Rooなどの **convention-over-configuration frameworkの良いパターンを取り入れた** とSpring公式ブログで説明している。

ただし「SpringがRailsから生まれた」という意味ではない。Spring Frameworkの起源は2002〜2003年ごろで、Railsより前。後にSpring上で動くGrailsがRails的な規約をJava/Groovy世界へ持ち込み、その後Spring BootがRails/Grailsなどで成功したCoCのパターンをSpring ecosystemへ取り込んだ、という流れが近い。

\`\`\`text
early J2EE complexity
       ↓
Spring Framework (2002/2003〜)
       ↓
   Spring ecosystem
       │
       ├──────────────┐
       ↓              │
    Grails ← Rails    │
       │              │
       └──────┐       │
              ↓       ↓
            Spring Boot (2013〜)
        CoC + opinionated defaults
\`\`\`

## 何に影響したか

Spring Bootは「Springアプリは外部Application ServerへWARを配備する」という従来型だけでなく、組み込みserverを含むstand-alone applicationを \`java -jar\` で起動する体験を標準的にした。

その後のJava/JVMバックエンドでは、フレームワーク側が依存関係・server・設定のデフォルトをまとめ、アプリ単体で起動できる開発体験が非常に一般的になった。

## Sources

- Spring Boot Reference: https://docs.spring.io/spring-boot/
- Spring Boot project: https://spring.io/projects/spring-boot
- Spring Framework overview: https://docs.spring.io/spring-framework/reference/overview.html
- Spring: the foundation for Grails: https://spring.io/blog/2010/06/08/spring-the-foundation-for-grails
- This Decade in Spring: https://spring.io/blog/2019/12/31/this-decade-in-spring-happy-new-year-edition-december-31-2019
