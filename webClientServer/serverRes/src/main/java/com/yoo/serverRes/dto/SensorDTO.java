package com.yoo.serverRes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SensorDTO {

    private String serialNumber;

    private Long dummyNumber;

    private Map<String, String> info;

    private String author;

}
