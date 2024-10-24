package com.team12.event.comment.controller;


import com.team12.clients.comment.dto.CommentModifyRequest;
import com.team12.clients.comment.dto.CommentSendRequest;
import com.team12.event.comment.entity.CommentDto;
import com.team12.event.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {this.commentService = commentService;}

    @GetMapping("/getCommentByAnswerId/{id}")
    public ResponseEntity<List<CommentDto>> getCommentByAnswerId(@PathVariable UUID id) {
        List<CommentDto> comments = commentService.getCommentsByAnswerId(id);

        return ResponseEntity.ok(comments != null ? comments : new ArrayList<>());
    }


    @PostMapping("/sendComment")
    public ResponseEntity<CommentDto> sendComment(@RequestBody CommentSendRequest commentSendRequest) {
        CommentDto createdComment = commentService.commentSend(commentSendRequest);
        log.info("Sending comment {}", commentSendRequest);

        return ResponseEntity.ok(createdComment);
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

    @DeleteMapping("/deleteCommentByAnswerId/{id}")
    public ResponseEntity<Void> deleteCommentByAnswerId(@PathVariable UUID id) {
        commentService.commentDeleteByAnswerId(id);
        return ResponseEntity.noContent().build();
    }
}
