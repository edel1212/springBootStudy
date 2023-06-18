package com.yoo.webSocketStudy.controller;

import com.yoo.webSocketStudy.service.SendClientMsgToSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ChatController {

    private final SendClientMsgToSocketService sendClientMsgToSocketService;


    @GetMapping("/chatPage")
    public void chatPage(){
        log.info("--- chat Page----");
    }


    @GetMapping("/chat")
    public String chatGET(){

        log.info("@ChatController, chat GET()");

        return "chat";
    }

    @GetMapping("/send")
    @ResponseBody
    public String sendClient() throws Exception{
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!");
        sendClientMsgToSocketService.sendMsgToAllClient("서버단에서 전송한것임");
        return "success";
    }

}
