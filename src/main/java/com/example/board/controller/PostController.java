package com.example.board.controller;


import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.dto.CommentResponse;
import com.example.board.dto.CreatePostRequest;
import com.example.board.dto.PostResponse;
import com.example.board.dto.UpdatePostRequest;
import com.example.board.exception.PostNotFoundException;
import com.example.board.service.CommentService;
import com.example.board.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping
    public PostResponse createPost(@RequestBody CreatePostRequest request, Authentication authentication) {
        String username = authentication.getName();
        Post post = postService.createPost(request.getTitle(), request.getContent(), username);
        return PostResponse.from(post);
    }

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts().stream()
                .map(PostResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return PostResponse.from(postService.getPost(id));
    }

    @PutMapping("/{id}")
    public PostResponse updatePost(@RequestBody UpdatePostRequest request, @PathVariable Long id, Authentication authentication) {
        Post post =postService.updatePost(request.getTitle(), request.getContent(), id, authentication.getName());
        return PostResponse.from(post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
    }

    @GetMapping("/{postId}/comments")
    public List<CommentResponse> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }
}
