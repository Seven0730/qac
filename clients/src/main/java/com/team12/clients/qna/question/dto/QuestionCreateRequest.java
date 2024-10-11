package com.team12.clients.qna.question.dto;

public record QuestionCreateRequest(
        String title,
        String content,
        String ownerId
) {
}
