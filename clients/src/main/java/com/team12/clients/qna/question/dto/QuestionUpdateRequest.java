package com.team12.clients.qna.question.dto;

public record QuestionUpdateRequest(
        String title,
        String content,
        String ownerId,
        String questionId
) {
}
