package com.team12.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;

    @Transactional
    public void clickUpvote(UUID userId, UUID postId, PostType postType) {
        Optional<Vote> existingVote = voteRepository.findByUserIdAndPostId(userId, postId);
        if (existingVote.isEmpty()) {
            // user has not voted, add upvote
            createVote(userId, postId, postType, 1); // 1 for upvote
            log.info("User {} upvote post {}", userId, postId);
        } else {
            // user has voted, check if it is up or down
            Vote vote = existingVote.get();
            if (vote.getVoteValue() == 1) {
                // existingVote is up, cancel it
                removeVote(userId, postId);
                log.info("User {} remove upvote from post {}", userId, postId);
            } else {
                // existingVote is down, turn down into up
                vote.setVoteValue(1);
                voteRepository.save(vote);
                log.info("User {} turn downvote into upvote from post {}", userId, postId);
            }
        }
    }

    @Transactional
    public void clickDownvote(UUID userId, UUID postId, PostType postType) {
        Optional<Vote> existingVote = voteRepository.findByUserIdAndPostId(userId, postId);
        if (existingVote.isEmpty()) {
            // user has not voted, add downvote
            createVote(userId, postId, postType, -1); // -1 for downvote
            log.info("User {} downvote post {}", userId, postId);
        } else {
            // user has voted, check if it is up or down
            Vote vote = existingVote.get();
            if (vote.getVoteValue() == -1) {
                // existingVote is down, cancel it
                removeVote(userId, postId);
                log.info("User {} remove downvote from post {}", userId, postId);
            } else {
                // existingVote is up, turn down into down
                vote.setVoteValue(-1);
                voteRepository.save(vote);
                log.info("User {} turn upvote into downvote from post {}", userId, postId);
            }
        }
    }

    @Transactional
    public void removeVote(UUID userId, UUID postId) {
        voteRepository.deleteByUserIdAndPostId(userId, postId);
    }

    public int[] getVoteCount(UUID postId) {
        int up = getVoteCountWithVoteValue(postId, 1);
        int down = getVoteCountWithVoteValue(postId, -1);
        return new int[]{up,down};
    }

    public int getVoteCountWithVoteValue(UUID postId, int voteValue) {
        return voteRepository.countByPostIdAndVoteValue(postId,voteValue);
    }

    public int hasUserVoted(UUID userId, UUID postId) {
        Optional<Vote> existingVote = voteRepository.findByUserIdAndPostId(userId, postId);
        return existingVote.map(Vote::getVoteValue).orElse(0);
        /*
        equal to codes as follows:
        if (existingVote.isEmpty()) {
            return 0;
        } else {
            return existingVote.get().getVoteValue();
        }
        */
    }

    /*
        ------------------------------------- private methods -------------------------------------
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
}
