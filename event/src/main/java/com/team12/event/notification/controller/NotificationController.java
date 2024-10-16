package com.team12.event.notification.controller;

import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.event.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/notification")
@AllArgsConstructor
public class NotificationController {

    private NotificationService notificationService;

    @PostMapping
    public void sendNotification(@RequestBody NotificationRequest notificationRequest) {

        log.info("New notification... {}", notificationRequest);
        notificationService.sendNotification(notificationRequest);

    }
}
