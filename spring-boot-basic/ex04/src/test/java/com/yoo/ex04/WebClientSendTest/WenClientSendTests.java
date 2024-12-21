package com.yoo.ex04.WebClientSendTest;

import com.yoo.ex04.dto.BoardDTO;
import com.yoo.ex04.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@Log4j2
public class WenClientSendTests {

    private final String TARGET_URI = "http://localhost:8080";

    //////////////////////////////////////////////////
    //Get Type
    //////////////////////////////////////////////////

    @Description("Flux로 반환 받음 - 다건")
    @Test
    public void getTypeTestWithFlux(){

        // 1 . WebClient 객체 생성 - Parameter : Target Root - URI
        WebClient webClient = WebClient.create(TARGET_URI);

        //  👉 Flux Type [ 여러건 ]
        // 2 . Flux는 0-N개의 결과를 처리하는 객체 [ Response의 반환 데이터 개수에 상관이 없음 ]
        Flux<ReplyDTO> fluxResult = webClient.get()                 // 3 . HTTP Method 지정
                .uri("/replies/board/"+ 11L)   // 4 . URL값 추가
                .retrieve()                        // 5 . body를 받아 디코딩하는 간단한 메소드
                .bodyToFlux(ReplyDTO.class);       // 6 / body내용을 Flux로 사용

        fluxResult.subscribe(log::info);

        log.info("---------------------------------------------------------------------");

    }

    @Description("Mono로 반환 받음 - 단건")
    @Test
    public void getTypeTestWithMono(){
        // 1 . WebClient 객체 생성 - Parameter : Target Root - URI
        WebClient webClient = WebClient.create(TARGET_URI);

        //  👉 Mono Type [ 단건 ]
        // 2 .   Mono는 0-1개의 결과를 처리하는 객체 [ Response의 반환 데이터 개수가 중요함 2개이상 일경우  Error]
        //       ErrorMsg : from Array value (token `JsonToken.START_ARRAY`);
        //                  nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException
        Mono<ReplyDTO> momResult = webClient.get()
                //.uri("/replies/board/"+ 11L)     //  ☠️ 해당 응답 값은 2건 이상으로 Error 발생
                .uri("/replies/testReplyOne/{rno}", 142L)//  - 단건
                .retrieve()
                .bodyToMono(ReplyDTO.class);
        momResult.subscribe(log::info);

        log.info("---------------------------------------------------------------------");
    }

    @Description("Mono로 반환하며 파라미터를 가지고있음")
    @Test
    public void getTypeTestWithMnnoHasParam(){
        /** 💬 IE와 같이 자동으로 인코딩을 해주지 않을경우 아래와 같은
         *     인코딩설정이 필요하나 최신 인터넷 브라우저는 자동으로 인코딩을 해주기에 따로 해줄 필요가 없음!!
            // 1 . DefaultUriBuilderFactory 객체 셍성
            DefaultUriBuilderFaㄴctory factory = new DefaultUriBuilderFactory(TARGET_URI);

            // 2 . 인코딩 모드 설정
            factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

            // 3 . Builder pattern을 사용하여 객체를 생성하며 uriBuilderFactory()에
            //     위에서 생성간 객체값 주입
            WebClient webClient = WebClient.builder()
                    .uriBuilderFactory(factory)
                    .baseUrl(TARGET_URI)
                    .build();
        **/

        WebClient webClient = WebClient.create(TARGET_URI);

        Mono<String> response = webClient.get()
                                        .uri(uriBuilder -> uriBuilder.path("/replies/restTest")
                                        .queryParam("name","흑곰!!")      // 👍 해당 방식으로 파라미터 추가
                                        .build())
                                        .retrieve()
                                        .bodyToMono(String.class);
        log.info(response.subscribe());

    }

    //////////////////////////////////////////////////
    //Post Type
    //////////////////////////////////////////////////

    @Description("Post Type 요청 테스트")
    @Test
    public void postTypeTest(){
        //1 . WebClient 객체 생성
        //WebClient webclient = WebClient.create(TARGET_URI);

        //1 - 2 . WebClient 객체 생성 [ Builder 사용 ]
        // Header 설명 및 좀 더 디테일한 설정은 Builder 패턴을 사용해 주자
        WebClient webclientBuilder = WebClient.builder()
                .baseUrl(TARGET_URI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE
                                , MediaType.APPLICATION_JSON_VALUE) // Header 옵션 적용
                .build();


        //2 . body Data 생성
        ReplyDTO replyDto = ReplyDTO.builder()
                .bno(11L)
                .replyer("TestBlackGom")
                .text("What is ??").build();

        //3 . Post 방식 전송
        Mono<Long> response = webclientBuilder.post()
                                        .uri("/replies/")
                                        //.contentType(MediaType.APPLICATION_JSON) 요렇게도 가능함!
                                        .bodyValue(replyDto)        // Body Data  주입
                                        .retrieve()
                                        .bodyToMono(Long.class);

        // 💬 아래와 같은 방식으로도 Body데이터를 주입 가능하다.
        //.body(BodyInserters.fromFormData("id", idValue).with("pwd", pwdValue))

        response.subscribe(log::info);
    }


