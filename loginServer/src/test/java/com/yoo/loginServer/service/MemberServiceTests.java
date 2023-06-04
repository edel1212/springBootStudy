package com.yoo.loginServer.service;

import com.yoo.loginServer.dto.member.MemberDTO;
import com.yoo.loginServer.enums.MemberRoles;
import com.yoo.loginServer.service.member.MemberService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCode1(){
        log.info("Hello world");
    }


    @Test
    public void findMemberTest(){
        MemberDTO memberDTO = MemberDTO.builder()
                .id("edel1212@naver.com")
                .build();
        boolean result = memberService.findMember(memberDTO);
        log.info("result ::: {}", result);
    }

    @Test
    public void registerMember(){
        log.info(MemberRoles.ADMIN.toString());
        log.info("-----------------------------");
        MemberDTO memberDTO = MemberDTO.builder()
                .id("edel1212@naver.com")
                .pw(passwordEncoder.encode("1111"))
                .name("유정호")
                .role(MemberRoles.ADMIN.toString())
                .build();

        String resultId = memberService.registerMember(memberDTO);

        log.info("resultId ::: {}",resultId);
    }

}
