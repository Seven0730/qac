package com.team12.event.notification.service;

import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.clients.notification.dto.NotificationType;
import com.team12.event.notification.entity.Notification;
import com.team12.event.notification.entity.NotificationDTO;
import com.team12.event.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
                .sender("notification-service")
                .sentAt(LocalDateTime.now())
                .notificationType(request.type())
                .build();

        executorService.submit(() -> {
            notificationRepository.save(notification);
            amqpTemplate.convertAndSend("notification.exchange", "notification.routing.key", notification);

            log.info("Notification sent and saved to database for user: {}", request.toUserId());
        });
    }


    public Optional<List<NotificationDTO>> getNotificationListById(UUID id) {
        List<Notification> notifications = notificationRepository.findAllByToUserId(id);

        if (notifications.isEmpty()) {
            return Optional.empty();
        }

        List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(this::convertToDTO)
                .toList();

        return Optional.of(notificationDTOs);
    }

    @Transactional
    public boolean notificationDelete(UUID id, int type) {
        try {
            NotificationType notificationType;

            switch (type) {
                case 0 -> {
                    notificationType = NotificationType.ANSWER_POSTED;
                    notificationRepository.deleteBytoUserIdAndNotificationType(id, notificationType);
                }
                case 1 -> {
                    notificationType = NotificationType.UPVOTE_RECEIVED;
                    notificationRepository.deleteBytoUserIdAndNotificationType(id, notificationType);
                    notificationType = NotificationType.DOWNVOTE_RECEIVED;
                    notificationRepository.deleteBytoUserIdAndNotificationType(id, notificationType);
                }
                case 2 -> {
                    notificationType = NotificationType.COMMENT_POSTED;
                    notificationRepository.deleteBytoUserIdAndNotificationType(id, notificationType);
                }
                case 4 -> {
                    notificationType = NotificationType.OTHER;
                    notificationRepository.deleteBytoUserIdAndNotificationType(id, notificationType);
                }
                default -> throw new IllegalArgumentException("Unexpected type: " + type);
            }

            log.info("Notification with userId: {} and type: {} has been deleted.", id, notificationType);
            return true;
        } catch (Exception e) {
            log.error("Error occurred while deleting notification: {}", e.getMessage());
            return false;
        }
    }



    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getMessage(),
                notification.getSentAt(),
                notification.getNotificationType()
        );
    }
}
