package com.example.board.service;

import com.example.board.domain.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void 회원가입_성공() {
        // when
        Member member = memberService.signUp("testuser", "1234", "닉네임");
        // then
        assertThat(member.getUsername()).isEqualTo("testuser");

    }

    @Test
    void 회원가입_중복아이디_실패() {
        // given
        memberService.signUp("testuser", "1234", "닉네임1");
        // when & then
        assertThatThrownBy(() -> memberService.signUp("testuser", "5678", "닉네임2"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }

    @Test
    void 로그인() {
        // given
        Member member = memberService.signUp("testuser", "1234", "닉네임");
        // when
        String token = memberService.login("testuser", "1234");
        //then
        assertThat(token).isNotNull();
    }

    @Test
    void 로그인_아이디_실패() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        // when & then
        assertThatThrownBy(() -> memberService.login("ttestuser", "1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 아이디입니다.");
    }

    @Test
    void 로그인_비번_실패() {
        // given
        memberService.signUp("testuser", "1234", "닉네임");
        // when & then
        assertThatThrownBy(() -> memberService.login("testuser", "2345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}