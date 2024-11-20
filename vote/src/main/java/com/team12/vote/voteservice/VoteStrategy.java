package com.team12.vote.voteservice;

import com.team12.clients.vote.dto.VoteRequest;

public interface VoteStrategy {
    void handleVote(VoteRequest voteRequest);
}


