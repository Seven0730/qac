package com.team12.question;

import com.team12.clients.qna.question.dto.QuestionCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 添加新问题
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

    // 根据 ID 获取问题
    public Question getQuestionById(UUID id) {
        return questionRepository.findById(id).orElse(null);
    }

    // 删除问题
    public void deleteQuestion(UUID id) {
        questionRepository.deleteById(id);
    }

    // 更新问题
    public Question updateQuestion(Question question) {
        // 直接更新传入的 Question 对象
        return questionRepository.save(question);
    }

    // 根据关键字搜索问题
    public List<Question> searchQuestions(String keyword) {
        return questionRepository.searchByKeyword(keyword);
    }

    // 获取所有问题
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // 根据 Owner ID 获取问题
    public List<Question> getQuestionsByOwnerId(UUID ownerId) {
        return questionRepository.findByOwnerId(ownerId);
    }
}
