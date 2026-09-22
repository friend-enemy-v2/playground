# 14 Connection Pool

## Goal
DB接続の裏側を **貸出カウンター** としてイメージする。

- Connection = アプリとDBが会話する1本の接続
- Pool = Connectionを置く貸出棚
- borrow = 棚から借りる
- return = SQL後に棚へ返す
- pool size = 同時に貸せる本数
- timeout = 空きを待てる時間

HikariCP管理下の `connection.close()` は、通常「物理接続を毎回壊す」ではなくPoolへの返却になる。

## Experiment
Pool 2本にworker 3人を向かわせる。

```text
Pool [A][B]
      ↓  ↓
     W1  W2     W3「空き待ち」

W1が返す → AをW3が借りる
```

```bash
mvn compile dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "target/classes:$(cat cp.txt)" playground.pool.PoolDemo
```

## Why?
毎requestで新規接続すると接続確立コストがある。一方、無制限な接続はDBを圧迫する。Poolは再利用と同時接続数の上限を扱う。

## Failure experiment
Pool sizeを1、sleepを1500ms、timeoutを1000msにする。空きを待ちきれないworkerがtimeoutする。

## 10 Threadとの接続
HTTPを処理するThreadが増えてもDB Connectionを無限には増やさない。イメージは **Request → Thread → DataSource → Pool → Connection → DB**。

`connection-pool.html` でも確認できる。
