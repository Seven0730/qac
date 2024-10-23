package com.team12.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public Answer createAnswer(Answer answer) {
        answer.setCreatedAt(LocalDateTime.now());
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
