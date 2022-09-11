package org.zerock.ex01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    /**
     * @Description : - Spring Boot는  많은 설정이 없이 JSON Type의 데이터를 반환할때도
     *                  jackson-databind 라이브러리를 추가해 주지 않아도
     *                  Spring initializr에서 추가한 Spring Web의 의존성 항목을 추가하여
     *                  자동으로 추가되어 사용이 가능하다.
     *
     *                - @RestController를 이용하여 별도의 화면없이
     *                  해당 URL 이동 시 JSON type 데이터 전송 확인이 가능하다!
     * **/
    @GetMapping("/hello")
    public String[] hello(){
        return new String[]{"Hello","Worl"};
    }

}
