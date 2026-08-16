package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.dto.LoginRequest;
import com.example.board.dto.SignUpRequest;
import com.example.board.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public Member signUp(@RequestBody SignUpRequest request) {
        return memberService.signUp(request.getUsername(), request.getPassword(), request.getNickname());
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return memberService.login(request.getUsername(), request.getPassword());
    }

}
