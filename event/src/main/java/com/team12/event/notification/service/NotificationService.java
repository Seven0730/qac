package com.team12.event.notification.service;

import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.event.notification.entity.Notification;
import com.team12.event.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AmqpTemplate amqpTemplate;


    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void sendNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .toUserId(request.toUserId())
                .message(request.message())
                .toUserEmail(request.toUserEmail())
                .sender("notification-service")
                .sentAt(LocalDateTime.now())
                .build();

        executorService.submit(() -> {
            notificationRepository.save(notification);
            amqpTemplate.convertAndSend("notification.exchange", "notification.routing.key", notification);

            log.info("Notification sent and saved to database for user: {}", request.toUserId());
        });
    }
}
