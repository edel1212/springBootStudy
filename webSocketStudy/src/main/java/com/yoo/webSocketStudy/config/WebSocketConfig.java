package com.yoo.webSocketStudy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket // WebSocket을 활성화
public class WebSocketConfig implements WebSocketConfigurer {

    // WebSocket을 컨트롤하기 위하여 주입
    private final WebSocketHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler         // 핸들러 주입
                        , "/ws/chat")    // 사용될 url 설정
                //.setAllowedOrigins("*")         ❌ SockJS 사용시 보안상 문제로 "*"사용이 불가능해짐 [CORS 설정 ]
                .setAllowedOriginPatterns("http://localhost:8080", "http://localhost:8081")
                .withSockJS();
    }





}
