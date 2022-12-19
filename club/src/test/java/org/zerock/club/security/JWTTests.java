package org.zerock.club.security;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.club.security.util.JWTUtil;

@SpringBootTest
@Log4j2
public class JWTTests {

    /**
     * 해당 테스트는 BeanContainer 를 활용하지 않기에
     * JWTUtil를 사용하기 위해서
     * @BeforeEach 가 추가된 <b>testBefore</b> 메서드를 사용하여
     * 객체를 생성한 후 테스트를 진행해야한다
     * */
    private JWTUtil jwtUil;

    @BeforeEach
    public void testBefore(){
        log.info("testBefore!!!!");
        jwtUil = new JWTUtil();
    }

    //encode 변환 test
    @Test
    public void testEncode() throws Exception{
        String email = "user95@naver.com";

        String str = jwtUil.generateToken(email);
        log.info("jwtUil.generateToken(email) ::: -> " + str);
    }

    @Test
    public void testValidate() throws Exception{
        String email = "user95@naver.com";

        String str = jwtUil.generateToken(email);

        Thread.sleep(5_000); // 쓰레드 sleep 으로 지연시간 5초 [ jwt 인증시간 테스트용 ]
        /**
         * JWT 만료시간을 1초로 한 후 테스트시 Error 발생 -- 만료시간과 Thread sleep 5초로 인해 맞지 않기 때문
         *  Error ) JWT expired at 2022-12-19T13:28:43Z. Current time: 2022-12-19T13:28:47Z,
         *          a difference of 4614 milliseconds.  Allowed clock skew: 0 milliseconds.
         * */

        String resultEmail = jwtUil.validateAndExtract(str);
        log.info("resultEmail ::: " + resultEmail);
    }

    //__Eof__
}
