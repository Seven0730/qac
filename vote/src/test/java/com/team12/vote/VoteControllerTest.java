package com.team12.vote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team12.clients.vote.dto.HasUserVotedRequest;
import com.team12.clients.vote.dto.PostTypeOfClients;
import com.team12.clients.vote.dto.VoteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VoteController.class)
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VoteService voteService;

    /*
        Test for clickUpvote()
    */
    @Test
    void testClickUpvote() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        PostTypeOfClients postTypeOfClients = PostTypeOfClients.QUESTION;
        VoteRequest voteRequest = new VoteRequest(userId, postId, null, null, postTypeOfClients);

        doNothing().when(voteService).clickUpvote(voteRequest);

        mockMvc.perform(post("/api/v1/vote/upvote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequest)))
                .andExpect(status().isOk())  // The expected HTTP status code is 200 OK
                .andExpect(content().string("Vote successfully."));  // The content of the response expected to be returned
    }

    /*
        Test for clickDownvote()
    */
    @Test
    void testClickDownvote() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        PostTypeOfClients postTypeOfClients = PostTypeOfClients.QUESTION;
        VoteRequest voteRequest = new VoteRequest(userId, postId, null, null, postTypeOfClients);

        doNothing().when(voteService).clickDownvote(voteRequest);

        mockMvc.perform(post("/api/v1/vote/downvote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequest)))
                .andExpect(status().isOk())  // The expected HTTP status code is 200 OK
                .andExpect(content().string("Vote successfully."));  // The content of the response expected to be returned
    }

    /*
        Test for getUpvoteCount()
    */
    @Test
    void testGetUpvoteCount() throws Exception {
        UUID postId = UUID.randomUUID();

        // The simulated service layer returns the number of upvote
        when(voteService.getVoteCountWithVoteValue(postId, 1)).thenReturn(10);

        // Simulate an HTTP GET request
        mockMvc.perform(get("/api/v1/vote/upvote/{postId}/count", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // The expected HTTP status code is 200 OK
                .andExpect(content().string("10"));  // The count of upvote expected to be returned
    }

    /*
        Test for getDownvoteCount()
    */
    @Test
    void testGetDownvoteCount() throws Exception {
        UUID postId = UUID.randomUUID();

        // The simulated service layer returns the number of upvote
        when(voteService.getVoteCountWithVoteValue(postId, -1)).thenReturn(10);

        // Simulate an HTTP GET request
        mockMvc.perform(get("/api/v1/vote/downvote/{postId}/count", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // The expected HTTP status code is 200 OK
                .andExpect(content().string("10"));  // The count of upvote expected to be returned
    }

    /*
        Test for hasUserVoted()
    */
    @Test
    void testHasUserVoted_Upvote() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        HasUserVotedRequest hasUserVotedRequest = new HasUserVotedRequest(userId, postId);

        // Simulate that the user click upvote (1: upvote)
        when(voteService.hasUserVoted(hasUserVotedRequest)).thenReturn(1);

        // Execute the request and verify the response
        mockMvc.perform(get("/api/v1/vote/has-voted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hasUserVotedRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));  // Verify that the return value is 1
    }

    @Test
    void testHasUserVoted_Downvote() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        HasUserVotedRequest hasUserVotedRequest = new HasUserVotedRequest(userId, postId);

        // Simulate that the user click downvote (-1: downvote)
        when(voteService.hasUserVoted(hasUserVotedRequest)).thenReturn(-1);

        // Execute the request and verify the response
        mockMvc.perform(get("/api/v1/vote/has-voted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hasUserVotedRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("-1"));  // Verify that the return value is -1
    }

    @Test
    void testHasUserVoted_NoVote() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        HasUserVotedRequest hasUserVotedRequest = new HasUserVotedRequest(userId, postId);

        // Simulate that the user do not click (0: no vote)
        when(voteService.hasUserVoted(hasUserVotedRequest)).thenReturn(0);

        // Execute the request and verify the response
        mockMvc.perform(get("/api/v1/vote/has-voted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hasUserVotedRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));  // Verify that the return value is 0
    }
}
