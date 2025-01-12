package com.yoo.webflux_study.controller;

import com.yoo.webflux_study.dto.ResDTO;
import com.yoo.webflux_study.service.AsyncSampleServiceImpl;
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
public class AsyncSampleController {

    private final AsyncSampleServiceImpl service;

    @GetMapping("/get-path-variable/{item}")
    public ResponseEntity<Mono<ResDTO>> getPathVariable(@PathVariable String item){
        return ResponseEntity.ok(service.getPathVariable(item));
    }
    // 2. GET Query Parameter
    @GetMapping("/get-query-param")
    public ResponseEntity<Mono<String>> getQueryParam(@RequestParam String param) {
        return ResponseEntity.ok(service.getQueryParam(param));
    }

    // 3. POST JSON Body
    @PostMapping("/post-process")
    public ResponseEntity<Mono<String>> postProcess(@RequestBody String data) {
        return ResponseEntity.ok(service.postProcess(data));
    }

    // 4. POST Multipart File
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<String>> uploadFile(@RequestPart MultipartFile file, @RequestPart String name) {
        return ResponseEntity.ok(service.uploadFile(file, name));
    }

}
