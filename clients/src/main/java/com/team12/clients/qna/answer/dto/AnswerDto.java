package com.team12.clients.qna.answer.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnswerDto(
        UUID id,
        String content,
        LocalDateTime createdAt,
        String ownerId,
        String questionId
) {
}
