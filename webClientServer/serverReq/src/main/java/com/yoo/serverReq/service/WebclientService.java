package com.yoo.serverReq.service;

import com.yoo.serverReq.dto.SensorDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface WebclientService {

    Mono<SensorDTO> getSensor(String serialNumber);

    Flux<SensorDTO> getSensorList();

    void deleteSensor(String serialNumber);

    void updateSensor(SensorDTO sensorDTO);

    public void registerSensor(SensorDTO sensorDTO);

}
