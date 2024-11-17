package com.team12.vote;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends MongoRepository<Vote, UUID> {
    Optional<Vote> findByUserIdAndPostId(UUID userId, UUID postId);
    int countByPostIdAndVoteValue(UUID postId, int voteValue);
    void deleteByUserIdAndPostId(UUID userId, UUID postId);
}
