package com.team12.user.service;

import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.clients.notification.NotificationClient;

import com.team12.clients.user.dto.UserLoginRequest;
import com.team12.clients.user.dto.UserRegistrationRequest;
import com.team12.user.entity.User;
import com.team12.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class  UserService {

    private final NotificationClient notificationClient;
    private final UserRepository userRepository;

    public void registerUser(UserRegistrationRequest request) {
        User user = User.builder()
                .username(request.username())
                .passwordHash(request.password())
                .email(request.email())
                .build();

        userRepository.save(user);

        notificationClient.sendNotification(new NotificationRequest(
                user.getId(),
                "User registered",
                request.email()
        ));
    }

    public boolean authenticateUser(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByUsername(userLoginRequest.username());
        return user != null && user.getPasswordHash().equals(userLoginRequest.password());
    }
}
