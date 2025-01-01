package com.yoo.p6spy.controller;

import com.yoo.p6spy.entity.Board;
import com.yoo.p6spy.entity.Member;
import com.yoo.p6spy.entity.Reply;
import com.yoo.p6spy.repository.BoardRepository;
import com.yoo.p6spy.repository.MemberRepository;
import com.yoo.p6spy.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class BoardController {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @GetMapping("/b1")
    ResponseEntity<String> get1(){
        boardRepository.findAll();
        return ResponseEntity.ok("success");
    }

    @GetMapping("/b2")
    ResponseEntity<String> get2(){
        Board board =  boardRepository.findById(1L)
                .orElseThrow();
        log.info(board.getWriter().getName());
        return ResponseEntity.ok("success");
    }

    @GetMapping("/r1")
    ResponseEntity<String> geRr1(){
        replyRepository.findAll();
        return ResponseEntity.ok("success");
    }

    @GetMapping("/r2")
    ResponseEntity<String> getR2(){
        Reply reply =  replyRepository.findById(1L)
                .orElseThrow();
        log.info(reply.getBoard());
        //log.info(reply.getBoard().getWriter());
        return ResponseEntity.ok("success");
    }
}
