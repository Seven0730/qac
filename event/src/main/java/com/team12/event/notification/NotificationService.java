package com.team12.event.notification;

import com.team12.clients.notification.dto.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void sendNotification(NotificationRequest request) {
        notificationRepository.save(Notification.builder()
                .toUserId(request.toUserId())
                .message(request.message())
                .toUserEmail(request.toUserEmail())
                .sender("notification-service")
                .sentAt(LocalDateTime.now())
                .build());
    }

}
