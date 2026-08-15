package com.example.board.service;

import com.example.board.domain.Post;
import com.example.board.domain.Member;
import com.example.board.exception.PostNotFoundException;
import com.example.board.repository.MemberRepository;
import com.example.board.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public Post createPost(String title, String content) {

        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("임시 사용자가 없습니다."));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setMember(member);

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    public Post updatePost(String title, String content, Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        post.setTitle(title);
        post.setContent(content);

        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
        postRepository.delete(post);
    }
}
