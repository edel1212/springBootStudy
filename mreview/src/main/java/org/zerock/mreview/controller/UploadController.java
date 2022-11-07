package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

@Controller
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}") // application.properties 에서 등록한(Bean) 값을 가져와 사용함
    private String uploadPath;

    @GetMapping("/uploadEx")
    public void uploadEx(){log.info("get uploadEx");}

    @PostMapping("/uploadAjax")
    public void uploadFile(MultipartFile[] uploadFiles){

        log.info("-----------------------");
        log.info(uploadFiles);
        log.info("-----------------------");

        for(MultipartFile uploadFile : uploadFiles){
            String originalFile = uploadFile.getOriginalFilename();
            String fileName = originalFile.substring(originalFile.lastIndexOf("\\")+1);

            log.info("fileName ::: " + fileName);
        }
        //TODO : template might not exist or might not be accessible by any of the configured Template Resolvers ---Error Check
    }

}
