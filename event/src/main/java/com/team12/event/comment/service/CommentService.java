package com.team12.event.comment.service;

import com.team12.clients.comment.dto.CommentModifyRequest;
import com.team12.clients.comment.dto.CommentSendRequest;
import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.clients.notification.NotificationClient;
import com.team12.clients.notification.dto.NotificationType;
import com.team12.clients.qna.QnAClient;
import com.team12.clients.qna.answer.dto.AnswerDto;
import com.team12.clients.user.UserClient;
import com.team12.event.comment.entity.Comment;
import com.team12.event.comment.entity.CommentDto;
import com.team12.event.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final NotificationClient notificationClient;
    private final UserClient userClient;
    private final QnAClient qnaClient;
    private final CommentRepository commentRepository;

    public CommentDto commentSend(CommentSendRequest request) {
        Comment comment = Comment.builder()
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .ownerId(request.ownerId())
                .answerId(request.answerId())
                .build();

        commentRepository.save(comment);

        AnswerDto answer = qnaClient.getAnswerById(request.answerId());
        notificationClient.sendNotification(new NotificationRequest(
                UUID.fromString(answer.ownerId()),
                request.content(),
                NotificationType.COMMENT_POSTED
        ));

        List<UUID> userIds = List.of(UUID.fromString(request.ownerId()));
        Map<UUID, String> userNames = userClient.getUsersByIds(userIds);

        return convertToDto(comment, userNames);
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

    public void commentDeleteByAnswerId(UUID answerId) {
        Optional<List<Comment>> optionalComments = commentRepository.findByAnswerId(answerId);
        if (optionalComments.isPresent() && !optionalComments.get().isEmpty()) {
            commentRepository.deleteAll(optionalComments.get());
        }
    }


    // Get All comments by its QuestionId
    public List<CommentDto> getCommentsByAnswerId(UUID id) {
        Optional<List<Comment>> optionalComments = commentRepository.findByAnswerId(id);
        List<Comment> comments = optionalComments.orElseGet(List::of);

        // Get All ownerId
        List<UUID> userIds = comments.stream()
                .map(comment -> UUID.fromString(comment.getOwnerId()))
                .distinct()
                .toList();

        //fetch all username from user Client
        Map<UUID, String> userNames = userClient.getUsersByIds(userIds);

        return comments.stream()
                .map(comment -> convertToDto(comment, userNames))
                .toList();
    }

    private CommentDto convertToDto(Comment comment, Map<UUID, String> userNames) {
        UUID ownerId = UUID.fromString(comment.getOwnerId());
        String userName = userNames.get(ownerId);
        return new CommentDto(comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getOwnerId(),
                comment.getAnswerId(),
                userName);
    }
}

