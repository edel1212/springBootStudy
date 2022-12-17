package org.zerock.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.service.NoteService;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/notes/")
public class NoteController {

    private final NoteService noteService;


    //등록
    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO){

        log.info("---------------------------------");
        log.info("Register ::" + noteDTO);

        Long num = noteService.register(noteDTO);

        HttpStatus status = num == 0 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;

        return new ResponseEntity<>(num, status);
    }

    //단건 조회
    @GetMapping(value = "/{num}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> read(@PathVariable Long num){
        log.info("----------------------");
        log.info("read Num :: " + num);
        return new ResponseEntity<>(noteService.get(num),HttpStatus.OK);
    }


    //다건 조회
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDTO>> getList(String email){
        log.info("------------getList------------");
        log.info("email ::: " + email);
        return new ResponseEntity<>(noteService.getAllWithWriter(email), HttpStatus.OK);
    }


    //수정
    @PutMapping(value = "/{num}" , produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@PathVariable Long num, @RequestBody NoteDTO noteDTO){

        log.info("------------modify--------");
        noteDTO.setNum(num);
        log.info("NoteDTO ::: " + noteDTO);

        noteService.modify(noteDTO);

        return new ResponseEntity<>("modify",HttpStatus.OK);
    }
    
    //삭제
    @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE) //반환 Type String 이기 때문임
    public ResponseEntity<String> remove(@PathVariable Long num){
        log.info("---------remove-----------");
        log.info("num ::: " + num);
        noteService.delete(num);
        return new ResponseEntity<>("removed",HttpStatus.OK);
    }


    //////////////////////////////////////////////////////////////////
    // produces 는 Client 단에서 Headers -> Accept 옵션을 줄 것이면 맞춰주자! [ 안써주면 반환값만 JSON 으로 맞춰주면 됨 ]
    @PostMapping(value = "stringTest" , produces = MediaType.APPLICATION_JSON_VALUE)  
    public String stringResponseTest(@RequestBody NoteDTO noteDTO){
        log.info(noteDTO);
        return "Hello!";
    }

    //__Eof__
}
