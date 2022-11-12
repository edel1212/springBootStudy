package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.DateUtils;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        log.info("-----------------------");
        log.info(uploadFiles);
        log.info("-----------------------");

        for(MultipartFile uploadFile : uploadFiles){
            
            //.getContentType()를 사용하여 확장자를 체크가 가능함
            if(!Objects.requireNonNull(uploadFile.getContentType()).startsWith("image")){
                log.warn("this file is not image type");
                //이미지가 아닐경우 403 Forbidden Error
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
                uploadFile.transferTo(savePath); //실제 데이터를 저장하는 Logic
                
                //썸네일 생성 Thumbnailator 사용
                //1) 파일명 생성 -- ✔ s_ 를 사용하여 구분함
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator
                        + "s_" + uuid + "_" + fileName;
                //2) File 객체 생성
                File thumbnailFile = new File(thumbnailSaveName);
                // Thumbnailator 를 사용하여 썸네일 생성 (File inFile, File outFile, width, height)
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100,100);

                resultDTOList.add(UploadResultDTO.builder().fileName(fileName).folderPath(folderPath).uuid(uuid).build());
            }catch (Exception e){
                e.printStackTrace();
            }//try -catch

        }//end loop
        return new ResponseEntity<>(resultDTOList,HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> geFile(String fileName){

        //return Data
        ResponseEntity<byte[]> result = null;

        try {
            //받아온 File src 를 decoding
            String srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

            log.info("fileName :: "+ srcFileName);

            File file = new File(uploadPath + File.separator+ srcFileName);

            log.info("file ::" + file);
            
            //Header 객체 생성
            HttpHeaders headers = new HttpHeaders();
            
            /**
             * MIME타입 처리
             *
             * 해주는 이유❔
             *  - 파일의 확장자에따라 브라우저에 전송하는 MIME타입이 달려쟈아 하므로
             * */
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
                    headers,
                    HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
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

    //__Eof__
}
