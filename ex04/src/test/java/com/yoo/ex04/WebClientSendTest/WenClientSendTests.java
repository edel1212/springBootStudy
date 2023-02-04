package com.yoo.ex04.WebClientSendTest;

import com.yoo.ex04.dto.BoardDTO;
import com.yoo.ex04.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
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
    public void getTypeSenTest(){

        // 1 . WebClient 객체 생성 - Parameter : Target Root - URI
        WebClient webClient = WebClient.create(TARGET_URI);

        //  👉 Flux Type [ 여러건 ]
        // 2 - 1 . Flux는 0-N개의 결과를 처리하는 객체 [ Response의 반환 데이터 개수에 상관이 없음 ]
        Flux<ReplyDTO> fluxResult = webClient.get()                 // 3 . HTTP Method 지정
                                 .uri("/replies/board/"+ 11L)   // 4 . URL값 추가
                                 .retrieve()                        // 5 . body를 받아 디코딩하는 간단한 메소드
                                 .bodyToFlux(ReplyDTO.class);       // 6 / body내용을 Flux로 사용

        fluxResult.subscribe(log::info);

        log.info("---------------------------------------------------------------------");


        //  👉 Mono Type [ 단건 ]
        // 2-2 . Mono는 0-1개의 결과를 처리하는 객체 [ Response의 반환 데이터 개수가 중요함 2개이상 일경우  Error]
        //       ErrorMsg : from Array value (token `JsonToken.START_ARRAY`);
        //                  nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException
        Mono<ReplyDTO> momResult = webClient.get()
                //.uri("/replies/board/"+ 11L)     //  ☠️ 해당 응답 값은 2건 이상으로 Error 발생
                .uri("/replies/testReplyOne/"+ 142L)//  - 단건
                .retrieve()
                .bodyToMono(ReplyDTO.class);
        momResult.subscribe(log::info);

        log.info("---------------------------------------------------------------------");


    }

}
