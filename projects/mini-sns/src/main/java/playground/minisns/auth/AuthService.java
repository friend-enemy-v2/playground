package playground.minisns.auth;

import playground.minisns.user.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public String register(String username, String displayName, String password) {
        if (users.existsByUsername(username)) {
            throw new IllegalArgumentException("username already exists");
        }

        User user = new User(username, displayName, passwordEncoder.encode(password));
        users.save(user);
        return jwtService.issue(user.getUsername());
    }

    @Transactional(readOnly = true)
    public String login(String username, String password) {
        User user = users.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("invalid credentials");
        }

        return jwtService.issue(user.getUsername());
    }
}
