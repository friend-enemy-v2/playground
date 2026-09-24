package playground.minisns.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository users;
    private final RelationshipRepository relationships;

    public UserService(UserRepository users, RelationshipRepository relationships) {
        this.users = users;
        this.relationships = relationships;
    }

    @Transactional
    public User updateProfile(String username, String displayName, String bio) {
        User user = user(username);
        user.updateProfile(displayName, bio);
        return user;
    }

    @Transactional
    public void follow(String me, String target) {
        User from = user(me);
        User to = user(target);
        requireDifferent(from, to);

        if (relationships.blockedEitherWay(from.getId(), to.getId())) {
            throw new SecurityException("blocked users cannot follow each other");
        }
        relationships.follow(from.getId(), to.getId());
    }

    @Transactional
    public void unfollow(String me, String target) {
        relationships.unfollow(user(me).getId(), user(target).getId());
    }

    @Transactional
    public void block(String me, String target) {
        User from = user(me);
        User to = user(target);
        requireDifferent(from, to);

        relationships.removeBothFollows(from.getId(), to.getId());
        relationships.block(from.getId(), to.getId());
    }

    @Transactional
    public void unblock(String me, String target) {
        relationships.unblock(user(me).getId(), user(target).getId());
    }

    @Transactional
    public void mute(String me, String target) {
        User from = user(me);
        User to = user(target);
        requireDifferent(from, to);
        relationships.mute(from.getId(), to.getId());
    }

    @Transactional
    public void unmute(String me, String target) {
        relationships.unmute(user(me).getId(), user(target).getId());
    }

    @Transactional(readOnly = true)
    public User user(String username) {
        return users.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("user not found"));
    }

    private void requireDifferent(User a, User b) {
        if (a.getId().equals(b.getId())) {
            throw new IllegalArgumentException("cannot target yourself");
        }
    }
}
