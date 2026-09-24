package playground.minisns.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    Profile profile(@PathVariable String username) {
        return Profile.from(service.user(username));
    }

    @PatchMapping("/me")
    Profile update(Authentication auth, @Valid @RequestBody ProfileRequest request) {
        return Profile.from(service.updateProfile(auth.getName(), request.displayName(), request.bio()));
    }

    @PutMapping("/{username}/follow")
    void follow(Authentication auth, @PathVariable String username) {
        service.follow(auth.getName(), username);
    }

    @DeleteMapping("/{username}/follow")
    void unfollow(Authentication auth, @PathVariable String username) {
        service.unfollow(auth.getName(), username);
    }

    // フォローバックも「相手をfollowする」ので同じendpointで表現できる。
    @PutMapping("/{username}/block")
    void block(Authentication auth, @PathVariable String username) {
        service.block(auth.getName(), username);
    }

    @DeleteMapping("/{username}/block")
    void unblock(Authentication auth, @PathVariable String username) {
        service.unblock(auth.getName(), username);
    }

    @PutMapping("/{username}/mute")
    void mute(Authentication auth, @PathVariable String username) {
        service.mute(auth.getName(), username);
    }

    @DeleteMapping("/{username}/mute")
    void unmute(Authentication auth, @PathVariable String username) {
        service.unmute(auth.getName(), username);
    }

    record ProfileRequest(
        @NotBlank @Size(max = 100) String displayName,
        @Size(max = 300) String bio
    ) {}

    record Profile(long id, String username, String displayName, String bio) {
        static Profile from(User user) {
            return new Profile(user.getId(), user.getUsername(), user.getDisplayName(), user.getBio());
        }
    }
}
