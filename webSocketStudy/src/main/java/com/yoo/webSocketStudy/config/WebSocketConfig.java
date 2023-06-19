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
                        , "ws/chat")    // 사용될 url 설정
                .setAllowedOrigins("*");         // CORS 설정 모두가 접근 가능

    //https://dev-gorany.tistory.com/224 적용 테스트 필요
    }
}
