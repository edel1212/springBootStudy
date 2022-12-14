package org.zerock.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.service.NoteService;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/notes/")
public class NoteController {

    private final NoteService noteService;

    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO){

        log.info("---------------------------------");
        log.info("Register ::" + noteDTO);

        Long num = noteService.register(noteDTO);

        HttpStatus status = num == 0 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;

        return new ResponseEntity<>(num, status);
    }

    //__Eof__
}
