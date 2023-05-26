package com.integrateVue.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class HomeController {

    @GetMapping(value = {"/", "/web/**"})
    public String mainPage(){
        // return "index.html";
        return "forward:/index.html";
    }

}
