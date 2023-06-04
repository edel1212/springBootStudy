package com.yoo.loginServer.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    // secretKey
    private String secretKey = "aaabbbbzzzcccaaaasssxxxzzzzsssdddaaasss";

    // 만료기간 - 한달
    private long expire = 60 * 24 * 30;


    /**
     * @description Token 생성 Method
     * @param  content
     * @return String
     * */
    public String generateToken(String content) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())    // 발급일자
                .setExpiration( Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant())) // 만료일자
                .claim("sub", content)                                                    // 사용자 속성 - sub에 등록
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8)) // 암호화 방식, 비밀키 주입
                .compact();
    }

    /**
     * @description Token 값 확인
     * @param  tokenStr
     * @return String
     * */
    public String validateAndExtract(String tokenStr) throws Exception{
        String contentValue = "";

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.secretKey.getBytes(StandardCharsets.UTF_8)) // 비밀키 주입
                .build()
                .parseClaimsJws(tokenStr)                                       // 디코딩할 토큰값
                .getBody();

        log.info("claims ::: {}", claims);

        contentValue = claims.getSubject(); // 입력 받은 정보값을 "sub"에 넣엇기 떄문에 getSubject()를 사용

        log.info("claims getSubject :::{}", contentValue);

        return contentValue;
    }


}
