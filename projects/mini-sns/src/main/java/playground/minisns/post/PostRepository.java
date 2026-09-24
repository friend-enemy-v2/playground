package playground.minisns.post;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = "author")
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO likes(user_id, post_id) VALUES (:userId, :postId) ON CONFLICT DO NOTHING", nativeQuery = true)
    void like(@Param("userId") long userId, @Param("postId") long postId);

    @Modifying
    @Query(value = "DELETE FROM likes WHERE user_id = :userId AND post_id = :postId", nativeQuery = true)
    void unlike(@Param("userId") long userId, @Param("postId") long postId);
}
