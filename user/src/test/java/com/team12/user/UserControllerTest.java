package com.team12.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.team12.clients.user.dto.UserLoginRequest;
import com.team12.clients.user.dto.UserRegistrationRequest;
import com.team12.user.controller.AuthController;
import com.team12.user.controller.UserController;
import com.team12.user.service.AuthService;
import com.team12.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void registerAuth_successfulRegistration() {
        UserRegistrationRequest request = new UserRegistrationRequest("Username", "password", "email");
        doNothing().when(authService).registerUser(request);

        authController.registerUser(request);

        verify(authService, times(1)).registerUser(request);
    }

    @Test
    void loginAuth_successfulLogin() {
        UserLoginRequest request = new UserLoginRequest("Username", "password");
        when(authService.authenticateUser(request)).thenReturn(true);

        ResponseEntity<String> response = authController.loginUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }

    @Test
    void loginAuth_failedLogin() {
        UserLoginRequest request = new UserLoginRequest("username", "1wrong_password");
        when(authService.authenticateUser(request)).thenReturn(false);

        ResponseEntity<String> response = authController.loginUser(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Login failed", response.getBody());
    }
}