    //////////////////////////////////////////////////
    //PUT Type
    //////////////////////////////////////////////////

    @Description("PUT 방식 요청 테스트")
    @Test
    public void putTypeTest(){
        WebClient webClient = WebClient.builder()
                .baseUrl(TARGET_URI)
                .build();

        //body Data 생성
        ReplyDTO replyDto = ReplyDTO.builder()
                .bno(11L)
                .replyer("Modify!!!")
                .text("Is It working??").build();

        Mono<String> response = webClient.put()
                .uri("/replies/"+ 156L)
                .bodyValue(replyDto)
                .retrieve()
                .bodyToMono(String.class);

        log.info("--------------------");
        response.subscribe(log::info);
        log.info("--------------------");
    }


    //////////////////////////////////////////////////
    //DELETE Type
    //////////////////////////////////////////////////

    @Description("DELETE 방식 요청 테스트")
    @Test
    public void deleteTypeTest(){
        WebClient webClient = WebClient.builder()
                .baseUrl(TARGET_URI).build();

        Mono<String> response = webClient.delete()
                                .uri("/replies/{rno}",155L)
                                .retrieve()
                                .bodyToMono(String.class); // Void로 받을수 있음
        //subscribe를 해줘야 요청이 간다!!
        response.subscribe(log::info);
    }


    //////////////////////////////////////////////////
    //Error  처리
    //////////////////////////////////////////////////
    @Description("Error 핸들링")
    @Test
    public void exceptionHandlingTest(){

        WebClient webClient = WebClient.builder()
                .baseUrl(TARGET_URI)
                .build();

        Flux<ReplyDTO> fluxResult = webClient.get()
                .uri("/replies123/board/{bno}", 11L)
                .retrieve()
                // 👉 1 . onStatus를 통해 애러 컨트롤이 가능하다
                .onStatus(httpStatus    // 👉 2 . 에러가 있을시 케치
                            -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError()
                        , clientResponse // 👉 3 . 케치된 에러를 핸들링함 [ Function Type을 파라미터로 받음 ]
                                -> clientResponse.bodyToMono(String.class).map(body-> new RuntimeException(body)))
                .bodyToFlux(ReplyDTO.class);

        fluxResult.subscribe(log::info);
    }


    //////////////////////////////////////////////////
    //Collection으로 변환
    //////////////////////////////////////////////////
    @Description("Flux or Mono를 변환")
    @Test
    public void changeTypeTest(){
        WebClient webClient = WebClient.builder()
                .baseUrl(TARGET_URI)
                .build();

        // List 경우 [다건]
        List<ReplyDTO> response = webClient.get()
                .uri("/replies/board/{bno}", 11L)
                .retrieve()
                .bodyToFlux(ReplyDTO.class)
                .toStream() // 👉 stream으로 변환
                .collect(Collectors.toList()); // 👉 List로 변환

        log.info("result :: {}",response);


        //////////////////////////////////////////////////////////////////

        // Map 경우 [단건]
        ReplyDTO response2 = webClient.get()
                .uri("/replies/testReplyOne/{rno}", 140L)
                .retrieve()
                .bodyToMono(ReplyDTO.class)     // 👉 단건이므로 Mono Type
                .flux()                         // 👉 toStream()사용을 위해 flux로 변환
                .toStream()                     // 👉 stream으로 변환
                .findFirst()                    // 👉 첫번째 요소만 가져온다 -- Optional 타입임!
                .orElse(ReplyDTO.builder().build()); //  👉  없을경우 default Value 설정

        log.info("result :: {}",response2);

    }

    //////////////////////////////////////////////////
    // Synchronous Use - 기본적으로 WebClient는 비동기식이다!
    //////////////////////////////////////////////////
    @Description("동기식으로 처리하는 방법")
    @Test
    public void syncTest(){
        WebClient webClient = WebClient.builder()
                .baseUrl(TARGET_URI)
                .build();

        // List 경우 [다건]
        List<ReplyDTO> response = webClient.get()
                .uri("/replies/board/{bno}", 11L)
                .retrieve()
                .bodyToFlux(ReplyDTO.class)
                .collectList()
                .block();               // 👉 block()을 사용해주기만 해도 동기식으로 변경된다.
        /**
         * 여기서 주의깊게 봐야하는것은 collectList() 사용해서
         * 바로 List로 변경했다는 것이다!
         * 💬 block()을 사용해야지만 사용이 가능하다!!
         * */

        log.info("result :: {}",response);


        //////////////////////////////////////////////////////////////////

        // Map 경우 [단건]
        ReplyDTO response2 = webClient.get()
                .uri("/replies/testReplyOne/{rno}", 140L)
                .retrieve()
                .bodyToMono(ReplyDTO.class)
                .block();            // 👉 block()을 사용해주기만 해도 동기식으로 변경된다.

        log.info("result :: {}",response2);
    }
}

