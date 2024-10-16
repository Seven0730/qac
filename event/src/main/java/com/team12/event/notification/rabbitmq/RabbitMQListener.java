package com.team12.event.notification.rabbitmq;

import com.team12.event.notification.entity.Notification;
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

    @RabbitListener(queues = "notification.queue")
    public void receiveMessage(Notification notification) {
        //Listen rabbitmq queue and send notifi to frontend
        messagingService.sendMessageToUser(notification.getToUserId(), notification.getMessage());

        log.info("Forwarded notification to user: {} with message: {}", notification.getToUserId(), notification.getMessage());
    }

}
