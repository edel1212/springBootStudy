package com.yoo.ex04.cotroller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/test/")
public class RestTestViewController {

    @GetMapping("foo")
    public void goView(){

    }
}
