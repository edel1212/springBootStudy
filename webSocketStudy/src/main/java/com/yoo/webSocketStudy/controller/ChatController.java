package com.yoo.webSocketStudy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ChatController {


    @GetMapping("/chatPage")
    public void chatPage(){
        log.info("--- chat Page----");
    }


    @GetMapping("/chat")
    public String chatGET(){

        log.info("@ChatController, chat GET()");

        return "chat";
    }

}
