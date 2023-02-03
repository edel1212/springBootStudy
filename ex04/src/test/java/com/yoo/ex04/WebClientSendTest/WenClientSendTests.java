package com.yoo.ex04.WebClientSendTest;

import com.yoo.ex04.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Log4j2
public class WenClientSendTests {

    private final String TARGET_URI = "http://localhost:8080";

    @Test
    public void getTypeSendTest(){
        // 1 . WebClient 객체 생성 - Parameter : Target Root - URI
        WebClient webClient = WebClient.create(TARGET_URI);

        // Flux는 0-N개의 결과를 처리하는 객체
        Flux<ReplyDTO> fluxResult = webClient.get() // 2 . HTTP Method 지정
                                 .uri("/replies/board/"+ 11L)
                                 .retrieve()
                                 .bodyToFlux(ReplyDTO.class);

        fluxResult.subscribe(log::info);

        log.info("---------------------------");

        // Mono는 0-1개의 결과를 처리하는 객체
        Mono<ReplyDTO> momResult = webClient.get() // 2 . HTTP Method 지정
               // .uri("/replies/board/"+ 11L) // TODO Error Check
                .uri("/replies/board/"+ 100L)
                .retrieve()
                .bodyToMono(ReplyDTO.class);
        momResult.subscribe(log::info);
    }

}
