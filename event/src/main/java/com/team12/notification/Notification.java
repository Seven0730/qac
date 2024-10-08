package com.team12.notification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Notification {

    @Id
    private Integer notificationId;
    private Integer toUserId;
    private String toUserEmail;
    private String sender;
    private String message;
    private LocalDateTime sentAt;
    private NotificationType notificationType;

}
