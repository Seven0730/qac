package com.team12.user.controller;

import com.team12.clients.user.dto.AuthRequest;
import com.team12.clients.user.dto.UserRegistrationRequest;
import com.team12.user.config.JwtUtil;
import com.team12.user.entity.Role;
import com.team12.user.entity.User;
import com.team12.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setPasswordHash("encodedPassword");
        user.setRole(Role.USER);

        when(userRepository.findByEmail(authRequest.email())).thenReturn(user);
        when(passwordEncoder.matches(authRequest.password(), user.getPasswordHash())).thenReturn(true);
        when(jwtUtil.createToken(String.valueOf(user.getId()), user.getRole())).thenReturn("jwtToken");

        ResponseEntity<Map<String, Object>> response = authController.login(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody().get("message"));
        assertEquals("jwtToken", response.getBody().get("token"));
    }

    @Test
    void testLoginFailure() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");

        when(userRepository.findByEmail(authRequest.email())).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = authController.login(authRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().get("message"));
    }

    @Test
    void testRegisterSuccess() {
        UserRegistrationRequest registerRequest = new UserRegistrationRequest("newuser", "newuser@example.com", "newpassword");

        when(userRepository.findByEmail(registerRequest.email())).thenReturn(null);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPassword");

        ResponseEntity<String> response = authController.register(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterFailure_UserAlreadyExists() {
        UserRegistrationRequest registerRequest = new UserRegistrationRequest("existinguser", "existinguser@example.com", "password");

        when(userRepository.findByEmail(registerRequest.email())).thenReturn(new User());

        ResponseEntity<String> response = authController.register(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void testValidateTokenSuccess() {
        String token = "Bearer validToken";
        when(jwtUtil.validateToken("validToken")).thenReturn(true);

        boolean isValid = authController.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateTokenFailure_InvalidFormat() {
        String token = "InvalidTokenFormat";

        boolean isValid = authController.validateToken(token);

        assertFalse(isValid);
    }

    @Test
    void testValidateTokenFailure_InvalidToken() {
        String token = "Bearer invalidToken";
        when(jwtUtil.validateToken("invalidToken")).thenReturn(false);

        boolean isValid = authController.validateToken(token);

        assertFalse(isValid);
    }
}
