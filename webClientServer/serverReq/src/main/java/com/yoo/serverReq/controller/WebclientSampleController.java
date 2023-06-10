package com.yoo.serverReq.controller;

import com.yoo.serverReq.dto.SensorDTO;
import com.yoo.serverReq.service.WebclientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webclient")
@Log4j2
@RequiredArgsConstructor
public class WebclientSampleController {

    private final WebclientService webclientService;

    @GetMapping("/{serialNumber}")
    public ResponseEntity<Mono<SensorDTO>> getSensor(@PathVariable String serialNumber){
        Mono<SensorDTO> result = webclientService.getSensor(serialNumber);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Flux<SensorDTO>> getSensorList(){
        Flux<SensorDTO> result = webclientService.getSensorList();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{serialNumber}")
    public ResponseEntity<String> deleteSensor(@PathVariable String serialNumber){
        webclientService.deleteSensor(serialNumber);
        return ResponseEntity.ok("del!!");
    }

    @PutMapping("/{serialNumber}")
    public ResponseEntity<String> updateSensor(@PathVariable String serialNumber,@RequestBody SensorDTO sensorDTO){
        webclientService.updateSensor(sensorDTO);
        return ResponseEntity.ok("update!!!!!");
    }

    @PostMapping
    public ResponseEntity<String> registerSensor(@RequestBody SensorDTO sensorDTO){
        webclientService.registerSensor(sensorDTO);
        return ResponseEntity.ok("register!!!!!");
    }

}
