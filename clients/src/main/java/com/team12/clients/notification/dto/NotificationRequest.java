package com.team12.clients.notification.dto;

public record NotificationRequest(
        Integer toUserId,
        String message,
        String toUserEmail
){
}
