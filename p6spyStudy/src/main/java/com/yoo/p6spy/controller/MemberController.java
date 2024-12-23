package com.yoo.p6spy.controller;

import com.yoo.p6spy.entity.Member;
import com.yoo.p6spy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private MemberRepository memberRepository;

    @GetMapping
    ResponseEntity<String> registerMember(){
        Member member = Member.builder()
                .id(UUID.randomUUID().toString())
                .name("i")
                .build();
        memberRepository.save(member);
        return ResponseEntity.ok("success");
    }
}
