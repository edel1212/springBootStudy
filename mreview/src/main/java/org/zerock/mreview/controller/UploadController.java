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

    @Value("${org.zerock.upload.path}") // application.properties 에서 등록한 변수 값을 가져와 사용함
    private String uploadPath;  // 👉 "C:\\upload" 값이 들어가 있음

    @GetMapping("/uploadEx")
    public void uploadEx(){log.info("get uploadEx");}

    /**
     * 💬 반환 값이 없을 경우
     * Error 발생 : Error resolving template [uploadAjax], template might not exist or might not be accessible by any of the configured Template Resolvers
     * */
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        // 1 .  결과 값을 반환할 List<> 생성
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        log.info("-----------------------");
        log.info(uploadFiles);
        log.info("-----------------------");

        // 2 . File의 개수 만큼 Loop
        for(MultipartFile uploadFile : uploadFiles){
            
            // 2 - 1 . 👉 MultipartFile.getContentType()를 사용하여 확장자를 체크가 가능함
            if(!uploadFile.getContentType().startsWith("image")){
                log.warn("this file is not image type");
                //이미지가 아닐경우 403 Forbidden Error
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }//if
            
            // 2 - 2 . 👉 IE 나 Edge 에서는 전체 경로가 들어오므로
            //            MultipartFile.getOriginalFilename()를 사용하여
            //            파일명을 잘라서 사용
            String originalFile = uploadFile.getOriginalFilename();
            String fileName = originalFile.substring(originalFile.lastIndexOf("\\")+1);

            log.info("fileName ::: " + fileName);

            // 2 - 3 . 날짜 폴더 생성 :: 반환값 ? 오늘 날짜의 파일 경로
            String folderPath = this.makeFolder();

            // 2 - 4 . 파일명 중복방지를 위한 UUID 생성
            String uuid = UUID.randomUUID().toString();
            
            // 2 - 5 . 전체 파일 명 -> + UUID + 구분자 _ 사용 하여 👉 FullPath + FileName 생성
            // 💬 문자열 내용 :: RootPath + Dir 구분자 + 오늘 날짜 폴더 Dir + Dir 구분자
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            // 2 - 6 . 위에서 만든 FullPath 정보로 Path 객체 생성
            Path savePath = Paths.get(saveName);
            try {
                // 2 - 6 - 1 . 파일정보를 토대로 ==> FullPath로 변환(저장)
                uploadFile.transferTo(savePath);
                
                //썸네일 생성 Thumbnailator 사용
                //1) 파일명 생성 -- ✔ s_ 를 사용하여 구분함
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator
                        + "s_" + uuid + "_" + fileName;
                //2) File 객체 생성
                File thumbnailFile = new File(thumbnailSaveName);
                // Thumbnailator 를 사용하여 썸네일 생성 (File inFile, File outFile, width, height)
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100,100);

                // 2 - 6 - 2 . 파일 저장 결과를 DTO에 저장 후 List에 Add해줌
                resultDTOList.add(UploadResultDTO.builder().fileName(fileName).folderPath(folderPath).uuid(uuid).build());
            }catch (Exception e){
                e.printStackTrace();
            }//try -catch

        }//end loop

        return ResponseEntity.ok().body(resultDTOList);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> geFile(String fileName, String size){

        //return Data
        ResponseEntity<byte[]> result = null;

        try {
            //받아온 File src 를 decoding
            String srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

            log.info("fileName :: "+ srcFileName);

            /**
             * File.separator+ srcFileName   :: \2022\11\21/s_7b71fbdc-90dd-44e2-92ba-27a23e3597be_권정열-R10421.jpg
             * */
            File file = new File(uploadPath + File.separator+ srcFileName);

            log.info("file ::" + file);
            //파일의 Dir + 썸네일 경로
            
            /**
             * @Description : 원본 사이즈를 구하기 위한 로직
             *               Parameter인 Size의 유무체르로 구분한다
             * */
            if(size != null && size.equals("1")){
                /***
                 * file.getParent(),              :: 파일의 Dir 경로
                 * file.getName().substring(2)    :: 7b71fbdc-90dd-44e2-92ba-27a23e3597be_권정열-R10421.jpg
                 *
                 * @Description : .substring(2) 이유는 받다오는 이미지의 주소값은 썸네일의 주소값으로 항상
                 *                 _s 가붙어 있으므로 해당 앞부분을 제외하면 원본 이미지의 주소임!
                 *
                 */
                file = new File(file.getParent(), file.getName().substring(2));
                /*
                 * 파일의 Dir 경로 + 7b71fbdc-90dd-44e2-92ba-27a23e3597be_권정열-R10421.jpg
                **/
                //
            }

            //Header 객체 생성
            HttpHeaders headers = new HttpHeaders();
            
            /**
             * MIME타입 처리
             *
             * 해주는 이유❔
             *  - 파일의 확장자에따라 브라우저에 전송하는 MIME타입이 달려쟈아 하므로
             *
             *  Files.probeContentType❔
             *  - 해당 경로의 파일의 확장자를 확인함 단! 확인하지 못하면 null을 반환함
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

    //파일 삭제
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){
        String srcFileName = null;
        try {
            log.info("---------------------");
            log.info(fileName);
            log.info("---------------------");
            //받아온 파일정보를 decoding
            srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

            //Decoding 된 정보로 File 객체 생성
            File file = new File(uploadPath + File.separator + srcFileName);
            //파일 삭제
            boolean result = file.delete();

            //상단에서 가져온 File 객체를 활용하여 file의 Parent 경로 + "Thumbnail 구분자" + 파일명으로 File 객체 생성
            File thumbnail = new File(file.getParent(),"s_" + file.getName());

            log.info("--------thumbnail-------------");
            log.info(thumbnail);
            log.info("---------------------");

            //Thumbnail 삭제
            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    /**
     * @Description : 오늘 날짜로 Directory를 만는 Method
     *
     * */
    private String makeFolder(){
        // 1 . "yyyy/MM/dd" 패턴으로 현재 날짜를 받아옴
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // 2 . "/" 를 Replace하여 운영체제에 맞는 파일 경로로 변경함
        String folderPath = str.replace("/", File.separator);

        // 3 . File 객체 생성 ( RootDir , 오늘 날자경로 )
        File uploadPathFolder = new File(uploadPath, folderPath);

        // 4 . Server에 uploadPathFolder 객체의 정보에 맞는  Directory가 있는지 확인
        if(!uploadPathFolder.exists()){
            // 4 - 1 . 없을 경우 해당 경로에 맞는 Directory 생성
            boolean success = uploadPathFolder.mkdirs();
        }//if

        //파일 경로 반환
        return folderPath;
    }

    //__Eof__
}
