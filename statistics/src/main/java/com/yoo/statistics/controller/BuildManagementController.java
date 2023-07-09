package com.yoo.statistics.controller;

import com.yoo.statistics.dto.HeadquartersDTO;
import com.yoo.statistics.service.HeadquartersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/build")
@RequiredArgsConstructor
@Log4j2
public class BuildManagementController {

    private final HeadquartersService headquartersService;

    // @RequestPart는 HTTP request body에 multipart/form-data 가 포함되어 있는 경우에 사용하는 어노테이션이다.
    // headquartersDTO <<- 해당 부분은 application-json 설정 필요
    @PostMapping("/headquarters")
    public ResponseEntity<String> registerHeadquarters(@RequestPart HeadquartersDTO headquartersDTO, @RequestPart MultipartFile uploadFile){
        headquartersService.registerHeadquarters(headquartersDTO, uploadFile);
        return ResponseEntity.ok("success");
    }

}
