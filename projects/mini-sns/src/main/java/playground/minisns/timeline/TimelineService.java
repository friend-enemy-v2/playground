package playground.minisns.timeline;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import playground.minisns.user.*;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class TimelineService {
    private final JdbcClient jdbc;
    private final UserRepository users;

    public TimelineService(JdbcClient jdbc, UserRepository users) {
        this.jdbc = jdbc;
        this.users = users;
    }

    public List<TimelinePost> timeline(String username, int limit, int offset) {
        User me = users.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("user not found"));

        // TimelineはJOIN/NOT EXISTS/paginationを見せるため、あえてSQLを明示する。
        return jdbc.sql("""
            SELECT p.id, u.username, p.body, p.created_at
            FROM posts p
            JOIN users u ON u.id = p.user_id
            WHERE (
                p.user_id = :me
                OR p.user_id IN (
                    SELECT followed_id FROM follows WHERE follower_id = :me
                )
            )
            AND NOT EXISTS (
                SELECT 1 FROM mutes m
                WHERE m.muter_id = :me AND m.muted_id = p.user_id
            )
            AND NOT EXISTS (
                SELECT 1 FROM blocks b
                WHERE (b.blocker_id = :me AND b.blocked_id = p.user_id)
                   OR (b.blocker_id = p.user_id AND b.blocked_id = :me)
            )
            ORDER BY p.created_at DESC
            LIMIT :limit OFFSET :offset
            """)
            .param("me", me.getId())
            .param("limit", Math.min(limit, 100))
            .param("offset", Math.max(offset, 0))
            .query((rs, rowNum) -> new TimelinePost(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("body"),
                rs.getObject("created_at", OffsetDateTime.class)
            ))
            .list();
    }

    public record TimelinePost(long id, String username, String body, OffsetDateTime createdAt) {}
}
