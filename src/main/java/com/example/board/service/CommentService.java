package com.example.board.service;

import com.example.board.domain.Comment;
import com.example.board.domain.Member;
import com.example.board.domain.Post;
import com.example.board.exception.PostNotFoundException;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import com.example.board.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Comment createComment(Long postId, String content, String username) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(content);
        comment.setMember(member);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment updateComment(String content, Long id, String username) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("댓글 수정 권한이 없습니다.");
        }

        comment.setContent(content);

        return commentRepository.save(comment);
    }

    public void deleteComment(Long id, String username) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("댓글 수정 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }


}
