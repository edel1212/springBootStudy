package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    @PreAuthorize("permitAll()") //누구나 접근 가능
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

    @PreAuthorize("hasRole('ADMIN')") //Admin 권한
    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin.....");
    }


    /////////////////////////////////////
    ////특정 사용자만 사용해서 하고싶을떄/////
    /////////////////////////////////////

    /***
     * @Description : 특정 사용자만 사용해서 하고싶을떄
     *                1) Parameter 로 로그인 Info 를 받는다  ✔@AuthenticationPrincipal 이어야함
     *                2) @PreAuthorize()  방식으로 접근 권한을 체크하는데
     *                   1번에서 받는 변수에 #을 붙여 AuthenticationPrincipal 에 접근이 가능함!
     * */
    @PreAuthorize("#clubAuthMemberDTO != null && #clubAuthMemberDTO.username eq \"user95@naver.com\"")
    @GetMapping("/exOnly")
    public String onlyTargetUser(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
        log.info("=======================");
        log.info("onlyTargetMember!!");
        log.info(clubAuthMemberDTO);
        log.info("=======================");

        return "/sample/admin";
    }


}
