package com.team12.clients.qna;

import com.team12.clients.qna.question.dto.QuestionDto;
import com.team12.clients.qna.answer.dto.AnswerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "qna", path = "/api/v1/QnA")
public interface QnAClient {

    @GetMapping("/question/search")
    List<QuestionDto> searchQuestions(@RequestParam("keyword") String keyword);

    @GetMapping("/answers/{id}")
    AnswerDto getAnswerById(@PathVariable("id") UUID id);
}
