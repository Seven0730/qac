package com.team12.user.service;


import com.team12.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.team12.user.repository.UserRepository;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Map<UUID, String> getUserNamesByIds(List<UUID> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return users.stream().collect(Collectors.toMap(User::getId, User::getUsername));
    }
    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }
}
