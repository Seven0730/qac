package com.team12.vote.voteservice;

import com.team12.clients.vote.dto.VoteRequest;
import com.team12.vote.PostType;
import com.team12.vote.Vote;
import com.team12.vote.VoteRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class DownvoteStrategy implements VoteStrategy {
    private final VoteRepository voteRepository;

    @Override
    public void handleVote(VoteRequest voteRequest) {
        UUID userId = voteRequest.userId();
        UUID postId = voteRequest.postId();
        PostType postType = PostType.valueOf(voteRequest.postType().name());

        Optional<Vote> existingVote = voteRepository.findByUserIdAndPostId(userId, postId);
        if (existingVote.isEmpty()) {
            // User has not voted, add downvote
            createVote(userId, postId, postType, -1);
        } else {
            Vote vote = existingVote.get();
            if (vote.getVoteValue() == -1) {
                // Cancel downvote
                voteRepository.deleteByUserIdAndPostId(userId, postId);
            } else {
                // Change upvote to downvote
                vote.setVoteValue(-1);
                voteRepository.save(vote);
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
}

