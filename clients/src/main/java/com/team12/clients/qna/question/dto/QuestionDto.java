package com.team12.clients.qna.question.dto;

import java.util.UUID;

public record QuestionDto (
        UUID id,
        String title,
        String content,
        UUID ownerId

){

}