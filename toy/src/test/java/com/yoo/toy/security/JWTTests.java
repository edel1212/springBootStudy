package com.yoo.toy.security;

import com.yoo.toy.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 해당 테스트는 Spring의 Bean객체가 필요한 테스트가 아니므로
 * 내부에서 직접 JWT객체를 만들어서 사용함
 * **/
@Log4j2
public class JWTTests {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore(){
        log.info("testBefore......");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() throws Exception{
        String email = "edel1212@naver.com";

        // JWT 토큰 생성
        String str = jwtUtil.generateToken(email);

        log.info("str :::: {}",str);
    }

}
