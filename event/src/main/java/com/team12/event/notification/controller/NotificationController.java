package com.team12.event.notification.controller;

import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.event.notification.entity.NotificationDTO;
import com.team12.event.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    //find all notification by toUserId
    @GetMapping("/{id}/")
    public ResponseEntity<List<NotificationDTO>> getNotificationsById(@PathVariable UUID id) {
        Optional<List<NotificationDTO>> notification = notificationService.getNotificationListById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(Collections.emptyList()));
    }

    //delete all notification by toUserId and type
    @DeleteMapping("/deleteNotification/{id}/{type}")
    public ResponseEntity<String> deleteNotification(@PathVariable UUID id, @PathVariable int type) {
        boolean deleted = notificationService.notificationDelete(id, type);

        if(deleted){
            return ResponseEntity.ok("Deleted notification successfully.");
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found or empty");
        }
    }


}
