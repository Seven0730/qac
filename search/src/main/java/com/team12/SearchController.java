package com.team12;

import com.team12.clients.qna.question.dto.QuestionDto;
import com.team12.clients.user.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDto>> searchQuestions(@RequestParam String keyword) {
        List<QuestionDto> result = searchService.searchQuestionsByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String keyword) {
        List<UserDto> result = searchService.searchUsersByUsername(keyword);
        return ResponseEntity.ok(result);
    }
}