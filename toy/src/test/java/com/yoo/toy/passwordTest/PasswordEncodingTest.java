package com.yoo.toy.passwordTest;

import com.yoo.toy.entity.ClubMember;
import com.yoo.toy.repository.ClubMemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Log4j2
@SpringBootTest
public class PasswordEncodingTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Test
    public void passwordEncodeTests(){
        // set Password
        String password = "1111";
        // encode password - used Bcrypt
        String enPw = passwordEncoder.encode(password);

        log.info("enPw ::: {}",enPw);

        //match password : ( basePw , encodePw )
        boolean pwMatchResult = passwordEncoder.matches(password, enPw);

        log.info("pwMatchResult ::: {} ", pwMatchResult);

    }


    @Test
    public void findMatchPw(){
        Optional<ClubMember> member = clubMemberRepository.findByEmail("dbwjdghman93@gmail.com",true);

        if(member.isPresent()){
            log.info(member.get());

            ClubMember result = member.get();

            String pw = result.getPassword();

            boolean match = passwordEncoder.matches("1111",pw);

            log.info("match :: {}",m함atch);

        }

    }


}
