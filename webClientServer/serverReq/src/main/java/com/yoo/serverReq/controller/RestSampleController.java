package com.yoo.serverReq.controller;

import jdk.jfr.Description;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/rest")
public class RestSampleController {

    /**
     * produces - client단 Accept
     * 👉 반환 데이터 타입을 지정해주는것
     * */

    @Description("반환값 과 produces 데이터 형식이 같음")
    @GetMapping(value = "/string", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getStringSuccess(){
        return ResponseEntity.ok("hi");
    }

    @Description("반환값과 다르게 설정 - Error : Resolved [org.springframework.http.converter.HttpMessageNotWritableException: No converter for [class java.util.HashMap] with preset Content-Type 'null']")
    @GetMapping(value = "/stringError", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Map<String,String>> getStringError(){
        Map<String, String> map = new HashMap<>();
        map.put("name", "yoo");
        return ResponseEntity.ok(map);
    }

    @Description("에러가 없음")
    @GetMapping(value = "/stringSuc", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,String>> getStringSuc(){
        Map<String, String> map = new HashMap<>();
        map.put("name", "yoo");
        return ResponseEntity.ok(map);
    }


    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * consumes - client단 Content-Type
     * 👉 Body의 데이터 타입을 지정
     * */

    @Description("요청값과 consumes가 같음")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postReqSuc(@RequestBody Map<String, String> map){
        log.info("abc:::: {}",map);
        return ResponseEntity.ok("abc");
    }

    @Description("요청값과 consumes가 다름 - Error : Resolved [org.springframework.web.HttpMediaTypeNotSupportedException: Content type 'application/json' not supported]")
    @PostMapping(value = "/register2", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> postReqErr(@RequestBody String abc){
        log.info("abc:::: {}",abc);
        return ResponseEntity.ok(abc);
    }

}
