package com.yoo.ex04.service;

import com.yoo.ex04.dto.ReplyDTO;
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
        ReplyDTO[] responseEntity = restTemplate.getForObject(uri, ReplyDTO[].class );

        List<ReplyDTO> lst = List.of(responseEntity);

        return lst;
    }


}