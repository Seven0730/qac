package com.team12.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.team12.clients.user.dto.UserLoginRequest;
import com.team12.clients.user.dto.UserRegistrationRequest;
import com.team12.user.controller.UserController;
import com.team12.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void registerUser_successfulRegistration() {
        UserRegistrationRequest request = new UserRegistrationRequest("username", "password", "email");
        doNothing().when(userService).registerUser(request);

        userController.registerUser(request);

        verify(userService, times(1)).registerUser(request);
    }

    @Test
    void loginUser_successfulLogin() {
        UserLoginRequest request = new UserLoginRequest("username", "password");
        when(userService.authenticateUser(request)).thenReturn(true);

        ResponseEntity<String> response = userController.loginUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }

    @Test
    void loginUser_failedLogin() {
        UserLoginRequest request = new UserLoginRequest("username", "1wrong_password");
        when(userService.authenticateUser(request)).thenReturn(false);

        ResponseEntity<String> response = userController.loginUser(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Login failed", response.getBody());
    }
}
