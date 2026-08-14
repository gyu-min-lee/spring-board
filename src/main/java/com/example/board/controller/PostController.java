package com.example.board.controller;


import com.example.board.domain.Post;
import com.example.board.dto.CreatePostRequest;
import com.example.board.exception.PostNotFoundException;
import com.example.board.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post createPost(@RequestBody CreatePostRequest request) {
        return postService.createPost(request.getTitle(), request.getContent());
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
    public Post updatePost(@RequestBody CreatePostRequest request, @PathVariable Long id) {
        return postService.updatePost(request.getTitle(), request.getContent(), id);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
