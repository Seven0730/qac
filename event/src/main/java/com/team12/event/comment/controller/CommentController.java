package com.team12.event.comment.controller;


import com.team12.clients.comment.dto.CommentModifyRequest;
import com.team12.clients.comment.dto.CommentSendRequest;
import com.team12.event.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {this.commentService = commentService;}

    @PostMapping("/sendComment")
    public void sendComment(@RequestBody CommentSendRequest commentSendRequest) {
        commentService.commentSend(commentSendRequest);
        log.info("Sending comment {}", commentSendRequest);
    }


    @PutMapping("/modifyComment/{id}")
    public ResponseEntity<String> modifyComment(@PathVariable UUID id, @RequestBody CommentModifyRequest request) {
        log.info("Modifying comment with ID: {}, New Content: {}", id, request.content());
        boolean modified = commentService.commentModify(id, request);

        if (modified) {
            return ResponseEntity.ok("Comment modified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }
    }


    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable UUID id) {
        boolean deleted = commentService.commentDelete(id);

        if (deleted) {
            return ResponseEntity.ok("Comment deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }
    }
}
