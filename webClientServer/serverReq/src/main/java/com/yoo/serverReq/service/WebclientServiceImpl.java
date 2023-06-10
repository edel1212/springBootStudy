package com.yoo.serverReq.service;

import com.yoo.serverReq.dto.SensorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class WebclientServiceImpl implements WebclientService{


    @Override
    public Mono<SensorDTO> getSensor(String serialNumber) {
        WebClient client = WebClient.create("http://localhost:8080");
        SensorDTO dto = client.get()
                .uri("/sensor/" + serialNumber)
                .retrieve()
                .bodyToMono(SensorDTO.class)
                .flux()
                .toStream()
                .findFirst()
                .orElse(SensorDTO.builder().build());

        log.info("이런식으로 변경도 가능해!! :::{}",dto.getInfo());

        return WebClient.builder().baseUrl("http://localhost:8080").build()
                .get()
                .uri("/sensor/" + serialNumber)
                .retrieve()
                .bodyToMono(SensorDTO.class);
    }


    @Override
    public Flux<SensorDTO> getSensorList() {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();
        Flux<SensorDTO> flux = client.get().uri("/sensor").retrieve().bodyToFlux(SensorDTO.class);
        return flux;
    }

    @Override
    public void deleteSensor(String serialNumber) {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();

        client.delete()
                .uri("/sensor/" + serialNumber)
                .exchangeToMono(response -> {
                    HttpStatus status = response.statusCode();
                    if (status.is2xxSuccessful()) {
                        log.info("성공 로직");
                        return Mono.empty(); // 빈 Mono 반환
                    } else {
                        log.info("실패 :: {}", status);
                        return response.bodyToMono(String.class); // 실패 응답 본문을 Mono<String>으로 변환하여 반환
                    }
                })
                .subscribe(log::info); //   비워도 OK
    }

    @Override
    public void updateSensor(SensorDTO sensorDTO) {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();
        client.put()
                .uri("/sensor/" + sensorDTO.getSerialNumber())
                .bodyValue(sensorDTO)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(log::info);
    }

    @Override
    public void registerSensor(SensorDTO sensorDTO) {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();
        client.post()
                .uri("/sensor")
                .bodyValue(sensorDTO)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(log::info);
    }

    //@Scheduled(cron = "0/5 * * * * ?")
    public void test(){
        WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();
        Flux<SensorDTO> flux = client.get().uri("/sensor").retrieve().bodyToFlux(SensorDTO.class);
        // 👉 flux 객체를 사용해야지 기동된다.
        flux.subscribe(log::info);
    }
}
