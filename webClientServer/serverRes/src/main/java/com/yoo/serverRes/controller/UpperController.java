package com.yoo.serverRes.controller;

import com.yoo.serverRes.dto.BaseWrapDTO;
import com.yoo.serverRes.dto.UpperDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@Log4j2
@RequestMapping("/upper")
public class UpperController {

    @GetMapping
    public ResponseEntity<BaseWrapDTO> getList(){



        List<UpperDTO> result = new ArrayList<>();

        IntStream.rangeClosed(1, 100).forEach(idx->{
            UpperDTO dto = UpperDTO.builder().SakuraNum("Hahahah"+ idx)
                    .FlagBool(idx%2 == 0)
                    .AppleID("BlackGom")
                    .NumberCd(Long.valueOf(idx))
                    .build();
            result.add(dto);
        });

        BaseWrapDTO dto = BaseWrapDTO.builder()
                .Method("Mmmmmm")
                .Result(result).build();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{code}")
    public ResponseEntity<UpperDTO> getItem(@PathVariable Long code){
        return ResponseEntity.ok(UpperDTO.builder().SakuraNum("Hahahah")
                .FlagBool(true)
                .AppleID("BlackGom")
                .NumberCd(code)
                .build());
    }

}
