package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.DateUtils;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}") // application.properties 에서 등록한(Bean) 값을 가져와 사용함
    private String uploadPath;

    @GetMapping("/uploadEx")
    public void uploadEx(){log.info("get uploadEx");}

    /**
     * ✔ 반환 값이 없을 경우
     * Error 발생 : Error resolving template [uploadAjax], template might not exist or might not be accessible by any of the configured Template Resolvers
     * */
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        //TODO Add List

        log.info("-----------------------");
        log.info(uploadFiles);
        log.info("-----------------------");

        for(MultipartFile uploadFile : uploadFiles){
            
            //.getContentType()를 사용하여 확장자를 체크가 가능함
            if(!Objects.requireNonNull(uploadFile.getContentType()).startsWith("image")){
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            
            //.getOriginalFilename() 에서는 IE 나 Edge 에서는 전체 경로가 들어오므로 잘라줌
            String originalFile = uploadFile.getOriginalFilename();
            assert originalFile != null;
            String fileName = originalFile.substring(originalFile.lastIndexOf("\\")+1);

            log.info("fileName ::: " + fileName);

            //날짜 폴더 생성
            String folderPath = makeFolder();

            String uuid = UUID.randomUUID().toString();
            
            //전체 파일 명 -> + UUID + 구분자 _ 사용
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);
            try {
                uploadFile.transferTo(savePath);
            }catch (Exception e){
                e.printStackTrace();
            }//try -catch

        }//end loop
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private String makeFolder(){
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        //Make folder
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(!uploadPathFolder.exists()){
            boolean success = uploadPathFolder.mkdirs();
            log.info(success);
        }

        return folderPath;
    }
}
