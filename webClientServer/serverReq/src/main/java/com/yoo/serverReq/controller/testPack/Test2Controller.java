package com.yoo.serverReq.controller.testPack;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/king")
@Log4j2
public class Test2Controller {

    @GetMapping("/hah2")
    public ResponseEntity<Boolean> test(boolean  flag){
        log.info("!!!!!!!{}",flag);
        return ResponseEntity.ok(flag);
    }

    @PostMapping("/hah3")
    public ResponseEntity<Map<String,String>> test2(@RequestBody Map<String,String> tt){
        log.info("!!!!!!!{}",tt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(tt);
    }

}
