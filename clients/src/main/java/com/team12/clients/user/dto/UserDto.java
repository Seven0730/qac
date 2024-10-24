package com.team12.clients.user.dto;

import java.util.UUID;

public record UserDto(UUID id, String username, String email) {
}
