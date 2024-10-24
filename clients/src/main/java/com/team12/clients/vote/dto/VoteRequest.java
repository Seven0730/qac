package com.team12.clients.vote.dto;

import java.util.UUID;

public record VoteRequest(
        UUID userId,
        UUID postId,
        UUID authorId,
        String authorEmail,
        PostTypeOfClients postType
) {
}
