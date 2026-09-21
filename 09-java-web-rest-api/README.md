# 09 Java REST API

04 Ruby REST APIと同じTodo APIをJavaで作り、動的型付けと静的型付けの違いを見る。

## Run

Java 17+。

```bash
cd 09-java-web-rest-api
javac --add-modules jdk.httpserver TodoServer.java
java --add-modules jdk.httpserver TodoServer
```

```bash
curl http://127.0.0.1:4006/todos

curl -X POST http://127.0.0.1:4006/todos \
  -H 'Content-Type: application/json' \
  -d '{"title":"Javaを学ぶ"}'
```

## Rubyとの違い

Ruby版ではTodoをHashとしてその場で組み立てた。

```ruby
todo = { id: next_id, title: title, done: false }
```

Java版では先にデータ構造を型として宣言する。

```java
record Todo(int id, String title, boolean done) {}
Todo todo = new Todo(nextId++, title, false);
```

Javaでは `Todo` の `id` に突然Stringを入れる、といったコードはコンパイル時に拒否される。Rubyでは柔軟に値を扱える代わりに、多くの型の間違いは実行するまで分からない。

ほかにも、Javaはメソッドの引数・戻り値に型があり、checked exceptionの `IOException` を明示的に扱う。Rubyより記述量は増えるが、「何が入って何が返るか」がコード上に現れやすい。

## 今回あえて使わないもの

Spring BootやJSONライブラリはまだ使わない。04でHTTPを手書きした後にRailsを見たのと同じく、まずJava標準API寄りの実装を見てからSpring Bootで何が隠れるか比較する。
