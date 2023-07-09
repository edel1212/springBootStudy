package com.yoo.statistics.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class HeadquartersDTO {
    private String buildCode;

    // 건물명
    private String buildName;

    // 파일 위치
    private String filePath;

    // 확장자
    private String fileExtension;


    public static void main(String[] args) throws JsonProcessingException {
        HeadquartersDTO dto = HeadquartersDTO.builder()
                .buildCode("aaa")
                .buildName("우리집")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        // {"buildCode":"aaa","buildName":"우리집","filePath":null,"fileExtension":null}
       log.info(objectMapper.writeValueAsString(dto));
    }
}
