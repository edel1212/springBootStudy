package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.BoardService;
import org.zerock.board.service.ReplyService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/replies/")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    private final BoardService boardService;

    @GetMapping(value = "/board/{bno}" // PathVariable 사용
    , produces = MediaType.APPLICATION_JSON_VALUE) //반환 타입은 JSON 명시
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno")Long bno){
        log.info("bno :: " + bno);
        return new ResponseEntity<>(replyService.getList(bno),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
        Long newRno = replyService.register(replyDTO);
        return new ResponseEntity<>(newRno,HttpStatus.OK);
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno")Long rno){
        log.info("rno :: " + rno);

        replyService.remove(rno);

        return new ResponseEntity<>("success",HttpStatus.OK);

    }

    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@PathVariable Long rno,@RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno);
        log.info(replyDTO);
        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }

    //////////////////////////
    // ex04에서 요청할 RestTemplate 테스트용 URL

    /**
     * 알게된 새로운 사실 파리미터인 name을 받을 때
     * @ReqeustParam을 사용할시 해당 name 파라미터가 강제됨 값이 없을 경우 에러를 반환함.
     * -> 기본 설정이 필수값으로 설정 되어 있기  때문임.
     *
     * ex) (@RequestParam String name)
     *     -> http://localhost:8080/replies/restTest?name=yoo  :: 이상없음
     *     -> http://localhost:8080/replies/restTest           :: 에러 발생☠️
     *      Error Code : 400 [bad Request]
     *      Error Msg  : Required request parameter 'name' for method
     *                   parameter type String is not present
     * 해결방안 : 1) required = boolean  옵션을 사용하여 필수 요소를 지정이 가능하다.
     *             - (@RequestParam(required = false) String name )
     *         2) defaultValue = "??"  지정이 가능하다.
     *             - (@RequestParam(required = false
     *                            , defaultValue = "흑곰" ) String name )
     * */
    @GetMapping("/restTest")
    public String getStringWithRestTempTest(@RequestParam(required = false) String name ){

        log.info("name ::: {} ", name);

        String result =
                name != null ? "Hello " + name + " World" : "Hello World";

        return result;
    }

    @Description("Mono Test : Get-Type API __ 단건")
    @GetMapping("/testReplyOne/{rno}")
    public ResponseEntity<ReplyDTO> getReply(@PathVariable Long rno){
        ReplyDTO result = replyService.getReply(rno);
        log.info("response data :: {}", result);
        return ResponseEntity.ok().body(result);
    }

}
