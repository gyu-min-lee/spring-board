package com.example.board.service;

import com.example.board.domain.Comment;
import com.example.board.domain.Member;
import com.example.board.domain.Post;
import com.example.board.exception.PostNotFoundException;
import com.example.board.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Test
    void 댓글_작성_성공() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        memberService.signUp("testuser1", "1234", "닉네임1");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        // when
        Comment comment = commentService.createComment(post.getId(), "testcontent", "testuser1");
        // then
        assertThat(comment.getContent()).isEqualTo("testcontent");
    }

    @Test
    void 댓글_작성_실패_존재하지않음() {
        // when & then
        assertThatThrownBy(() -> commentService.createComment(999L, "testcontent", "testuser1"))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    void 댓글_작성_실패_회원없음() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        // when & then
        assertThatThrownBy(() -> commentService.createComment(post.getId(), "testcontent", "testuser1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

    @Test
    void 댓글_목록조회_성공() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        memberService.signUp("testuser1", "1234", "닉네임1");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        commentService.createComment(post.getId(), "testcontent", "testuser");
        commentService.createComment(post.getId(), "testcontent1", "testuser1");
        // when
        List<Comment> comments = commentService.getCommentsByPostId(post.getId());
        // then
        assertThat(comments).hasSize(2);
    }

    @Test
    void 댓글_수정_성공() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        memberService.signUp("testuser1", "1234", "닉네임1");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        Comment comment = commentService.createComment(post.getId(), "testcontent", "testuser1");
        // when
        Comment updateComment = commentService.updateComment("updatecontent", comment.getId(), "testuser1");
        // then
        assertThat(updateComment.getContent()).isEqualTo("updatecontent");
    }

    @Test
    void 댓글_수정_실패_존재하지않음() {
        // when & then
        assertThatThrownBy(() -> commentService.updateComment("updatecontent", 999L, "testuser1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
    }

    @Test
    void 댓글_수정_실패_작성자아님() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        memberService.signUp("testuser1", "1234", "닉네임1");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        Comment comment = commentService.createComment(post.getId(), "testcontent", "testuser1");
        // when & then
        assertThatThrownBy(() -> commentService.updateComment("updatecontent", comment.getId(), "testuser"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("댓글 수정 권한이 없습니다.");
    }

    @Test
    void 댓글_삭제_성공() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        memberService.signUp("testuser1", "1234", "닉네임1");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        Comment comment = commentService.createComment(post.getId(), "testcontent", "testuser1");
        Comment otherComment = commentService.createComment(post.getId(), "testcontent", "testuser1");
        // when
        commentService.deleteComment(comment.getId(), "testuser1");
        // then
        List<Comment> comments = commentService.getCommentsByPostId(post.getId());

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getId()).isEqualTo(otherComment.getId());
    }

    @Test
    void 댓글_삭제_실패_존재하지않음() {
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(999L, "testuser1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
    }

    @Test
    void 댓글_삭제_실패_작성자아님() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        memberService.signUp("testuser1", "1234", "닉네임1");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        Comment comment = commentService.createComment(post.getId(), "testcontent", "testuser1");
        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId(), "testuser"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("댓글 삭제 권한이 없습니다.");
    }
}