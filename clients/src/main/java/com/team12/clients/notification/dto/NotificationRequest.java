package com.team12.clients.notification.dto;

import java.util.UUID;

public record NotificationRequest(
        UUID toUserId,
        String message,
        String toUserEmail
){
}
