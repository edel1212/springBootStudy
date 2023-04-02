package com.yoo.toy.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    // 비밀 인증 키
    private String secretKey = "edel1212@naver.com";

    // 기간 1달
    private long expire = 60 * 24 * 30;

    /**
     * claim() 사용 이유
     * - 사용자의 ID, 이름, 권한 등의 정보를 JWT 내부에 추가하고 싶다면,
     *   Jwts.builder().claim("key", value)와 같은 형식으로 정보를 추가할 수 있습니다.
     * - JWT의 활용성을 높이고, 보안성을 강화하기 위해 중요한 역할을 합니다.
     * */
    public String generateToken(String content) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())                                  // 발급 일시
                .setExpiration( Date.from(
                                        ZonedDateTime.now()
                                        .plusMinutes(expire)
                                        .toInstant()))                    // 만료 일자
                .claim("sub" , content)                             // JWT 내용 추가
                .signWith(SignatureAlgorithm.HS256
                            , secretKey.getBytes(StandardCharsets.UTF_8)) //H2565암호화 적용
                .compact();
    }

    public String validateAndExtract(String token) throws Exception{
        String contentValue = "";

        try {

        } catch (Exception ex){
            ex.printStackTrace();
        }
        return "TODO";
    }


}
