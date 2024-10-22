// AnswerControllerTest.java
package com.team12.question;

import com.team12.answer.Answer;
import com.team12.answer.AnswerController;
import com.team12.answer.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AnswerControllerTest {

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private AnswerController answerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAnswer_returnsCreatedAnswer() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        Answer answer = new Answer(id, "Test content", null, userId, questionId);
        when(answerService.createAnswer(any(Answer.class))).thenReturn(answer);

        ResponseEntity<Answer> response = answerController.createAnswer(answer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(answer, response.getBody());
    }

    @Test
    void getAllAnswers_returnsListOfAnswers() {
        List<Answer> answers = List.of(new Answer());
        when(answerService.getAllAnswers()).thenReturn(answers);

        ResponseEntity<List<Answer>> response = answerController.getAllAnswers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(answers, response.getBody());
    }

    @Test
    void getAnswerById_returnsAnswer_whenFound() {
        UUID id = UUID.randomUUID();
        Answer answer = new Answer(id, "Test content", null, UUID.randomUUID(), UUID.randomUUID());
        when(answerService.getAnswerById(id)).thenReturn(Optional.of(answer));

        ResponseEntity<Answer> response = answerController.getAnswerById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(answer, response.getBody());
    }

    @Test
    void getAnswerById_returnsNotFound_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(answerService.getAnswerById(id)).thenReturn(Optional.empty());

        ResponseEntity<Answer> response = answerController.getAnswerById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateAnswer_updatesAndReturnsAnswer() {
        UUID id = UUID.randomUUID();
        Answer updatedAnswer = new Answer(id, "Updated content", null, UUID.randomUUID(), UUID.randomUUID());
        when(answerService.updateAnswer(eq(id), any(Answer.class))).thenReturn(updatedAnswer);

        ResponseEntity<Answer> response = answerController.updateAnswer(id, updatedAnswer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAnswer, response.getBody());
    }

    @Test
    void deleteAnswer_deletesAnswerAndReturnsNoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(answerService).deleteAnswer(id);

        ResponseEntity<Void> response = answerController.deleteAnswer(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(answerService, times(1)).deleteAnswer(id);
    }
}
