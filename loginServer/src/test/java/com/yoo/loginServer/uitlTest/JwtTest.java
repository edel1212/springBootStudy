package com.yoo.loginServer.uitlTest;

import com.yoo.loginServer.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Log4j2
public class JwtTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void beforeMethod(){
        this.jwtUtil = new JWTUtil();
    }


    @Test
    public void generateToken() throws Exception {
        String email = "edel1212@naver.com";
        String token = jwtUtil.generateToken(email);
        log.info("token ::: {}", token);
    }


    @Test
    public void validateCheck() throws Exception{
        String email = "edel1212@naver.com";
        String token = jwtUtil.generateToken(email);

        log.info("token ::: {}", token);

        String content = jwtUtil.validateAndExtract(token);

        log.info("content :: {}", content);

    }

}
