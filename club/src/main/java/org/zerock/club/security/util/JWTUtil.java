package org.zerock.club.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private final String secretKey = "yoo12345678";

    /**
     * JWT 버전이 변경되면서 Key 타입으로 파라미터를 넣워줘야하기에
     *  사용된 메서드
     * */
    private Key getSignKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * @Description : JWT 토큰을 생성하는 Method
     *              - JWT 문자열을 자체를 알면 누구든 APU 를 사용할수 있는 문제가 있기에
     *                만료기간을 설정함 [expire]값
     * */
    public String generateToken(String content) throws Exception{

        //1MONTH
        long expire = 60 * 24 * 30;

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant())) //유지기간
                .claim("sub",content)
                .signWith(getSignKey(this.secretKey),SignatureAlgorithm.HS256)                  //내가 설정한 Key 값으로 Signature 생성
                .compact();
    }

    /**
     * @Description : JWT 문자열을 검증하는 역할을 하는 Method
     * */
    public String validateAndExtract(String tokenStr) throws Exception{
        String contentValue = null;

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(tokenStr)
                    .getBody();
            log.info("Claims [Before - DefaultJws]::: " + claims);

            contentValue = claims.getSubject();
        } catch (Exception ex){
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
        return contentValue;
    }

    //__Eof__
}
