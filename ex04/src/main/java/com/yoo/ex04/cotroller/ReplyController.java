package com.yoo.ex04.cotroller;

import com.yoo.ex04.dto.ReplyDTO;
import com.yoo.ex04.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Controller  ❌ @RestController 사용시 필요가 없음
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/replies/")
public class ReplyController {

    private final ReplyService replyService;

    //produces Test

    /*
    * 반환 타입과 produces 설정 또한 맞지 않음
    * */
    @Description("반환 타입과 produces가 맞지 않기에 500Error 반환")
    @Deprecated
    @GetMapping(value = "/errorCase/{bno}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<List<ReplyDTO>> producesErrorCase(@PathVariable Long bno){
        log.info("bno ::: {}" , bno);
        return ResponseEntity.ok().body(replyService.getList(bno));
    }

    @Description("Error는 없지만 Client단에서의 모순이 있음")
    @Deprecated
    @GetMapping(value = "/errorCase2/{bno}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> producesErrorCase2(@PathVariable Long bno){
        log.info("bno ::: {}" , bno);
        return ResponseEntity.ok().body("Yoo");
    }

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable Long bno){
        log.info("bno ::: {}" , bno);
        return ResponseEntity.ok().body(replyService.getList(bno));
    }

    ////////////////////////////////////////////////////////////////////

    //consumes Test

    @Description("Error Case Get방식은 Body가 없으므로 consumes가 불필요함")
    @Deprecated
    @GetMapping(value = "/consumesErrorCase1", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> errorCase1(@RequestBody Map<String, String> testValue){

        log.info("testValue :: {}", testValue);
        return ResponseEntity.ok().body("ErrorCase");
    }

    @PostMapping(value = "/consumesSuccess", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> errorCase2(@RequestBody Map<String, String> testValue){

        log.info("testValue :: {}", testValue);

        Map<String , String > result = new HashMap<>();
        result.put("result","SUCCESS");

        return ResponseEntity.ok().body(result);
    }

    ////////////////////////////////////////////////////////////////////

    // application - Type Test

    @Description("URL에 값이 담겨나옴")
    @GetMapping(value = "/formVer")
    public ResponseEntity<List<ReplyDTO>> applicationFormVerTest(Long testValue){
        log.info("bno ::: {}" , testValue);
        return ResponseEntity.ok().body(replyService.getList(testValue));
    }

    @Description("DTO에 값이 담기는지 확인")
    @GetMapping(value = "/formVer2")
    public ResponseEntity<List<ReplyDTO>> applicationFormVerTest(ReplyDTO replyDTO){
        log.info("bno ::: {}" , replyDTO);
        return ResponseEntity.ok().body(replyService.getList(replyDTO.getBno()));
    }

    /**
     * Parameter를 (Long testValue) 받았을 시 이상없음 확인 완료
     * */
    @PostMapping(value = "/formAndPostVer")
    public ResponseEntity<List<ReplyDTO>> applicationFormAndPostVerTest(ReplyDTO replyDTO){
        log.info("bno ::: {}" , replyDTO);
        return ResponseEntity.ok().body(replyService.getList(replyDTO.getBno()));
    }
}
