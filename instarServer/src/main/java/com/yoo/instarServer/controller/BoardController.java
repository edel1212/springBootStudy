package com.yoo.instarServer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("list")
public class BoardController {

    @GetMapping
    ResponseEntity<List<Integer>> getList(){
        List<Integer> reulst = new ArrayList<>();
        reulst.add(1);
        reulst.add(1);
        return ResponseEntity.ok(reulst);
    }

}
