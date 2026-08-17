package com.example.board.controller;

import com.example.board.domain.Comment;
import com.example.board.dto.CreateCommentRequest;
import com.example.board.dto.UpdateCommentRequest;
import com.example.board.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment createComment(@RequestBody CreateCommentRequest request, Authentication authentication) {
        return commentService.createComment(request.getPostId(), request.getContent(), authentication.getName());
    }

    @PutMapping("/{id}")
    public Comment updateComment(@RequestBody UpdateCommentRequest request, @PathVariable Long id, Authentication authentication) {
        return commentService.updateComment(request.getContent(), id, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id, Authentication authentication) {
        commentService.deleteComment(id, authentication.getName());
    }
}
