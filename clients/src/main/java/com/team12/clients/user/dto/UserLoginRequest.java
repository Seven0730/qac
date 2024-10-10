package com.team12.clients.user.dto;

public record UserLoginRequest(
        String username,
        String password
) {
}
