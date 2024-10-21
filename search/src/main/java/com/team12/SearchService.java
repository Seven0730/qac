package com.team12;

import com.team12.clients.qna.QuestionClient;
import com.team12.clients.qna.question.dto.QuestionDto;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableFeignClients(basePackages = "com.team12.clients")
public class SearchService {

    private final QuestionClient questionClient;

    public SearchService(QuestionClient questionClient) {
        this.questionClient = questionClient;
    }

    public List<QuestionDto> searchQuestionsByKeyword(String keyword) {
        return questionClient.searchQuestions(keyword);
    }
}
