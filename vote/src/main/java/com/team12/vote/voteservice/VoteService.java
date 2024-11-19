package com.team12.vote.voteservice;

import com.team12.clients.notification.NotificationClient;
import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.clients.notification.dto.NotificationType;
import com.team12.clients.vote.dto.HasUserVotedRequest;
import com.team12.clients.vote.dto.VoteRequest;
import com.team12.vote.PostType;
import com.team12.vote.Vote;
import com.team12.vote.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final NotificationClient notificationClient;

    public void handleVote(VoteRequest voteRequest, String voteType) {
        VoteStrategy strategy = getVoteStrategy(voteType);
        strategy.handleVote(voteRequest);
    }

    private VoteStrategy getVoteStrategy(String voteType) {
        return switch (voteType.toLowerCase()) {
            case "upvote" -> new UpvoteStrategy(voteRepository, notificationClient);
            case "downvote" -> new DownvoteStrategy(voteRepository);
            default -> throw new IllegalArgumentException("Invalid vote type: " + voteType);
        };
    }

    public void clickUpvote(VoteRequest voteRequest) {
        handleVote(voteRequest, "upvote");
    }

    public void clickDownvote(VoteRequest voteRequest) {
        handleVote(voteRequest, "downvote");
    }

    public void removeVote(UUID userId, UUID postId) {
        voteRepository.deleteByUserIdAndPostId(userId, postId);
    }

    public void removeVote(UUID postId) {voteRepository.deleteByPostId(postId);}

    public int[] getVoteCount(UUID postId) {
        int up = getVoteCountWithVoteValue(postId, 1);
        int down = getVoteCountWithVoteValue(postId, -1);
        return new int[]{up,down};
    }

    public int getVoteCountWithVoteValue(UUID postId, int voteValue) {
        return voteRepository.countByPostIdAndVoteValue(postId,voteValue);
    }

    public int hasUserVoted(HasUserVotedRequest hasUserVotedRequest) {
        UUID userId = hasUserVotedRequest.userId();
        UUID postId = hasUserVotedRequest.postId();

        Optional<Vote> existingVote = voteRepository.findByUserIdAndPostId(userId, postId);
        return existingVote.map(Vote::getVoteValue).orElse(0);
    }

    /*
        ------------------------------------- Private methods -------------------------------------
    */

    private Vote createVote(UUID userId, UUID postId, PostType postType, int voteValue) {
        Vote newVote = Vote.builder()
                .userId(userId)
                .postId(postId)
                .postType(postType)
                .voteValue(voteValue)
                .build();

        return voteRepository.save(newVote);
    }

    private void sendNotification(VoteRequest voteRequest) {
        NotificationRequest notificationRequest = new NotificationRequest(voteRequest.authorId(), "You receive a new upvote.", NotificationType.UPVOTE_RECEIVED);
        notificationClient.sendNotification(notificationRequest);
    }
}
