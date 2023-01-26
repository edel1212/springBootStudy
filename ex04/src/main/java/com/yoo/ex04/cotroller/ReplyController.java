package com.yoo.ex04.cotroller;

import com.yoo.ex04.dto.ReplyDTO;
import com.yoo.ex04.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller  ❌ @RestController 사용시 필요가 없음
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/replies/")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable Long bno){
        log.info("bno ::: {}" , bno);
        return ResponseEntity.ok().body(replyService.getList(bno));
    }

    @PostMapping(value = "/consumesSuccess", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> consumesTest(){
        return ResponseEntity.ok().body("success");
    }

}
