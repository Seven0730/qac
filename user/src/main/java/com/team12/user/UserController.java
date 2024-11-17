package com.team12.user;

import com.team12.clients.user.dto.AuthRequest;
import com.team12.clients.user.dto.UserRegistrationRequest;
import com.team12.user.config.JwtUtil;
import com.team12.user.entity.Role;
import com.team12.user.entity.User;
import com.team12.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("api/v1/user/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest authRequest) {

        User user = userRepository.findByEmail(authRequest.email());

        if (user == null || !passwordEncoder.matches(authRequest.password(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "status", HttpStatus.UNAUTHORIZED.value(),
                            "message", "Invalid credentials",
                            "timestamp", System.currentTimeMillis()
                    )
            );
        }

        String token = jwtUtil.createToken(String.valueOf(user.getId()), user.getRole());

        return ResponseEntity.ok(
                Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "Login successful",
                        "token", token,
                        "timestamp", System.currentTimeMillis()
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest registerRequest) {

        if (userRepository.findByEmail(registerRequest.email()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.password());

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPasswordHash(encodedPassword);
        user.setRole(Role.USER);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);
            return jwtUtil.validateToken(actualToken);
        }
        return false;
    }
}
