package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restCall")
@Log4j2
public class RestCallController {

    @GetMapping("/testView")
    public void viewCall(){
       log.info("call View!!");
    }

}
