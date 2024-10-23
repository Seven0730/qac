package com.team12.clients.qna;

import com.team12.clients.qna.question.dto.QuestionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "question", path = "api/v1/QnA/question")
public interface QuestionClient {

    @GetMapping("/search")
    List<QuestionDto> searchQuestions(@RequestParam String keyword);

}