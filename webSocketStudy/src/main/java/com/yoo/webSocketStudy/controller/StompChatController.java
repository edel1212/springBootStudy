package com.yoo.webSocketStudy.controller;

import com.yoo.webSocketStudy.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * @MessageMapping 을 통해 WebSocket으로 들어오는 메세지 발행을 처리한다.
     *
     * - 둘의 URL Path 정보를 구분해 놓은 이유는
     *   enter의 경우 Client에서 첫 한번만 실행 되게 끔 함 "~님이 입장"을 위해 사용되었음
     *   message의 경우 받아온 메세지를 전달 하기 위함임
     * **/
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message){
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message){
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
