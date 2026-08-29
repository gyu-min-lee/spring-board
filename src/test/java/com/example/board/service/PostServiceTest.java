package com.example.board.service;

import com.example.board.domain.Post;
import com.example.board.exception.PostNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Test
    void 게시글_작성_성공() {
        //given
        memberService.signUp("testuser", "1234", "닉네임");
        //when
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        //then
        assertThat(post.getTitle()).isEqualTo("testtitle");
    }

    @Test
    void 게시글_단건조회_성공() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        // when
        Post foundPost = postService.getPost(post.getId());
        // then
        assertThat(foundPost.getTitle()).isEqualTo("testtitle");
    }

    @Test
    void 게시글_단건조회_실패_존재하지않음() {
        // when & then
        assertThatThrownBy(() -> postService.getPost(999L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    void 수정_성공() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        // when
        Post updatePost = postService.updatePost("updatetitle", "updatecontent", post.getId(), "testuser");
        // then
        assertThat(updatePost.getContent()).isEqualTo("updatecontent");
    }

    @Test
    void 수정_실패_존재하지않음() {
        // when & then
        assertThatThrownBy(() -> postService.updatePost("updatetitle", "updatecontent", 999L, "testuser"))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    void 수정_실패_작성자아님() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        memberService.signUp("otheruser", "4321", "닉네임1");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        // when & then
        assertThatThrownBy(() -> postService.updatePost("updatetitle", "updatecontent", post.getId(), "otheruser"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("게시글 수정 권한이 없습니다.");
    }

    @Test
    void 삭제_성공() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        // when
        postService.deletePost(post.getId(), "testuser");
        // then
        assertThatThrownBy(() -> postService.getPost(post.getId()))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void 삭제_실패_존재하지않음() {
        // when & then
        assertThatThrownBy(() -> postService.deletePost(999L, "testuser"))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    void 삭제_실패_작성자아님() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        memberService.signUp("otheruser", "4321", "닉네임1");
        Post post = postService.createPost("testtitle", "testcontent", "testuser");
        // when & then
        assertThatThrownBy(() -> postService.deletePost(post.getId(), "otheruser"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("게시글 삭제 권한이 없습니다.");
    }
}