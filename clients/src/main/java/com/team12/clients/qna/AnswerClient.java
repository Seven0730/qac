package com.team12.clients.qna;

import com.team12.clients.qna.answer.dto.AnswerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = "qna", path = "/api/v1/QnA/answers")
public interface AnswerClient {

    @GetMapping("/{id}")
    AnswerDto getAnswerById(@PathVariable("id") UUID id);
}
