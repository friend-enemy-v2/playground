# 15 JVM / GC

## Goal
Java objectが「どこにいて、いつゴミ扱いになるか」をイメージする。

- JVM = Java bytecodeを実行する環境
- Stack = method呼び出しごとの作業机
- Heap = objectを置く共有の大きな部屋
- Reference = objectへたどり着く手掛かり
- GC = 到達できなくなったobjectのメモリを再利用可能にする仕組み
- GC eligible = 回収してよい候補。即消えるという意味ではない

## Mental model
```text
Stack（作業机）             Heap（物置）
user ───────────────────→ [User object]
temporary ──────────────→ [5MB object]

 temporary = null
                           [5MB object]
                           ↑ 強い参照から到達不能
                           GCが後で回収可能
```

変数をnullにした瞬間にGCが走るわけではない。

## Javaが動くまで
`.java → javac → .class(bytecode) → JVM → interpreter/JIT → CPU`

JITは実行中にmachine codeへcompileして高速化する仕組み。

## Experiment
```bash
mvn compile
java -Xms32m -Xmx32m -Xlog:gc -cp target/classes playground.jvm.GcDemo
```

GC logとheap使用量を見る。数値やGCタイミングは実行ごとに変わり得る。

## Failure experiment
`kept` に入れるobjectを増やす。参照を保持しているobjectはGCが勝手に捨てられないので、小さい `-Xmx` の上限ではOutOfMemoryErrorになり得る。

## Important
`System.gc()` も即時GCの保証ではない。GCはファイル削除でもresource closeでもない。DB Connectionやfile handleはGC任せにせずcloseする。

`jvm-gc.html` でStack / Heap / Reference / GCを図で確認できる。
