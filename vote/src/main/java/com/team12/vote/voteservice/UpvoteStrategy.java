package com.team12.vote.voteservice;

import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.clients.notification.dto.NotificationType;
import com.team12.clients.vote.dto.VoteRequest;
import com.team12.clients.notification.NotificationClient;
import com.team12.vote.PostType;
import com.team12.vote.Vote;
import com.team12.vote.VoteRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class UpvoteStrategy implements VoteStrategy {
    private final VoteRepository voteRepository;
    private final NotificationClient notificationClient;

    @Override
    public void handleVote(VoteRequest voteRequest) {
        UUID userId = voteRequest.userId();
        UUID postId = voteRequest.postId();
        PostType postType = PostType.valueOf(voteRequest.postType().name());

        Optional<Vote> existingVote = voteRepository.findByUserIdAndPostId(userId, postId);
        if (existingVote.isEmpty()) {
            // User has not voted, add upvote
            createVote(userId, postId, postType, 1);
            sendNotification(voteRequest);
        } else {
            Vote vote = existingVote.get();
            if (vote.getVoteValue() == 1) {
                // Cancel upvote
                voteRepository.deleteByUserIdAndPostId(userId, postId);
            } else {
                // Change downvote to upvote
                vote.setVoteValue(1);
                voteRepository.save(vote);
                sendNotification(voteRequest);
            }
        }
    }

    private void createVote(UUID userId, UUID postId, PostType postType, int voteValue) {
        Vote newVote = Vote.builder()
                .userId(userId)
                .postId(postId)
                .postType(postType)
                .voteValue(voteValue)
                .build();
        voteRepository.save(newVote);
    }

    private void sendNotification(VoteRequest voteRequest) {
        NotificationRequest notificationRequest = new NotificationRequest(
                voteRequest.authorId(),
                "You received a new upvote.",
                NotificationType.UPVOTE_RECEIVED
        );
        notificationClient.sendNotification(notificationRequest);
    }
}
