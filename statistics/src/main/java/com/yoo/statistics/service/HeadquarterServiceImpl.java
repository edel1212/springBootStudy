package com.yoo.statistics.service;

import com.yoo.statistics.dto.HeadquartersDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Log4j2
public class HeadquarterServiceImpl implements HeadquartersService{

    @Value("${test.value}")     // application.properties 에서 등록한 변수 값을 가져와 사용함
    private String uploadPath;  // 👉 "C:\\upload" 값이 들어가 있음

    @Override
    public void registerHeadquarters(HeadquartersDTO headquartersDTO, MultipartFile uploadFile) {


        if(uploadFile != null){

            // 확장자 체크
//            if(!uploadFile.getContentType().startsWith("image")){
//                log.warn("this file is not image type");
//                //이미지가 아닐경우 403 Forbidden Error
//                return;
//            }//if

            String originalFile = uploadFile.getOriginalFilename();
            String fileName = originalFile.substring(originalFile.lastIndexOf("\\")+1);


            // 폴더명
            String folderPath = this.makeFolder(headquartersDTO);

            String saveName = uploadPath + File.separator + folderPath + File.separator + fileName;

            Path savePath = Paths.get(saveName);

            log.info("확장자명 ::: {}", uploadFile.getContentType());
            log.info("fileName ::: {}" , fileName);
            log.info("파일저장된 전체 경로 :: {}",saveName);

            try {
                uploadFile.transferTo(savePath);
            }catch (Exception e){
                e.printStackTrace();
            }//try -catch

        }//if

    }

    // 폴더 생성
    private String makeFolder(HeadquartersDTO headquartersDTO){
        // 받아온 기관 Code를 사용 폴더명 생성
        String folderPath = headquartersDTO.getBuildCode();

        // 폴더 생성
        File uploadPathFolder = new File(uploadPath, folderPath);

        // 해당 경로에 Folder가 없을 경우 생성
        if(!uploadPathFolder.exists()){
            uploadPathFolder.mkdirs();
        }//if

        return folderPath;
    }
}
