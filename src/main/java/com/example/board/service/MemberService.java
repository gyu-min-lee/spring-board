package com.example.board.service;

import com.example.board.domain.Member;
import com.example.board.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member signUp(String username, String password, String nickname) {
        if(memberRepository.existsByUsername(username)) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        member.setNickname(nickname);

        return memberRepository.save(member);
    }
}
