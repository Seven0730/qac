package com.team12.question;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;


    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question getQuestionById(int id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void deleteQuestion(int id) {
        questionRepository.deleteById(id);
    }

    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByOwnerId(String ownerId) {
        return questionRepository.findByOwnerId(ownerId);
    }

}
