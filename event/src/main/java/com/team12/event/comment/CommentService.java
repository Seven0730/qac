package com.team12.event.comment;


import com.team12.clients.comment.dto.CommentModifyRequest;
import com.team12.clients.comment.dto.CommentSendRequest;
import com.team12.clients.notification.NotificationClient;
import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.event.notification.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final NotificationClient notificationClient;
    private final CommentRepository commentRepository;

    public void commentSend(CommentSendRequest request) {
        Comment comment = Comment.builder()
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .ownerId(request.ownerId())
                .answerId(request.answerId())
                .build();

        commentRepository.save(comment);

        // 根据答案 ID 搜索用户
        /* notificationClient.sendNotification(new NotificationRequest(
        )); */
    }

    @Transactional
    public boolean commentModify(UUID commentId, CommentModifyRequest request) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setContent(request.content());
                    commentRepository.save(comment);
                    return true;
                })
                .orElse(false);
    }


    public boolean commentDelete(UUID commentId) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    commentRepository.delete(comment);
                    return true;
                })
                .orElse(false);
    }
}

