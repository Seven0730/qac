package com.team12;

import com.team12.clients.qna.QnAClient;
import com.team12.clients.qna.question.dto.QuestionDto;
import com.team12.clients.user.UserClient;
import com.team12.clients.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    @Mock
    private QnAClient qnaClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchQuestionsByKeyword_ShouldReturnQuestionList() {

        String keyword = "test";
        UUID questionId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        QuestionDto questionDto = new QuestionDto(questionId, "Test Title", "Test Content", ownerId);
        when(qnaClient.searchQuestions(keyword)).thenReturn(List.of(questionDto));

        List<QuestionDto> result = searchService.searchQuestionsByKeyword(keyword);

        assertEquals(1, result.size());
        assertEquals(questionDto, result.get(0));
    }

    @Test
    void searchUsersByUsername_ShouldReturnUserList() {

        String keyword = "user";
        UUID userId = UUID.randomUUID();
        UserDto userDto = new UserDto(userId, "username", "user@example.com");
        when(userClient.searchUsersByUsername(keyword)).thenReturn(List.of(userDto));

        List<UserDto> result = searchService.searchUsersByUsername(keyword);

        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
    }
}
