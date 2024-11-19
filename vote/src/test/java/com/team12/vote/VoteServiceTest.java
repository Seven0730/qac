package com.team12.vote;

import com.team12.clients.notification.NotificationClient;
import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.clients.vote.dto.HasUserVotedRequest;
import com.team12.clients.vote.dto.PostTypeOfClients;
import com.team12.clients.vote.dto.VoteRequest;
import com.team12.vote.voteservice.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @MockBean
    private VoteRepository voteRepository;

    @MockBean
    private NotificationClient notificationClient;


    private UUID userId;
    private UUID postId;
    private PostTypeOfClients postTypeOfClients;
    private VoteRequest voteRequest;
    private HasUserVotedRequest hasUserVotedRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        postId = UUID.randomUUID();
        postTypeOfClients = PostTypeOfClients.QUESTION; // question / answer
        voteRequest = new VoteRequest(userId, postId, null, null, postTypeOfClients);
        hasUserVotedRequest = new HasUserVotedRequest(userId, postId);
        doNothing().when(notificationClient).sendNotification(any(NotificationRequest.class));
    }

    /*
        Test for clickUpvote()
    */

    // Test 1: No vote at the beginning, the upvote succeeded
    @Test
    void testClickUpvote_NoExistingVote_AddUpvote() {
        when(voteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.empty());

        voteService.clickUpvote(voteRequest);

        verify(voteRepository, times(1)).save(any(Vote.class)); // Verify that the new vote is saved
        verify(voteRepository, never()).deleteByUserIdAndPostId(any(), any()); // No records were deleted
    }

    // Test 2: Click the up button at first, then unclick the up button
    @Test
    void testClickUpvote_ExistingUpvote_RemoveUpvote() {
        Vote existingVote = Vote.builder()
                .userId(userId)
                .postId(postId)
                .postType(PostType.valueOf(postTypeOfClients.name()))
                .voteValue(1) // up
                .build();

        when(voteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.of(existingVote));

        voteService.clickUpvote(voteRequest);

        verify(voteRepository, times(1)).deleteByUserIdAndPostId(userId, postId); // Verify that the vote was deleted
        verify(voteRepository, never()).save(any()); // No new records are saved
    }

    // Test 3: At first, click on the down, then convert to up
    @Test
    void testClickUpvote_ExistingDownvote_TurnToUpvote() {
        Vote existingVote = Vote.builder()
                .userId(userId)
                .postId(postId)
                .postType(PostType.valueOf(postTypeOfClients.name()))
                .voteValue(-1) // down
                .build();

        when(voteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.of(existingVote));

        voteService.clickUpvote(voteRequest);

        assertEquals(1, existingVote.getVoteValue()); // Verify that the vote value is updated to 1 (up)
        verify(voteRepository, times(1)).save(existingVote); // Verify that the updated vote is saved
    }

    /*
        Test for clickDownvote()
    */



    /*
        Test for removeVote()
    */

    @Test
    void testRemoveVote() {
        voteService.removeVote(userId, postId);

        verify(voteRepository, times(1)).deleteByUserIdAndPostId(userId, postId);
    }

    /*
        Test for getVoteCountWithVoteValue()
    */

    @Test
    void testGetVoteCountWithVoteValue() {
        // Simulate that the number of returned votes is 10
        when(voteRepository.countByPostIdAndVoteValue(postId, 1)).thenReturn(10);

        int count = voteService.getVoteCountWithVoteValue(postId, 1);

        // Verify that the number of returned votes is 10
        assertEquals(10, count);
        verify(voteRepository, times(1)).countByPostIdAndVoteValue(postId, 1);
    }

    /*
        Test for hasUserVoted()
    */

    // User has voted
    @Test
    void testHasUserVoted_UserHasVoted() {
        Vote existingVote = Vote.builder()
                .userId(userId)
                .postId(postId)
                .voteValue(1)  // User has voted
                .build();

        when(voteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.of(existingVote));

        int voteValue = voteService.hasUserVoted(hasUserVotedRequest);

        // Verify that return value is 1
        assertEquals(1, voteValue);
        verify(voteRepository, times(1)).findByUserIdAndPostId(userId, postId);
    }

    // User has not voted
    @Test
    void testHasUserVoted_UserHasNotVoted() {
        when(voteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.empty());

        int voteValue = voteService.hasUserVoted(hasUserVotedRequest);

        // Verify that return value is 0
        assertEquals(0, voteValue);
        verify(voteRepository, times(1)).findByUserIdAndPostId(userId, postId);
    }
}
