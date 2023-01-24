package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Member;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .email("user"+i+"@naver.com")
                    .password("111")
                    .name("Yoo"+i+"입니다.")
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    public void findUser(){
        List<Member> result = memberRepository.findAll();
        result.forEach(log::info);
    }

}
