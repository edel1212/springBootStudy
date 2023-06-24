package com.yoo.serverReq.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@Log4j2
public class ChatPageController {

    @GetMapping("/chatPage")
    public void chatPage(){
        log.info("ChatPag!!");
    }

    @GetMapping("/chatPageWithStomp")
    public void chatPageWithStomp(){
        log.info("ChatPageWithStomp!!");
    }


}
