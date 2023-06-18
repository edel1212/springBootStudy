package com.yoo.webSocketStudy.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SendClientMsgTests {

    @Autowired
    private SendClientMsgToSocketService sendClientMsgToSocketService;

    @Test
    @Description("서버단에서 전달 테스트")
    public void testCode1() throws  Exception{
        sendClientMsgToSocketService.sendMsgToAllClient("서비스단에서 보내는 메세지 입니다!!!!");
    }

}
