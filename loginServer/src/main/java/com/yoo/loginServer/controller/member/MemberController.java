package com.yoo.loginServer.controller.member;

import com.yoo.loginServer.dto.member.MemberDTO;
import com.yoo.loginServer.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{email}")
    public ResponseEntity<MemberDTO> getMemberInfo(@PathVariable String email){
        MemberDTO memberDTO = MemberDTO.builder().id(email).build();
        return ResponseEntity.ok(memberService.MemberInfo(memberDTO));
    }

}
