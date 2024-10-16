package com.team12.user.controller;

import com.team12.clients.user.dto.UserLoginRequest;
import com.team12.clients.user.dto.UserRegistrationRequest;
import com.team12.user.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
public class AuthController{

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        log.info("Registering user: {}", userRegistrationRequest);
        authService.registerUser(userRegistrationRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        log.info("Logging in user: {}", userLoginRequest);
        boolean isAuthenticated = authService.authenticateUser(userLoginRequest);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

}
