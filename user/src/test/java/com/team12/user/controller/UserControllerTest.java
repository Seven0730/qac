package com.team12.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.user.entity.Role;
import com.team12.user.entity.User;
import com.team12.user.service.UserService;
import com.team12.clients.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        userId = UUID.randomUUID();
        user = User.builder()
                .username("testuser")
                .email("testuser@example.com")
                .role(Role.USER)
                .build();
    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users = Arrays.asList(user);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("testuser@example.com"));
    }

    @Test
    void getUserById_UserFound() throws Exception {
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/v1/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void getUserById_UserNotFound() throws Exception {
        when(userService.getUserById(userId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/user/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void updateUser_UserFound() throws Exception {
        when(userService.updateUser(any(UUID.class), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/v1/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void updateUser_UserNotFound() throws Exception {
        when(userService.updateUser(any(UUID.class), any(User.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_UserFound() throws Exception {
        when(userService.deleteUser(userId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/user/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_UserNotFound() throws Exception {
        when(userService.deleteUser(userId)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/user/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchUsersByUsername() throws Exception {
        UserDto userDto = new UserDto(userId, "testuser", "testuser@example.com");
        List<UserDto> userDtos = Arrays.asList(userDto);

        when(userService.searchUsersByUsername("test")).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/v1/user/search")
                        .param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("testuser@example.com"));
    }

    @Test
    void getUserProfileById() throws Exception {
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/v1/user/profile/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void getUserProfileById_UserNotFound() throws Exception {
        when(userService.getUserById(userId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/user/profile/{id}", userId))
                .andExpect(status().isNotFound());
    }
}
