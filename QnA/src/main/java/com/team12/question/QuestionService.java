package com.team12.question;

import com.team12.clients.qna.question.dto.QuestionCreateRequest;
import com.team12.clients.qna.question.dto.QuestionUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question addQuestion(QuestionCreateRequest request) {
        return questionRepository.save(
                Question.builder()
                        .title(request.title())
                        .content(request.content())
                        .ownerId(request.ownerId())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public Question getQuestionById(UUID id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void deleteQuestion(UUID id) {
        questionRepository.deleteById(id);
    }

    public Question updateQuestion(QuestionUpdateRequest request) {
        return questionRepository.save(
                Question.builder()
                        .id(request.questionId())
                        .title(request.title())
                        .content(request.content())
                        .ownerId(request.ownerId())
                        .build()
        );
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByOwnerId(UUID ownerId) {
        return questionRepository.findByOwnerId(ownerId);
    }

}
