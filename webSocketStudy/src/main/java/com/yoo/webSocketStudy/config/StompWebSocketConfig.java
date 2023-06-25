package com.yoo.webSocketStudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 👉 Stomp를 사용하기위해 추가
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //WebSocket 또는 SockJS Client가 웹소켓을 사용하기 위해 "핸드셰이크" 커넥션을 생성할 Path
        registry.addEndpoint("/stomp/chat")
                // "*" 적용 시 보안상 문제로 에러 발생
                .setAllowedOrigins("http://localhost:8080","http://localhost:8081")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Client에서 SEND 요청을 처리
        registry.setApplicationDestinationPrefixes("/pub");
        //  해당하는 경로를 SUBSCRIBE하는 Client에게 메세지를 전달하는 간단한 작업을 수행
        registry.enableSimpleBroker("/sub");
    }

}
