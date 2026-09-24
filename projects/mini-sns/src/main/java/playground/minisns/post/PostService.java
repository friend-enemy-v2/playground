package playground.minisns.post;

import playground.minisns.user.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository posts;
    private final CommentRepository comments;
    private final UserRepository users;

    public PostService(PostRepository posts, CommentRepository comments, UserRepository users) {
        this.posts = posts;
        this.comments = comments;
        this.users = users;
    }

    @Transactional
    public Post create(String username, String body) {
        return posts.save(new Post(currentUser(username), body));
    }

    @Transactional(readOnly = true)
    public Page<Post> list(Pageable pageable) {
        return posts.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional
    public Post edit(String username, long postId, String body) {
        Post post = post(postId);
        requireOwner(username, post);
        post.edit(body);
        return post;
    }

    @Transactional
    public void delete(String username, long postId) {
        Post post = post(postId);
        requireOwner(username, post);
        posts.delete(post);
    }

    @Transactional
    public Comment comment(String username, long postId, String body) {
        return comments.save(new Comment(post(postId), currentUser(username), body));
    }

    @Transactional
    public void deleteComment(String username, long commentId) {
        Comment comment = comments.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("comment not found"));

        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new SecurityException("not your comment");
        }
        comments.delete(comment);
    }

    @Transactional
    public void like(String username, long postId) {
        posts.like(currentUser(username).getId(), postId);
    }

    @Transactional
    public void unlike(String username, long postId) {
        posts.unlike(currentUser(username).getId(), postId);
    }

    private User currentUser(String username) {
        return users.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("user not found"));
    }

    private Post post(long id) {
        return posts.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
    }

    private void requireOwner(String username, Post post) {
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new SecurityException("not your post");
        }
    }
}
