package com.yoo.toy.passwordTest;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
@SpringBootTest
public class PasswordEncodingTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

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


}
