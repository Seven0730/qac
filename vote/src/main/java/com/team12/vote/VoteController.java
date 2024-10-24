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
        click upvote button
     */
    @PostMapping("/upvote/{postId}")
    public ResponseEntity<String> clickUpvote(@RequestBody VoteRequest voteRequest) {
        try {
            voteService.clickUpvote(voteRequest);
            return ResponseEntity.ok("Vote successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
        click downvote button
     */
    @PostMapping("/downvote/{postId}")
    public ResponseEntity<String> clickDownvote(@RequestBody VoteRequest voteRequest) {
        try {
            voteService.clickDownvote(voteRequest);
            return ResponseEntity.ok("Vote successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
        get the count of upvote of the post
     */
    @GetMapping("/upvote/{postId}/count")
    public ResponseEntity<Integer> getUpvoteCount(@PathVariable UUID postId) {
        int count = voteService.getVoteCountWithVoteValue(postId, 1);
        return ResponseEntity.ok(count);
    }

    /*
        get the count of downvote of the post
     */
    @GetMapping("/downvote/{postId}/count")
    public ResponseEntity<Integer> getDownvoteCount(@PathVariable UUID postId) {
        int count = voteService.getVoteCountWithVoteValue(postId, -1);
        return ResponseEntity.ok(count);
    }

    /*
        check if user has voted for the post
        1: upvote
        -1: downvote
        0: has not voted
    */
    @GetMapping("/has-voted/{postId}")
    public ResponseEntity<Integer> hasUserVoted(@RequestBody HasUserVotedRequest hasUserVotedRequest) {
        int hasVoted = voteService.hasUserVoted(hasUserVotedRequest);
        return ResponseEntity.ok(hasVoted);
    }
}
