package com.yoo.webflux_study.service;

import com.yoo.webflux_study.dto.ResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class SyncServiceImpl {

    private final WebClient webClient;

    public ResDTO getPathVariable(String variable) {
        try {
            // Synchronous blocking call
            return webClient
                    .get()
                    .uri("/path/{variable}", variable)
                    .retrieve()
                    .onStatus(
                            HttpStatus.INTERNAL_SERVER_ERROR::equals,
                            response -> response.bodyToMono(String.class).map(Exception::new))
                    .onStatus(
                            HttpStatus.BAD_REQUEST::equals,
                            response -> response.bodyToMono(String.class).map(Exception::new))
                    .bodyToMono(ResDTO.class)
                    // Error handling
                    .onErrorMap(error -> {
                        log.error("Mapping error for /path: {}", error.getMessage());
                        return new RuntimeException("Custom Exception: " + error.getMessage());
                    })
                    .onErrorResume(error -> {
                        log.error("Error during request to /path: {}", error.getMessage());
                        // Return default ResDTO in case of error
                        return Mono.just(new ResDTO("9999", "연계 에러 발생", "Error!!"));
                    })
                    .doOnTerminate(() -> log.info("Request to /path completed"))
                    // Block the Mono to get the result synchronously
                    .block();
        } catch (Exception e) {
            log.error("Error occurred during synchronous request: {}", e.getMessage());
            // Handle or propagate the exception as needed
            return new ResDTO("9999", "Unknown Error", "Error occurred");
        }
    }

}
