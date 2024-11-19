package com.team12.user.service;

import com.team12.user.entity.User;
import com.team12.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private UUID sampleUserId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUserId = UUID.randomUUID();
        sampleUser = new User();
        sampleUser.setId(sampleUserId);
        sampleUser.setUsername("testUser");
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(sampleUser));
        User user = userService.getUserById(sampleUserId);
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        verify(userRepository, times(1)).findById(sampleUserId);
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.empty());
        User user = userService.getUserById(sampleUserId);
        assertNull(user);
        verify(userRepository, times(1)).findById(sampleUserId);
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        User createdUser = userService.createUser(sampleUser);
        assertNotNull(createdUser);
        assertEquals(sampleUserId, createdUser.getId());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testUpdateUser_UserExists() {
        when(userRepository.existsById(sampleUserId)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User updatedUser = userService.updateUser(sampleUserId, sampleUser);
        assertNotNull(updatedUser);
        assertEquals("testUser", updatedUser.getUsername());
        verify(userRepository, times(1)).existsById(sampleUserId);
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        when(userRepository.existsById(sampleUserId)).thenReturn(false);
        User updatedUser = userService.updateUser(sampleUserId, sampleUser);
        assertNull(updatedUser);
        verify(userRepository, times(1)).existsById(sampleUserId);
        verify(userRepository, times(0)).save(sampleUser);
    }

    @Test
    void testDeleteUser_UserExists() {
        when(userRepository.existsById(sampleUserId)).thenReturn(true);
        boolean isDeleted = userService.deleteUser(sampleUserId);
        assertTrue(isDeleted);
        verify(userRepository, times(1)).existsById(sampleUserId);
        verify(userRepository, times(1)).deleteById(sampleUserId);
    }

    @Test
    void testDeleteUser_UserDoesNotExist() {
        when(userRepository.existsById(sampleUserId)).thenReturn(false);
        boolean isDeleted = userService.deleteUser(sampleUserId);
        assertFalse(isDeleted);
        verify(userRepository, times(1)).existsById(sampleUserId);
        verify(userRepository, times(0)).deleteById(sampleUserId);
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = Arrays.asList(sampleUser, new User());
        when(userRepository.findAll()).thenReturn(userList);
        Iterable<User> allUsers = userService.getAllUsers();
        assertNotNull(allUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserNamesByIds() {
        List<User> users = List.of(sampleUser);
        List<UUID> userIds = List.of(sampleUserId);

        when(userRepository.findAllById(userIds)).thenReturn(users);

        Map<UUID, String> result = userService.getUserNamesByIds(userIds);

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(sampleUserId));

        verify(userRepository, times(1)).findAllById(userIds);
    }

    @Test
    void testSearchUsersByUsername() {
        String username = "test";
        List<User> users = List.of(sampleUser);

        when(userRepository.findByUsernameContaining(username)).thenReturn(users);

        List<User> result = userService.searchUsersByUsername(username);

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());

        verify(userRepository, times(1)).findByUsernameContaining(username);
    }
}

