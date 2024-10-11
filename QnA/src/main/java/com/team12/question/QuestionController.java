package com.team12.question;

import com.team12.clients.qna.question.dto.QuestionCreateRequest;
import com.team12.clients.qna.question.dto.QuestionUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/QnA/question")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable int id) {
        Question question = questionService.getQuestionById(id);
        if (question != null) {
            return ResponseEntity.ok(question);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable int id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Question> updateQuestion(@RequestBody QuestionUpdateRequest request) {
        Question updatedQuestion = questionService.updateQuestion(request);
        return ResponseEntity.ok(updatedQuestion);
    }

    @PostMapping("/add")
    public ResponseEntity<Question> addQuestion(@RequestBody QuestionCreateRequest request) {
        Question newQuestion = questionService.addQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newQuestion);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Question>> getQuestionsByOwnerId(@PathVariable String ownerId) {
        List<Question> questions = questionService.getQuestionsByOwnerId(ownerId);
        return ResponseEntity.ok(questions);
    }
}
