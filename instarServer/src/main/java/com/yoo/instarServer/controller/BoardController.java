package com.yoo.instarServer.controller;

import com.yoo.instarServer.service.BoardService;
import com.yoo.instarServer.vo.BoardVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private  final BoardService boardService;

    @GetMapping
    ResponseEntity<List<BoardVO>> getList(){
        List<BoardVO> result = boardService.getBoard();
        log.info(result);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<Integer> registerBoard(@RequestBody BoardVO boardVO){
        log.info("@@@@@");
        log.info(boardVO);
        log.info("@@@@@");
        int result = boardService.registerBoard(boardVO);
        log.info(result);
        return ResponseEntity.ok(result);
    }

}
