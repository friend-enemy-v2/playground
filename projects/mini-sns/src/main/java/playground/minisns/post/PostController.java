package playground.minisns.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    Page<PostResponse> list(@PageableDefault(size = 20) Pageable pageable) {
        return service.list(pageable).map(PostResponse::from);
    }

    @PostMapping
    PostResponse create(Authentication auth, @Valid @RequestBody BodyRequest request) {
        return PostResponse.from(service.create(auth.getName(), request.body()));
    }

    @PatchMapping("/{id}")
    PostResponse edit(Authentication auth, @PathVariable long id, @Valid @RequestBody BodyRequest request) {
        return PostResponse.from(service.edit(auth.getName(), id, request.body()));
    }

    @DeleteMapping("/{id}")
    void delete(Authentication auth, @PathVariable long id) {
        service.delete(auth.getName(), id);
    }

    @PostMapping("/{id}/comments")
    CommentResponse comment(Authentication auth, @PathVariable long id, @Valid @RequestBody CommentRequest request) {
        Comment comment = service.comment(auth.getName(), id, request.body());
        return new CommentResponse(comment.getId(), comment.getAuthor().getUsername(), comment.getBody());
    }

    @DeleteMapping("/comments/{id}")
    void deleteComment(Authentication auth, @PathVariable long id) {
        service.deleteComment(auth.getName(), id);
    }

    @PutMapping("/{id}/like")
    void like(Authentication auth, @PathVariable long id) {
        service.like(auth.getName(), id);
    }

    @DeleteMapping("/{id}/like")
    void unlike(Authentication auth, @PathVariable long id) {
        service.unlike(auth.getName(), id);
    }

    record BodyRequest(@NotBlank @Size(max = 500) String body) {}
    record CommentRequest(@NotBlank @Size(max = 300) String body) {}
    record CommentResponse(long id, String username, String body) {}

    record PostResponse(long id, String username, String body, Instant createdAt) {
        static PostResponse from(Post post) {
            return new PostResponse(
                post.getId(),
                post.getAuthor().getUsername(),
                post.getBody(),
                post.getCreatedAt()
            );
        }
    }
}
