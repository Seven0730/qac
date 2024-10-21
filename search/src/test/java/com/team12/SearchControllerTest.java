package com.team12;

import com.team12.clients.qna.question.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    private List<QuestionDto> mockQuestions;

    @BeforeEach
    public void setUp() {
        mockQuestions = Arrays.asList(
                new QuestionDto(UUID.randomUUID(), "First Question", "This is the first question content", UUID.randomUUID()),
                new QuestionDto(UUID.randomUUID(), "Second Question", "This is the second question content", UUID.randomUUID())
        );
    }

    @Test
    public void testSearchQuestions() throws Exception {
        // Mock searchService to return mockQuestions when searchQuestionsByKeyword is called
        Mockito.when(searchService.searchQuestionsByKeyword(anyString())).thenReturn(mockQuestions);

        // Perform a GET request to /search/questions with a keyword parameter
        mockMvc.perform(get("/search/questions")
                        .param("keyword", "question")
                        .contentType(MediaType.APPLICATION_JSON))
                // Expect HTTP 200 OK status
                .andExpect(status().isOk())
                // Verify that the response is JSON and contains the expected data
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("First Question"))
                .andExpect(jsonPath("$[0].content").value("This is the first question content"))
                .andExpect(jsonPath("$[1].title").value("Second Question"))
                .andExpect(jsonPath("$[1].content").value("This is the second question content"));
    }
}
