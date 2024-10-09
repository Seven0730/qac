package com.team12.user;

import com.team12.clients.auth.AuthClient;
import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.clients.notification.NotificationClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class  UserService {

    private final AuthClient authClient;
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
}
