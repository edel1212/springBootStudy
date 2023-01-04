package com.yoo.toy.ca;

import com.yoo.toy.dto.PersonDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/t")
@Controller
@Log4j2
public class TestCt {

    @GetMapping({"/aa", "/bb", "/cc"}) // 다른 URL 이지만 한개의 controller에서 같은 로직으로 처리가 가능하다.
    public void helloView(Model model){
        log.info("!!!!");

        List<PersonDTO> list = IntStream.rangeClosed(1, 20).mapToObj(i ->{
            return PersonDTO.builder()
                    .name("흑곰"+ i)
                    .age(i)
                    .gender(i % 2 == 0)
                    .build();
        }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }

    @GetMapping("/yoo")
    public String stringUrlTest(){
        log.info("??!");
        return "/test";
    }

    @GetMapping("/yoo2")
    public String stringUrlTest2(){
        log.info("??!");
        return "/test/jh";
    }

}
