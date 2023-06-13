package com.yoo.serverReq.service;

import com.yoo.serverReq.dto.BaseWrapDTO;
import com.yoo.serverReq.dto.UpperDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
@Log4j2
public class UpperCamelServiceImpl implements UpperCamelService {

    @Override
    public List<UpperDTO> getList() {

        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

        BaseWrapDTO<UpperDTO> dto = webClient.get().uri("/upper").retrieve().bodyToMono(BaseWrapDTO.class).block();

        log.info("result ::: {}",dto.getResult().get(0).getFlagBool());

        return null;
    }

    @Override
    public UpperDTO getItem(Long code) {

        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

        UpperDTO result = webClient.get().uri("/upper/"+code).retrieve().bodyToMono(UpperDTO.class).block();

        log.info("result ::: {}",result);

        return result;
    }
}
