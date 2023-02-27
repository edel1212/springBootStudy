package com.yoo.toy.securityRepository;

import com.yoo.toy.entity.ClubMember;
import com.yoo.toy.entity.ClubMemberRole;
import com.yoo.toy.repository.ClubMemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Description("Insert Dummy Member Date")
    public void insertClubMemberTest(){

        IntStream.rangeClosed(1,100).forEach(i->{
            ClubMember clubMember = ClubMember.builder()
                    .email("user"+i+"@naver.com")
                    .name("User"+1)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();
            //권한 추가 - 기본적으로 USER 권한을 줌
            clubMember.addMemberRole(ClubMemberRole.USER);

            if(i > 80){
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if(i > 90){
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            //Insert Member
            clubMemberRepository.save(clubMember);
        });

    }

    @Description("Email을 사용하여 회원 찾기")
    @Test
    public void findByEmailToUserTest(){
        Optional<ClubMember> result = clubMemberRepository
                .findByEmail("user100@naver.com",false);

        result.ifPresent(log::info);
    }


    @Description("Database Pw와 Encode Password Match Test")
    @Test
    public void matchTest(){
        Optional<ClubMember> result = clubMemberRepository
                .findByEmail("user100@naver.com",false);

        ClubMember clubMember = result.get();

        String pw = "1111";

        String encodePW = passwordEncoder.encode(pw);


        //boolean match = passwordEncoder.matches()



    }

}
