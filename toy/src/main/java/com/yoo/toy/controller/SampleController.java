package com.yoo.toy.controller;

import com.yoo.toy.dto.security.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll(){
        log.info("누구나 접근 가능");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO dto){

        log.info("login info ::: {}",dto);

        log.info("Member 이상의 권한만 접근 가능");
    }

    @GetMapping("/admin")
    public void exAdmin(){
        log.info("Admin 이상의 권한만 접근 가능");
    }


    @PreAuthorize("permitAll()")
    @GetMapping("/login")
    public void loginPage(HttpServletRequest request, Model model){
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.info(request.getAttribute("errorMap"));
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@");
        model.addAttribute("test",request.getAttribute("errorMap"));
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(Map<String, String> errorMap){
        return ResponseEntity.ok().body(errorMap);
    }

}
