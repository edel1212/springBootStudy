package org.zerock.club.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {
    /**
     * SecurityConfig.class - @Configuration 클래스에서 만들어준
     * PasswordEncoder 를 @Bean 으로 DI에 주입해준것을 사용함.
     *
     * */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncode(){
        String password = "1111";

        String enPw = passwordEncoder.encode(password);

        System.out.println("enPw ::: " + enPw);

        boolean matchResult = passwordEncoder.matches(password , enPw);
        System.out.println("matchResult :: " + matchResult);
    }

}
