package com.team12.question;

import com.team12.clients.qna.question.dto.QuestionCreateRequest;
import com.team12.clients.qna.question.dto.QuestionUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class QuestionControllerTest {

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
        List<Question> questions = List.of(new Question());
        when(questionService.getAllQuestions()).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionController.getAllQuestions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void getQuestionById_returnsQuestion_whenFound() {
        UUID id = UUID.randomUUID();
        Question question = new Question();
        when(questionService.getQuestionById(id)).thenReturn(question);

        ResponseEntity<Question> response = questionController.getQuestionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    @Test
    void getQuestionById_returnsNotFound_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(questionService.getQuestionById(id)).thenReturn(null);

        ResponseEntity<Question> response = questionController.getQuestionById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteQuestion_deletesQuestion() {
        UUID id = UUID.randomUUID();
        doNothing().when(questionService).deleteQuestion(id);

        ResponseEntity<Void> response = questionController.deleteQuestion(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(questionService, times(1)).deleteQuestion(id);
    }

    @Test
    void updateQuestion_updatesQuestion() {
        QuestionUpdateRequest request = new QuestionUpdateRequest(
                "title", "content", UUID.randomUUID(), UUID.randomUUID());
        Question updatedQuestion = new Question();
        when(questionService.updateQuestion(request)).thenReturn(updatedQuestion);

        ResponseEntity<Question> response = questionController.updateQuestion(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedQuestion, response.getBody());
    }

    @Test
    void addQuestion_createsNewQuestion() {
        QuestionCreateRequest request = new QuestionCreateRequest(
                "title", "content", UUID.randomUUID()
        );
        Question newQuestion = new Question();
        when(questionService.addQuestion(request)).thenReturn(newQuestion);

        ResponseEntity<Question> response = questionController.addQuestion(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newQuestion, response.getBody());
    }

    @Test
    void getQuestionsByOwnerId_returnsListOfQuestions() {
        UUID ownerId = UUID.randomUUID();
        List<Question> questions = List.of(new Question());
        when(questionService.getQuestionsByOwnerId(ownerId)).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionController.getQuestionsByOwnerId(ownerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void getQuestionsByOwnerId_returnsEmptyList_whenNoQuestionsFound() {
        UUID ownerId = UUID.randomUUID();
        when(questionService.getQuestionsByOwnerId(ownerId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Question>> response = questionController.getQuestionsByOwnerId(ownerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }
}