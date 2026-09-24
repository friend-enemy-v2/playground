package playground.minisns.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    TokenResponse register(@Valid @RequestBody RegisterRequest request) {
        return new TokenResponse(authService.register(
            request.username(),
            request.displayName(),
            request.password()
        ));
    }

    @PostMapping("/login")
    TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return new TokenResponse(authService.login(request.username(), request.password()));
    }

    record RegisterRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(max = 100) String displayName,
        @NotBlank @Size(min = 8, max = 100) String password
    ) {}

    record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    record TokenResponse(String token) {}
}
