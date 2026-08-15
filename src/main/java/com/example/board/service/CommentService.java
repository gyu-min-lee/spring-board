package com.example.board.service;

import com.example.board.domain.Comment;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import com.example.board.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }



}
