package com.yoo.toy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : Security Success 시 처리하는
 *                Handler class
 *
 *                👉 Failure Handler 와 다르게 SuccessHandler는 Interface이다!
 * */
@Component
@Log4j2
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {

        log.info("Success!!");

        //TODO Business logic [ 비밀번호 오류 횟수 초기화등 여러가지 로직이 처리 가능하다. ]

        log.info("login user authentication :: " + authentication.getAuthorities());

        Map<String, String> result = new HashMap<>();
        result.put("status","200");
        result.put("Msg","Success");

        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(result));

        /**
         * 해당 방법으로 리다이렉트 이동이 불가능하다.
         * 💬 Login 로직을 비동기 방식으로 진행했기에
         * 404 에러가 떨어짐 URL 이동은 스크립트로 처리가 필요하다
         * */
        //response.sendRedirect("/sample/member");

    }
}
