package com.yoo.ex04.restTempateSendTest;

import com.yoo.ex04.dto.ReplyDTO;
import com.yoo.ex04.service.RestTemplateService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.List;

@SpringBootTest
@Log4j2
public class RestTemplateSendTest {

    @Autowired
    private RestTemplateService restTemplateService;

    @Description("GET 방식 Send")
    @Test
    public void sendTestGetType1(){
        String result = restTemplateService.getHelloWorld();
        log.info("result ::: {}",result);
    }

    @Description("GET 방식 파라미터 추가 Send")
    @Test
    public void sendTestGetType2(){
        String result = restTemplateService.getHelloWordWithParam();
        log.info("result ::: {}", result);
    }

    @Description("String Type로 넘어 온다!")
    @Test
    public void sendTestGetType3(){
        String result = restTemplateService.getListByBoardVerStr();
        log.info("result ::: {}", result);
    }

    @Description("Service Logic에서 변환하여 전달해줌")
    @Test
    public void sendTestGetType4(){
        List<ReplyDTO> result = restTemplateService.getListByBoardVerLst();
        result.forEach(log::info);
    }
}