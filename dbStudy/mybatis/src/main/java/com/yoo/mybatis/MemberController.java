package com.yoo.mybatis;

import com.yoo.mybatis.repository.MemberRepository;
import com.yoo.mybatis.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/member")
    public ResponseEntity<String> getMember(){
        List<MemberVO> resultList = memberRepository.getAllList();
        log.info("------------------");
        log.info(resultList);
        log.info("------------------");

        log.info("===============================");

        MemberVO resultOne = memberRepository.fineById("yoo");
        log.info("------------------");
        log.info(resultOne);
        log.info("------------------");

        return ResponseEntity.ok("success");
    }
}
