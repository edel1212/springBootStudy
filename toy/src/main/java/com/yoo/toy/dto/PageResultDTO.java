package com.yoo.toy.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class PageResultDTO <DTO, EN>{ // Generic으로 DTO와, Entuty를 받음 

    private List<DTO> dtoList;
    
    //생성자로는 Page 와 처리할 Function을 매개변수로 받는다
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());
    }


}
