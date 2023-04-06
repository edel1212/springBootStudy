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


    // JWT 인코딩 테스트
    @Test
    public void testEncode() throws Exception{
        String email = "edel1212@naver.com";

        // JWT 토큰 생성
        String str = jwtUtil.generateToken(email);

        log.info("str :::: {}",str);
        //str :::: eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2ODA3ODY1MjgsImV4cCI6M
        // TY4MzM3ODUyOCwic3ViIjoiZWRlbDEyMTJAbmF2ZXIuY29tIn0.
        // MKcyOwYZSUF7exYfMLnbnpk9JyuwqndjJMZMNPw6AbM
    }


    // JWT 기간 만료 테스트
    @Test
    public void testValidateTest() throws Exception{
        String email = "eedel1212@naver.com";

        // email을 사용하여 Jwt Token 생성
        String str = jwtUtil.generateToken(email);

        Thread.sleep(5,000);
        /**
         * 지연 테스트 시 만료기간이 지나면 Error 발생
         * - Error Msg : JWT expired at 2023-04-06T13:30:28Z. Current time: 2023-04-06T13:30:28Z
         *                  , a difference of 335 milliseconds.  Allowed clock skew: 0 milliseconds.
         * */

        // 생성된 Jwt를 사용하여 값 확인
        String resultEmail = jwtUtil.validateAndExtract(str);

        log.info("result Email :: {}",resultEmail);

    }

}
