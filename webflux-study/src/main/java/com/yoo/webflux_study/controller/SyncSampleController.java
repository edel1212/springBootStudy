package com.yoo.webflux_study.controller;

import com.yoo.webflux_study.dto.ResDTO;
import com.yoo.webflux_study.service.AsyncSampleServiceImpl;
import com.yoo.webflux_study.service.SyncServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@RestController
public class SyncSampleController {

    private final SyncServiceImpl service;

    @GetMapping("/sync/get-path-variable/{item}")
    public ResponseEntity<ResDTO> getPathVariable(@PathVariable String item){
        return ResponseEntity.ok(service.getPathVariable(item));
    }


}
