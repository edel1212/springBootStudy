package com.yoo.serverReq.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Log4j2
public class SensorDTO {

    private String serialNumber;

    private Long dummyNumber;

    private Map<String, String> info;

    private String author;


    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("dog","black");
        SensorDTO sensorDTO = SensorDTO.builder()
                .serialNumber("eeee")
                .author("yoojh")
                .dummyNumber(1993L)
                .info(map)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(objectMapper.writeValueAsString(sensorDTO));
    }

}
