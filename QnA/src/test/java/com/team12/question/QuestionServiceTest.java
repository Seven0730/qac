package com.team12.question;

import com.team12.clients.qna.question.dto.QuestionCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    public void testAddQuestion() {
        QuestionCreateRequest request = new QuestionCreateRequest("Test Title", "Test Content", UUID.randomUUID());
        Question savedQuestion = Question.builder()
                .id(UUID.randomUUID())
                .title(request.title())
                .content(request.content())
                .ownerId(request.ownerId())
                .createdAt(LocalDateTime.now())
                .build();

        when(questionRepository.save(any(Question.class))).thenReturn(savedQuestion);

        Question result = questionService.addQuestion(request);

        assertNotNull(result);
        assertEquals(request.title(), result.getTitle());
        assertEquals(request.content(), result.getContent());
        assertEquals(request.ownerId(), result.getOwnerId());
    }

    @Test
    public void testGetQuestionById() {
        UUID questionId = UUID.randomUUID();
        Question question = Question.builder()
                .id(questionId)
                .title("Test Title")
                .content("Test Content")
                .ownerId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        when(questionRepository.findById(questionId)).thenReturn(java.util.Optional.of(question));

        Question result = questionService.getQuestionById(questionId);

        assertNotNull(result);
        assertEquals(questionId, result.getId());
        assertEquals("Test Title", result.getTitle());
    }

    @Test
    public void testDeleteQuestion() {
        UUID questionId = UUID.randomUUID();

        questionService.deleteQuestion(questionId);

        verify(questionRepository, times(1)).deleteById(questionId);
    }

    @Test
    public void testSearchQuestions() {
        String keyword = "Test";
        List<Question> questions = List.of(
                new Question(UUID.randomUUID(), "Test Title", "Test Content", LocalDateTime.now(), UUID.randomUUID()),
                new Question(UUID.randomUUID(), "Another Title", "Test Content", LocalDateTime.now(), UUID.randomUUID())
        );

        when(questionRepository.searchByKeyword(keyword)).thenReturn(questions);

        List<Question> result = questionService.searchQuestions(keyword);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllQuestions() {
        List<Question> questions = List.of(
                new Question(UUID.randomUUID(), "Title 1", "Content 1", LocalDateTime.now(), UUID.randomUUID()),
                new Question(UUID.randomUUID(), "Title 2", "Content 2", LocalDateTime.now(), UUID.randomUUID())
        );

        when(questionRepository.findAll()).thenReturn(questions);

        List<Question> result = questionService.getAllQuestions();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetQuestionsByOwnerId() {
        UUID ownerId = UUID.randomUUID();
        List<Question> questions = List.of(
                new Question(UUID.randomUUID(), "Title 1", "Content 1", LocalDateTime.now(), ownerId)
        );

        when(questionRepository.findByOwnerId(ownerId)).thenReturn(questions);

        List<Question> result = questionService.getQuestionsByOwnerId(ownerId);

        assertEquals(1, result.size());
        assertEquals(ownerId, result.get(0).getOwnerId());
    }
}
