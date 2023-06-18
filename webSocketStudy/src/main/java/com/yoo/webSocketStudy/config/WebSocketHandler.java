package com.yoo.webSocketStudy.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class WebSocketHandler extends TextWebSocketHandler {

    /**
     * WebSocket 세션 목록(list)에 있는 모든 세션에게 메시지를 보내기 위해 사용됩니다.
     *
     * 2개의 클라이언트 연결 시 들어있는 목록
     *  - [StandardWebSocketSession[id=8feb5e84-0399-b91a-e334-3526ed284250, uri=ws://localhost:8080/ws/chat]
     *  - , StandardWebSocketSession[id=e9ba4e10-747b-bdae-7466-e24b738be127, uri=ws://localhost:8080/ws/chat]]
     * */
    private  static List<WebSocketSession> list = new ArrayList<>();

    /** 통신간 메서드 전송시 사용될 메서드 */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 저장 되어있는 모든 클라이은트 들에게 전달하기 위해서이다.
        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    /** Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결 목록에 추가
        this.list.add(session);
        log.info(session + " 클라이언트 접속");
    }

    /** Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결 목록에서 제거
        this.list.remove(session);
        log.info(session + " 클라이언트 접속 해제");
    }

    /**
     * @description : Service에서 요청하여 소켓에 연결 중인 모든 클라이언트에 값 전달.
     * @param string message
     * @return void
     * **/
    public void sendMessageToAllClient(String message) throws Exception{
        TextMessage textMessage  = new TextMessage(message);
        log.info("------------------------");
        log.info("소켓에 접속중인 세션 목록 :::{}",list);
        log.info("textMessage :::{}",textMessage);
        log.info("------------------------");

        // 모든 세션에 전달하기위한 Loop
        for(WebSocketSession sess: list) {
            sess.sendMessage(textMessage);
        }
    }

}

