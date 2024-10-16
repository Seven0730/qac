package com.team12.clients.comment.dto;

import java.util.UUID;

public record CommentSendRequest(
        String content,
        String ownerId,
        UUID answerId
) {
}
