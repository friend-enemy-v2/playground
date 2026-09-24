package playground.minisns.post;

import jakarta.persistence.*;
import lombok.*;
import playground.minisns.user.User;

import java.time.Instant;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(nullable = false, length = 500)
    private String body;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    public Post(User author, String body) {
        this.author = author;
        this.body = body;
    }

    public void edit(String body) {
        this.body = body;
    }
}
