package com.team12.clients.qna.question.dto;

import java.util.UUID;

public record QuestionCreateRequest(
        String title,
        String content,
        UUID ownerId
) {
}
