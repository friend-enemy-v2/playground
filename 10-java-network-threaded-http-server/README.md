# 10 Java Threaded HTTP Server

HTTPリクエストを複数Threadで同時処理し、並行処理・共有状態・race condition・thread poolを理解する。

## Run

\`\`\`bash
cd 10-java-network-threaded-http-server
javac --add-modules jdk.httpserver ThreadedHttpServer.java
java --add-modules jdk.httpserver ThreadedHttpServer
\`\`\`

別ターミナルから同時に送る。

\`\`\`bash
for i in {1..8}; do curl -s http://127.0.0.1:4007/ & done; wait
\`\`\`

レスポンスの \`thread\` が複数種類になり、4 workerが並行して処理することを確認する。

## Thread / shared state

1つのThreadは「処理の流れ」1本。複数Threadは同じJavaプロセスのheapを共有するため、同じオブジェクトへ同時アクセスできる。

\`requestCount++\` は見た目は1操作でも、概念的には read → add → write。2 Threadが同じ値をreadすると更新が消えることがある。これがrace conditionの一例。

今回は \`AtomicInteger.incrementAndGet()\` で「読み取り→加算→書き込み」を原子的に扱う。

## なぜThread Poolか

リクエストごとに \`new Thread(...)\` するとアクセス数に応じてThreadが増え続ける。Threadにはstack memoryやschedulerのコストがある。

固定Thread Poolなら、今回は4 workerだけを再利用し、5件目以降はworkerが空くまでqueueで待つ。

## Visual guide

\`threading.html\` をブラウザで開くと、single thread / thread pool / race condition / request flowを図で確認できる。