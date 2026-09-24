# 15 JVM / GC

## これは何？
Javaのobjectは主にHeapに置かれ、どこからも到達できなくなったobjectをGCが回収する。

`null` にした瞬間に削除されるわけではない。
「もう到達できないので、GCが回収してよい候補になる」と考える。

## コードは何をしている？
一時objectへの強い参照を外し、GCの回収候補になる流れとHeap使用量を見る。

`System.gc()` はGCを依頼するだけで、即時回収を保証しない。

## 実務ではどこで使う？
Javaアプリのメモリ増加、OutOfMemoryError、GC負荷、長時間動かすサーバーの調査。

## よくあるミス：使い終わったobjectを保持し続ける
`MemoryRetentionDemo.java.example` を参照。

「後で使うかもしれない」とstaticなListやMapへrequestデータやcacheを追加し続けるコードは普通に書ける。

処理自体は終わっていても、List → object という参照が残っているためGCから見るとまだ使用中。
GCを何回動かしても回収できず、長時間運用するとHeapが増え続ける。

これは「GCがあるからメモリ管理を考えなくてよい」が間違いだと分かる例。

**対策:** 不要になった参照を長寿命Collectionへ残さない。cacheなら件数上限・期限・削除戦略を持たせる。

## 次に何につながる？
Heap dump / profiler / GC log / memory leak調査 / cache設計 / JVM tuning。

## Run
```bash
mvn compile
java -Xms32m -Xmx32m -Xlog:gc -cp target/classes playground.jvm.GcDemo
```

DB Connectionやfile handleはGC任せにせずcloseする。
