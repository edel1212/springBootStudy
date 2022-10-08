package com.yoo.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description  : 해당 Class 는 JPA를 이용하는 Repository에서  페이지 처리결과를 Page<Entity>로 처리결괄르 받게하기
 *                 위한 Class 이다 서비스 계층에서 이를 처리하기 위해 별도의 클래스를 만들어 사용하는것
 *                 
 *                 - Page<Entity>의 엔티티 객체들의 DTO 객체로 변환해서 자료구조로 담아줘야함
 *                 - 화면에 출력에 필요한 페이지 정보들을 구성해줘야함
 * */
@Data
public class PageResultDTO<DTO, EN> {

    private List<DTO> dtoList;

    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());
    }

}
