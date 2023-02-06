package com.yoo.exGraphQL.service;

import com.yoo.exGraphQL.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.List;

@SpringBootTest
@Log4j2
public class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Description("get List Test")
    @Test
    public void getListTest(){
        log.info("get List!!");
        List<Member> result = memberService.getList();
        result.forEach(log::info);
    }

}
