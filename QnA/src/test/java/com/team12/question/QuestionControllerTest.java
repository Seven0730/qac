package com.team12.question;

import com.team12.clients.qna.question.dto.QuestionCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllQuestions_returnsListOfQuestions() {

        List<Question> questions = List.of(new Question(UUID.randomUUID(), "Title 1", "Content 1", null, UUID.randomUUID()));
        when(questionService.getAllQuestions()).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionController.getAllQuestions();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getQuestionById_returnsQuestion_whenFound() {

        UUID questionId = UUID.randomUUID();
        Question question = new Question(questionId, "Title", "Content", null, UUID.randomUUID());
        when(questionService.getQuestionById(questionId)).thenReturn(question);

        ResponseEntity<Question> response = questionController.getQuestionById(questionId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(questionId);
    }

    @Test
    void getQuestionById_returnsNotFound_whenQuestionDoesNotExist() {

        UUID questionId = UUID.randomUUID();
        when(questionService.getQuestionById(questionId)).thenReturn(null);

        ResponseEntity<Question> response = questionController.getQuestionById(questionId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteQuestion_deletesQuestion() {

        UUID questionId = UUID.randomUUID();
        doNothing().when(questionService).deleteQuestion(questionId);

        ResponseEntity<Void> response = questionController.deleteQuestion(questionId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(questionService, times(1)).deleteQuestion(questionId);
    }

    @Test
    void addQuestion_createsNewQuestion() {

        QuestionCreateRequest request = new QuestionCreateRequest("Title", "Content", UUID.randomUUID());
        Question question = new Question(UUID.randomUUID(), request.title(), request.content(), null, request.ownerId());
        when(questionService.addQuestion(any(QuestionCreateRequest.class))).thenReturn(question);

        ResponseEntity<Question> response = questionController.addQuestion(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(request.title());
    }

    @Test
    void getQuestionsByOwnerId_returnsListOfQuestions() {

        UUID ownerId = UUID.randomUUID();
        List<Question> questions = List.of(new Question(UUID.randomUUID(), "Title", "Content", null, ownerId));
        when(questionService.getQuestionsByOwnerId(ownerId)).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionController.getQuestionsByOwnerId(ownerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void searchQuestions_returnsMatchingQuestions() {

        String keyword = "Title";
        List<Question> questions = List.of(new Question(UUID.randomUUID(), "Title", "Content", null, UUID.randomUUID()));
        when(questionService.searchQuestions(keyword)).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionController.searchQuestions(keyword);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }
}
