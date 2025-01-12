package com.yoo.webflux_study.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClientBuild(){
        /**
         * 	•	responseTimeout: 클라이언트가 서버에 요청을 보낸 후, 서버에서 응답을 보내는 데 5초 이상 걸리면 타임아웃이 발생합니다.
         * 	•	readTimeout: 서버에서 응답을 시작했으나, 클라이언트가 응답 데이터를 읽는 데 5초 이상 걸리면 타임아웃이 발생합니다.
         * 	•	writeTimeout: 클라이언트가 서버에 데이터를 보내는 데 5초 이상 걸리면 타임아웃이 발생합니다.
         * */
        // HttpClient 구성
        HttpClient httpClient = HttpClient.create()
                // 연결 타임아웃 설정 (5000ms)
                .responseTimeout(Duration.ofMillis(5000)) // 응답 대기 시간
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 연결 타임아웃
                // 이 핸들러는 서버로부터 응답을 읽을 때 지정된 시간(밀리초) 동안 응답이 오지 않으면 타임아웃을 발생시킵니다.
                .doOnConnected(conn -> conn
                        // 1.	읽기 타임아웃: 서버에서 응답을 받는 데 5초를 초과하면 타임아웃 에러 발생
                        //	2.	쓰기 타임아웃: 서버로 데이터를 전송하는 데 5초를 초과하면 타임아웃 에러 발생
                        .addHandlerLast(new ReadTimeoutHandler(5000))  // 읽기 타임아웃
                        .addHandlerLast(new WriteTimeoutHandler(5000)));

        return WebClient.builder()
                .baseUrl("http://localhost:3000")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // ExchangeStrategies를 설정하여 메모리 크기 제한 설정 (16MB)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)
                                                            //.maxInMemorySize(-1)) //제한없음
                        ) // 최대 메모리 크기 설정 (16MB)
                        .build())
                .build();
    }
}
