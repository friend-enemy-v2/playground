# 14 Connection Pool

## これは何？
DB Connectionを毎回作り直さず、Poolから借りて使い終わったら返す仕組み。

`Request → Thread → DataSource → Pool → Connection → DB`

## コードは何をしている？
PoolにConnectionを2本まで用意し、3つのworkerが順番に借りる。
2本とも使用中なら、3人目は返却されるまで待つ。

HikariCP管理下では `connection.close()` は通常「DB接続を完全に破棄」ではなく「Poolへ返す」という意味になる。

## 実務ではどこで使う？
Spring BootなどからDBへアクセスするとき。大量リクエストが来てもDB Connectionを無制限に増やさず、接続確立コストも減らす。

## よくあるミス：Connectionを返し忘れる
`ConnectionLeakDemo.java.example` を参照。

`getConnection()` した後、正常系では `close()` していても、途中で例外やreturnが入ると返却処理を通らないコードは書けてしまう。

これがリクエストごとに積み重なるとPoolのConnectionが全部貸出中になり、関係ない正常なリクエストまでConnection待ち・timeoutになる。

**対策:** Connectionはtry-with-resourcesで取得する。処理が成功しても失敗しても自動でcloseされ、Poolへ返る。

## 次に何につながる？
DB timeout調査 / SQL性能 / Transaction / Thread数とPool sizeの調整 / 本番の負荷調査。

## Run
```bash
mvn compile dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "target/classes:$(cat cp.txt)" playground.pool.PoolDemo
```
