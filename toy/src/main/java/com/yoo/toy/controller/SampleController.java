package com.yoo.toy.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/all")
    public void exAll(){
        log.info("누구나 접근 가능");
    }

    @GetMapping("/member")
    public void exMember(){
        log.info("Member 이상의 권한만 접근 가능");
    }

    @GetMapping("/admin")
    public void exAdmin(){
        log.info("Admin 이상의 권한만 접근 가능");
    }

}
