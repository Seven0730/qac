package com.team12.clients.vote;

import com.team12.clients.vote.dto.HasUserVotedRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vote", path = "api/v1/vote")
public interface VoteClient {
    @GetMapping("/has-voted/{postId}")
    public ResponseEntity<Integer> hasUserVoted(HasUserVotedRequest hasUserVotedRequest);

    @GetMapping("/upvote/{postId}/count")
    public ResponseEntity<Integer> getUpvoteCount(@PathVariable UUID postId);

    @GetMapping("/downvote/{postId}/count")
    public ResponseEntity<Integer> getDownvoteCount(@PathVariable UUID postId);
}
