package com.team12.clients.qna.question.dto;

import java.util.UUID;

public record QuestionUpdateRequest(
        String title,
        String content,
        UUID ownerId,
        UUID questionId
) {
}
