package com.team12.question;

import com.team12.clients.qna.question.dto.QuestionCreateRequest;
import com.team12.clients.qna.question.dto.QuestionUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllQuestions() {
        List<Question> questions = Arrays.asList(new Question(), new Question());
        when(questionService.getAllQuestions()).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionController.getAllQuestions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    public void testGetQuestionById() {
        Question question = new Question();
        when(questionService.getQuestionById(1)).thenReturn(question);

        ResponseEntity<Question> response = questionController.getQuestionById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    @Test
    public void testGetQuestionById_NotFound() {
        when(questionService.getQuestionById(1)).thenReturn(null);

        ResponseEntity<Question> response = questionController.getQuestionById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteQuestion() {
        doNothing().when(questionService).deleteQuestion(1);

        ResponseEntity<Void> response = questionController.deleteQuestion(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testUpdateQuestion() {
        QuestionUpdateRequest request = new QuestionUpdateRequest("title", "content", "ownerId", "questionId");
        Question updatedQuestion = new Question();
        when(questionService.updateQuestion(request)).thenReturn(updatedQuestion);

        ResponseEntity<Question> response = questionController.updateQuestion(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedQuestion, response.getBody());
    }

    @Test
    public void testAddQuestion() {
        QuestionCreateRequest request = new QuestionCreateRequest("title", "content", "ownerId");
        Question newQuestion = new Question();
        when(questionService.addQuestion(request)).thenReturn(newQuestion);

        ResponseEntity<Question> response = questionController.addQuestion(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newQuestion, response.getBody());
    }

    @Test
    public void testGetQuestionsByOwnerId() {
        List<Question> questions = Arrays.asList(new Question(), new Question());
        when(questionService.getQuestionsByOwnerId("ownerId")).thenReturn(questions);

        ResponseEntity<List<Question>> response = questionController.getQuestionsByOwnerId("ownerId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }
}