package org.zerock.ex01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    /**
     * @Description :
     * **/
    @GetMapping("/hello")
    public String[] hello(){
        return new String[]{"Hello","Worl"};
    }

}
