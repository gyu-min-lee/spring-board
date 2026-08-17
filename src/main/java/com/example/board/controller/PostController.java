package com.example.board.controller;


import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.dto.CreatePostRequest;
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
    public Post createPost(@RequestBody CreatePostRequest request, Authentication authentication) {
        String username = authentication.getName();
        return postService.createPost(request.getTitle(), request.getContent(), username);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/{id}")
    public Post updatePost(@RequestBody UpdatePostRequest request, @PathVariable Long id, Authentication authentication) {
        return postService.updatePost(request.getTitle(), request.getContent(), id, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
    }

    @GetMapping("/{postId}/comments")
    public List<Comment> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }
}
