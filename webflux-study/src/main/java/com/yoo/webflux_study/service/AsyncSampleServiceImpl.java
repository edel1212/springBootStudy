package com.yoo.webflux_study.service;

import com.yoo.webflux_study.dto.ResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class AsyncSampleServiceImpl {

    private final WebClient webClient;

    public Mono<ResDTO> getPathVariable(String variable) {
        return webClient
                .get()
                .uri("/path/{variable}", variable)
                .retrieve()
                /**
                 * ✅ 상태코드에 맞는 처리 -> 받은 값 그대로 넘겨줌
                 *     - 해당 상태값에 맞는 처리리된 데이터는 예외로 변경되어 던지지고 하단 에러 처리 부분에 원하는 방식으로 차리가 가능하다.
                 * */
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(ResDTO.class)
                /**
                 * ✅ 에러 처리 방법 : 순차적 실행 원하는 처리 방법만 골라 지정 필요
                 *    - onErrorMap
                 *    - onErrorResume
                 *    - doOnError
                 * */
                // 사용하면 발생한 에러를 다른 유형의 에러로 변환하여 반환
                .onErrorMap(error -> {
                    log.error("Mapping error for /path: {}", error.getMessage());
                    return new RuntimeException("Custom Exception: " + error.getMessage());
                })
                // 에러 발생 시 대체 데이터를 반환하거나 다른 흐름으로 복구할 수 있습니다.
                .onErrorResume(error -> {
                    log.error("Error during request to /path: {}", error.getMessage());
                    // 대체 데이터를 반환
                    return Mono.just(new ResDTO("9999","연계 에러 발생","Error!!"));
                })
                // 에러가 발생했을 때만 특정 작업을 수행
                .doOnError(error -> log.error("Error occurred during request to /path: {}", error.getMessage()))
                /**
                 * ✅ 무조건 처리되는 메서드
                 *    - doOnTerminate
                 * */
                // 스트림이 어떻게 종료되었는지에 상관없이 무조건 실행되는 종료 시점 처리용 메서드로, 주로 로그 남기기, 리소스 정리, 모니터링 같은 작업에 활용됩니다.
                .doOnTerminate(() -> log.info("Request to /path completed"))

                ;
    }

    public Mono<String> getQueryParam(String param) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/query")
                        .queryParam("param", param)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.info("Request to /query completed"));
    }

    public Mono<String> postProcess(String data) {
        return webClient
                .post()
                .uri("/process")
                //.bodyValue(new DataRequest(data))
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.info("Request to /process completed"));
    }

    public Mono<String> uploadFile(MultipartFile fileData, String name) {

        // MultipartBodyBuilder 대신 MultiValueMap 사용 가능
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();

        // MultipartFile을 body에 추가
        bodyMap.add("file", fileData.getResource()); // fileData 자체를 사용
        bodyMap.add("additionalData", name); // additionalData는 단순 String

        return webClient
                .post()
                .uri("/upload")
                .bodyValue(bodyMap)  // 멀티파트 본문 전송
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .doOnTerminate(() -> log.info("Request to /upload completed"))
                // onStatus에서 발생한 에러를 해당 onErrorResume에서 캐치 후 처리할 수 있음
                .onErrorResume(e -> {
                    log.error("Error occurred: {}", e.getMessage());
                    return Mono.just(e.getMessage()); // Return the error message as a string
                })
                ;
    }
}
