package com.yoo.exGraphQL.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/yoo")
@Controller
public class FooController {

    @GetMapping("/test")
    public void testView(){ }

}
