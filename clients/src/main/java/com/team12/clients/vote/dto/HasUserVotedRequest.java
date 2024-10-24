package com.team12.clients.vote.dto;

import java.util.UUID;

public record HasUserVotedRequest(
        UUID userId,
        UUID postId
) {
}
