import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodoServer {
    private static final List<Todo> todos = new ArrayList<>();
    private static int nextId = 1;

    // Rubyでは Hash { id: 1, title: "...", done: false } のように
    // 実行時に自由な形のデータを作れる。JavaではTodoという型を先に定義し、
    // 各フィールドが何型なのかをコンパイル時に決める。
    record Todo(int id, String title, boolean done) {}

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 4006), 0);
        server.createContext("/todos", TodoServer::handleTodos);
        server.start();
        System.out.println("REST API listening on http://127.0.0.1:4006");
    }

    private static void handleTodos(HttpExchange exchange) throws IOException {
        // Ruby版の method, path = ... のような動的な値も、
        // Javaでは戻り値の型(String)がメソッド定義で決まっている。
        String method = exchange.getRequestMethod();

        if ("GET".equals(method)) {
            sendJson(exchange, 200, todosJson());
            return;
        }

        if ("POST".equals(method)) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String title = extractTitle(body);
            if (title == null || title.isBlank()) {
                sendJson(exchange, 400, "{\"error\":\"title is required\"}");
                return;
            }

            Todo todo = new Todo(nextId++, title.strip(), false);
            todos.add(todo);
            sendJson(exchange, 201, todoJson(todo));
            return;
        }

        sendJson(exchange, 405, "{\"error\":\"method not allowed\"}");
    }

    private static String extractTitle(String json) {
        // JSONライブラリをまだ導入せず、HTTP/APIとJavaの型の違いに集中するための最小実装。
        Matcher matcher = Pattern.compile("\\\"title\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"").matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static String todosJson() {
        return "[" + todos.stream().map(TodoServer::todoJson).reduce((a, b) -> a + "," + b).orElse("") + "]";
    }

    private static String todoJson(Todo todo) {
        return "{\"id\":" + todo.id() + ",\"title\":\"" + escape(todo.title()) + "\",\"done\":" + todo.done() + "}";
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
