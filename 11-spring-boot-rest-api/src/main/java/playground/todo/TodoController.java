package playground.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final List<Todo> todos = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @GetMapping
    public List<Todo> index() {
        return todos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@RequestBody CreateTodoRequest request) {
        Todo todo = new Todo(nextId.getAndIncrement(), request.title(), false);
        todos.add(todo);
        return todo;
    }

    // 09ではHTTP bodyをStringで受けて自分でJSONを解析した。
    // Spring MVCでは@RequestBodyがJSON -> Java型への変換を担当する。
    public record CreateTodoRequest(String title) {}
    public record Todo(int id, String title, boolean done) {}
}