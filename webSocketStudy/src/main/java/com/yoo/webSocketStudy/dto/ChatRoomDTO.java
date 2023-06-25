package com.yoo.webSocketStudy.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoomDTO {

    // 소켓을 구분하기 위한 Id 사용
    private String roomId;

    // 해당 방의 이름 구분
    private String name;

    //WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션 - 예제에서는 실직적 사용 ❌
    private Set<WebSocketSession> sessions = new HashSet<>();

    // 객체 생성 시 방의 이름을 주입 받고 아이디는 UUID로 생성
    public ChatRoomDTO (String name){
        this.roomId = UUID.randomUUID().toString();
        this.name = name;
    }

}
