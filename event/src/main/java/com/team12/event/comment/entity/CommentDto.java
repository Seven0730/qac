package com.team12.event.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private UUID id;

    private String content;

    private LocalDateTime createdAt;

    private String ownerId;

    private UUID answerId;

    private String username;
}
