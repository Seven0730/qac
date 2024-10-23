package com.team12.event.notification.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.event.notification.entity.Notification;
import com.team12.event.notification.entity.NotificationDTO;
import com.team12.event.notification.websocket.MessagingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class RabbitMQListener {

    private final MessagingService messagingService;
    private final ObjectMapper objectMapper; // 注入 ObjectMapper

    @RabbitListener(queues = "notification.queue")
    public void receiveMessage(Notification notification) {
        try {
            NotificationDTO notificationDTO = new NotificationDTO(
                    notification.getMessage(),
                    notification.getSentAt(),
                    notification.getNotificationType()
            );

            String notificationJson = objectMapper.writeValueAsString(notificationDTO);

            messagingService.sendMessageToUser(notification.getToUserId(), notificationJson);

            log.info("Forwarded notification to user: {} with message: {}", notification.getToUserId(), notificationJson);
        } catch (JsonProcessingException e) {
            log.error("Error while converting notification to JSON", e);
        }
    }
}