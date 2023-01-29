package com.yoo.ex04.service;

import com.yoo.ex04.dto.ReplyDTO;

import java.util.List;

public interface RestTemplateService {
    //GET 방식 요청 Mehtod - forEntity
    public String getHelloWorld();

    public String getHelloWordWithParam();

    public String getListByBoardVerStr();

    public List<ReplyDTO> getListByBoardVerLst();

    //GET 방식 요청 Mehtod - forObject
    public List<ReplyDTO> getListByBoardVerLstAndObject();

}
