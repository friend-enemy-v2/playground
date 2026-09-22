package playground.sns;
import java.security.Principal; import java.util.*; import java.util.concurrent.atomic.AtomicInteger; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController public class PostController { private final List<Post> posts=Collections.synchronizedList(new ArrayList<>()); private final AtomicInteger ids=new AtomicInteger(1);
 @GetMapping("/posts") List<Post> index(){return List.copyOf(posts);} @PostMapping("/posts") @ResponseStatus(HttpStatus.CREATED) Post create(@RequestBody NewPost body, Principal p){var post=new Post(ids.getAndIncrement(),p.getName(),body.text());posts.add(post);return post;}
 @DeleteMapping("/posts/{id}") ResponseEntity<Void> delete(@PathVariable int id,Principal p){boolean removed=posts.removeIf(x->x.id()==id&&x.author().equals(p.getName()));return removed?ResponseEntity.noContent().build():ResponseEntity.notFound().build();}
 record NewPost(String text){} record Post(int id,String author,String text){}
}
