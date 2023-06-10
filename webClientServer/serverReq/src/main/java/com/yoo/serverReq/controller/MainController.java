package com.yoo.serverReq.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping
public class MainController {

    @GetMapping("/hi")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello");
    }

}
