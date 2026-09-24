package playground.minisns.user;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface RelationshipRepository extends Repository<User, Long> {
    @Modifying
    @Query(value = "INSERT INTO follows(follower_id, followed_id) VALUES (:from, :to) ON CONFLICT DO NOTHING", nativeQuery = true)
    void follow(@Param("from") long from, @Param("to") long to);

    @Modifying
    @Query(value = "DELETE FROM follows WHERE follower_id = :from AND followed_id = :to", nativeQuery = true)
    void unfollow(@Param("from") long from, @Param("to") long to);

    @Modifying
    @Query(value = "DELETE FROM follows WHERE (follower_id = :a AND followed_id = :b) OR (follower_id = :b AND followed_id = :a)", nativeQuery = true)
    void removeBothFollows(@Param("a") long a, @Param("b") long b);

    @Modifying
    @Query(value = "INSERT INTO blocks(blocker_id, blocked_id) VALUES (:from, :to) ON CONFLICT DO NOTHING", nativeQuery = true)
    void block(@Param("from") long from, @Param("to") long to);

    @Modifying
    @Query(value = "DELETE FROM blocks WHERE blocker_id = :from AND blocked_id = :to", nativeQuery = true)
    void unblock(@Param("from") long from, @Param("to") long to);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM blocks WHERE (blocker_id = :a AND blocked_id = :b) OR (blocker_id = :b AND blocked_id = :a))", nativeQuery = true)
    boolean blockedEitherWay(@Param("a") long a, @Param("b") long b);

    @Modifying
    @Query(value = "INSERT INTO mutes(muter_id, muted_id) VALUES (:from, :to) ON CONFLICT DO NOTHING", nativeQuery = true)
    void mute(@Param("from") long from, @Param("to") long to);

    @Modifying
    @Query(value = "DELETE FROM mutes WHERE muter_id = :from AND muted_id = :to", nativeQuery = true)
    void unmute(@Param("from") long from, @Param("to") long to);
}
