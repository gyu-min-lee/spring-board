package com.example.board.service;

import com.example.board.domain.Member;
import com.example.board.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member signUp(String username, String password, String nickname) {
        if(memberRepository.existsByUsername(username)) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));
        member.setNickname(nickname);

        return memberRepository.save(member);
    }
}
