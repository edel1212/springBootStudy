package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    @GetMapping("/all")
    public void exAll(){
        log.info("exAll.....");
    }
    
    //@AuthenticationPrincipal 를 사용하면 별도의  케스팅 없이 데이터를
    //사용이 가능함
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
        
        log.info("exMember....");
        
        log.info("로그인 정보 :::" + clubAuthMemberDTO);
        
    }

    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin.....");
    }

}
