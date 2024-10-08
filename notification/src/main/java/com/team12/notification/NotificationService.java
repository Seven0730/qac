package com.team12.notification;

import com.team12.clients.notification.dto.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest) {
        notificationRepository.save(
                Notification.builder()
                        .toUserId(notificationRequest.toUserId())
                        .message(notificationRequest.message())
                        .toUserEmail(notificationRequest.toUserEmail())
                        .sender("notification-service")
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}
