package com.yoo.webSocketStudy.service;

import com.yoo.webSocketStudy.config.ChatHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SendClientMsgToSocketServiceImpl implements SendClientMsgToSocketService{

    private final ChatHandler chatHandler;


    @Override
    public void sendMsgToAllClient(String msg) throws Exception{
        log.info("msg ::::::::{}",msg);
        chatHandler.sendMessageToAllClient(msg);
    }
}
