package com.yoo.ex04.service;

import com.yoo.ex04.dto.ReplyDTO;
import com.yoo.ex04.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@Log4j2
public class RestTemplateServiceImpl implements RestTemplateService{

    private final String TARGET_URI = "http://localhost:8080";

    ///////////////// GET 방식 /////////////////

    /**
     * @Desription : 요청 시 parameter 를 사용하지 않는 형식
     * */
    @Override
    public String getHelloWorld() {

        URI uri = UriComponentsBuilder
                .fromUriString(TARGET_URI)
                .path("/replies/restTest")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        log.info("status code :: {}", responseEntity.getStatusCode());
        log.info("body :: {}", responseEntity.getBody());

        return responseEntity.getBody();
    }

    /**
     * @Desription : 요청 시 parameter 를 사용한 형식
     * */
    @Override
    public String getHelloWordWithParam() {
        URI uri = UriComponentsBuilder
                .fromUriString(TARGET_URI)
                .path("/replies/restTest")
                .queryParam("name","yoo!!")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        log.info("status code :: {}", responseEntity.getStatusCode());
        log.info("body :: {}", responseEntity.getBody());

        return responseEntity.getBody();
    }

    /**
     * @Desription : 요청 시 PathVariable을 사용한 방식
     * */
    @Override
    public String getListByBoardVerStr() {
        URI uri = UriComponentsBuilder
                .fromUriString(TARGET_URI)
                .path("/replies/board/{bno}")
                .encode()
                .build()
                .expand(90L)
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);


        log.info("status code :: {}", responseEntity.getStatusCode());

        log.info("body :: {}", responseEntity.getBody());

        return responseEntity.getBody();
    }

    // 배열로 받아와서 사용
    @Override
    public List<ReplyDTO> getListByBoardVerLst() {
        URI uri = UriComponentsBuilder
                .fromUriString(TARGET_URI)
                .path("/replies/board/{bno}")
                .encode()
                .build()
                .expand(90L)
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ReplyDTO[]> responseEntity = restTemplate.getForEntity(uri, ReplyDTO[].class );

        List<ReplyDTO> lst = List.of(responseEntity.getBody());

        log.info("status code :: {}", responseEntity.getStatusCode());

        log.info("body :: {}", lst);

        return lst;
    }

    @Override
    public List<ReplyDTO> getListByBoardVerLstAndObject() {
        URI uri = UriComponentsBuilder
                .fromUriString(TARGET_URI)
                .path("/replies/board/{bno}")
                .encode()
                .build()
                .expand(90L)
                .toUri();
        RestTemplate restTemplate = new RestTemplate();

        // 바로 Object 형태로 바로 가져오기에 ReplyDTO[]로 바로 받아서 사용이 가능함
        ReplyDTO[] result = restTemplate.getForObject(uri, ReplyDTO[].class );

        List<ReplyDTO> lst = List.of(result);

        return lst;
    }

    /**
     * POST 방식 [Insert]
     * */
    @Override
    public ResponseEntity<Long> replyRegister() {

        // 1. URI 객체 생성
        URI uri = UriComponentsBuilder
                .fromUriString(TARGET_URI)
                .path("/replies/")
                .encode()
                .build()
                .toUri();

        // 2. RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // 3 . Insert 를 위한 ReplyDTO 객체 생성
        //     - 해당 로직은 테스트 로직이기에 데이터를 받아오지 않고
        //       로직에서 Dummy data 생성
        ReplyDTO replyDTO = ReplyDTO.builder()
                .text("RestTemplateTest")
                .bno(101L)
                .replyer("guest1")
                .build();

        // 4 . postForEntity(URI , Parameter , 반환타입) 사용하여 POST 요청
        ResponseEntity<Long> result = restTemplate.postForEntity(uri,replyDTO, Long.class);

        return result;
    }

    ///////////////// POST 방식 /////////////////


    ///////////////// PUT 방식 /////////////////

    ///////////////// DELETE 방식 /////////////////

}
