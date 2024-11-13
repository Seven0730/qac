package com.team12.vote;

import com.team12.clients.vote.dto.HasUserVotedRequest;
import com.team12.clients.vote.dto.VoteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/vote")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    /*
        Click upvote button
     */
    @PostMapping("/upvote")
    public ResponseEntity<String> clickUpvote(@RequestBody VoteRequest voteRequest) {
        try {
            voteService.clickUpvote(voteRequest);
            return ResponseEntity.ok("Vote successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
        Click downvote button
     */
    @PostMapping("/downvote")
    public ResponseEntity<String> clickDownvote(@RequestBody VoteRequest voteRequest) {
        try {
            voteService.clickDownvote(voteRequest);
            return ResponseEntity.ok("Vote successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
        Get the count of upvote of the post
     */
    @GetMapping("/upvote/{postId}/count")
    public ResponseEntity<Integer> getUpvoteCount(@PathVariable UUID postId) {
        int count = voteService.getVoteCountWithVoteValue(postId, 1);
        return ResponseEntity.ok(count);
    }

    /*
        Get the count of downvote of the post
     */
    @GetMapping("/downvote/{postId}/count")
    public ResponseEntity<Integer> getDownvoteCount(@PathVariable UUID postId) {
        int count = voteService.getVoteCountWithVoteValue(postId, -1);
        return ResponseEntity.ok(count);
    }

    /*
        Check if user has voted for the post
        1: upvote
        -1: downvote
        0: has not voted
    */
    @GetMapping("/has-voted")
    public ResponseEntity<Integer> hasUserVoted(@RequestBody HasUserVotedRequest hasUserVotedRequest) {
        int hasVoted = voteService.hasUserVoted(hasUserVotedRequest);
        return ResponseEntity.ok(hasVoted);
    }

    /*
        Delete all the votes of the postId
     */
    @DeleteMapping("/delete-vote/{postId}")
    public ResponseEntity<String> deleteVote(@PathVariable UUID postId) {
        try {
            voteService.removeVote(postId);
            return ResponseEntity.ok("Delete vote successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
