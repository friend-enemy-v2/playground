import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadedHttpServer {
    private static final AtomicInteger requestCount = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 4007), 0);
        // リクエストごとに無制限にThreadを作らず、4本のworkerを再利用する。
        ExecutorService workers = Executors.newFixedThreadPool(4);
        server.setExecutor(workers);
        server.createContext("/", ThreadedHttpServer::handle);
        server.start();
        System.out.println("Threaded HTTP server listening on http://127.0.0.1:4007");
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String thread = Thread.currentThread().getName();
        // 普通のintの++は複数Threadで競合できる。AtomicIntegerで原子的に増やす。
        int count = requestCount.incrementAndGet();
        try {
            Thread.sleep(1000); // 並行処理を観察しやすくする
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String body = "{\"thread\":\"" + thread + "\",\"requestCount\":" + count + "}";
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}