package ru.leyman.manugen.templates.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.leyman.manugen.templates.domain.entity.User;
import ru.leyman.manugen.templates.repo.UserRepo;

import java.util.Map;
import java.util.Optional;

/**
 * Simple authentication endpoint for development/internal use.
 * Does not require OAuth provider credentials.
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class SimpleAuthController {

    private final UserRepo userRepo;

    @Value("${app.simple-auth.enabled:false}")
    private boolean simpleAuthEnabled;

    /**
     * Simple login - creates or finds user by email.
     * Returns a basic token for API authentication.
     */
    @PostMapping("simple/login")
    public Map<String, Object> simpleLogin(@RequestBody Map<String, String> credentials) {
        if (!simpleAuthEnabled) {
            throw new RuntimeException("Simple authentication is disabled. Set app.simple-auth.enabled=true");
        }

        String login = credentials.get("login");
        String name = credentials.get("name");
        String email = credentials.get("email");

        if (login == null || email == null) {
            throw new IllegalArgumentException("Login and email are required");
        }

        // Find or create user
        User user = userRepo.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User(login, name != null ? name : login, email);
                    return userRepo.save(newUser);
                });

        // Return simple token (for development only - not secure for production)
        String token = "simple-token-" + user.getId();

        return Map.of(
                "id", String.valueOf(user.getId()),
                "email", user.getEmail(),
                "name", user.getName(),
                "login", user.getLogin(),
                "accessToken", token,
                "tokenType", "simple"
        );
    }

    /**
     * Demo login - quick login with predefined demo user.
     */
    @PostMapping("simple/demo")
    public Map<String, Object> demoLogin() {
        if (!simpleAuthEnabled) {
            throw new RuntimeException("Simple authentication is disabled. Set app.simple-auth.enabled=true");
        }

        User user = userRepo.findByEmail("demo@manugen.local")
                .orElseGet(() -> {
                    User demoUser = new User("demo", "Demo User", "demo@manugen.local");
                    return userRepo.save(demoUser);
                });

        String token = "simple-token-" + user.getId();

        return Map.of(
                "id", String.valueOf(user.getId()),
                "email", user.getEmail(),
                "name", user.getName(),
                "login", user.getLogin(),
                "accessToken", token,
                "tokenType", "demo"
        );
    }

}
