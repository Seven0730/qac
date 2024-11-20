package com.team12;

import com.team12.clients.qna.QnAClient;
import com.team12.clients.qna.question.dto.QuestionDto;
import com.team12.clients.user.UserClient;
import com.team12.clients.user.dto.UserDto;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableFeignClients(basePackages = "com.team12.clients")
public class SearchFacade {

    private final QnAClient qnaClient;
    private final UserClient userClient;

    public SearchFacade(QnAClient qnaClient, UserClient userClient) {
        this.qnaClient = qnaClient;
        this.userClient = userClient;
    }

    public List<QuestionDto> searchQuestionsByKeyword(String keyword) {
        return qnaClient.searchQuestions(keyword);
    }

    public List<UserDto> searchUsersByUsername(String keyword) {
        return userClient.searchUsersByUsername(keyword);
    }
}
