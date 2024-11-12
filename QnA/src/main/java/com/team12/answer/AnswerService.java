package com.team12.answer;

import com.team12.clients.notification.NotificationClient;
import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.clients.notification.dto.NotificationType;
import com.team12.question.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AnswerService {

    private AnswerRepository answerRepository;

    private final NotificationClient notificationClient;
    private final QuestionService questionService;

    public Answer createAnswer(Answer answer) {
        answer.setCreatedAt(LocalDateTime.now());


        //send notification to problem owner
        notificationClient.sendNotification(new NotificationRequest(
                questionService.getQuestionById(answer.getQuestionId()).getOwnerId(),
                answer.getContent(),
                NotificationType.ANSWER_POSTED
        ));

        return answerRepository.save(answer);
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public Optional<Answer> getAnswerById(UUID id) {
        return answerRepository.findById(id);
    }

    public Answer updateAnswer(UUID id, Answer updatedAnswer) {
        return answerRepository.findById(id)
                .map(answer -> {
                    answer.setContent(updatedAnswer.getContent());
                    answer.setOwnerId(updatedAnswer.getOwnerId());
                    answer.setQuestionId(updatedAnswer.getQuestionId());
                    return answerRepository.save(answer);
                })
                .orElseThrow(() -> new RuntimeException("Answer not found"));
    }

    public void deleteAnswer(UUID id) {
        answerRepository.deleteById(id);
    }

    public List<Answer> getAnswersByQuestionId(UUID questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

}
