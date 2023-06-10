package com.yoo.serverRes.controller;

import com.yoo.serverRes.dto.SensorDTO;
import jdk.jfr.Description;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
@Log4j2
@RequestMapping("/sensor")
public class IotController {

    @Description("단건")
    @GetMapping("/{serialNumber}")
    public ResponseEntity<SensorDTO> getSensor(@PathVariable String serialNumber){
        log.info("serialNumber ::: {}", serialNumber);

        Map<String, String> map = new HashMap<>();
        map.put("name","유정호");

        SensorDTO result = SensorDTO.builder()
                .serialNumber(serialNumber)
                .author("yoo")
                .dummyNumber(99L)
                .info(map)
                .build();

        return ResponseEntity.ok(result);
    }


    @Description("다건")
    @GetMapping
    public ResponseEntity<List<SensorDTO>> getSensorList(){
        List<SensorDTO> arr = new ArrayList<>();


        log.info("==================");
        log.info("==================");
        log.info("다건!!!!");
        log.info("==================");
        log.info("==================");

        Map<String, String> map = new HashMap<>();
        map.put("name","유정호");

        IntStream.rangeClosed(1, 100).forEach( idx ->
                arr.add(
                        SensorDTO.builder()
                                .author("yoo"+idx)
                                .dummyNumber(Long.valueOf(idx))
                                .info(map)
                                .serialNumber("zzzz"+idx)
                                .build()
                ));
        return ResponseEntity.ok(arr);
    }


    @Description("삭제")
    @DeleteMapping("/{serialNumber}")
    public ResponseEntity<String>  deleteSensor(@PathVariable String serialNumber){
        log.info("------------------------");
        log.info("삭제요청 serialNumber::: {}",serialNumber);
        return ResponseEntity.ok("삭제완료!");
    }

    @Description("수정")
    @PutMapping("/{serialNumber}")
    public ResponseEntity<String>  updateSensor(@PathVariable String serialNumber,@RequestBody SensorDTO sensorDTO){
        log.info("수정요청 serialNumber::: {}",serialNumber);
        log.info("수정요청 sensorDTO::: {}",sensorDTO);
        return ResponseEntity.ok("수정완료!");
    }


    @Description("등록")
    @PostMapping
    public ResponseEntity<String>  registerSensor(@RequestBody SensorDTO sensorDTO){
        log.info("생성요청 sensorDTO::: {}",sensorDTO);
        return ResponseEntity.ok("수정완료!");
    }

}
