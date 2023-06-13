package com.yoo.serverReq.service;

import com.yoo.serverReq.dto.UpperDTO;

import java.util.List;

public interface UpperCamelService {

    List<UpperDTO> getList();

    UpperDTO getItem(Long code);

}
