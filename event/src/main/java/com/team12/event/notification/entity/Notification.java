package com.team12.event.notification.entity;

import com.team12.clients.notification.dto.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID toUserId;
    private String sender;
    private String message;
    private LocalDateTime sentAt;
    private NotificationType notificationType;

}
