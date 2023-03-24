package com.yoo.toy.security.handler;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description : Security Exception 발생시 처리하는
 *                Handler class
 *
 *               - 💬 인증 실패 시 실행될 로직을 담은 인터페이스이며, 이를 구현한 클래스를 직접 작성하여 사용해야 합니다.
 *                    예를 들어, 로그인 실패 시 로그를 남기거나, 실패 카운트를 증가시키는 등의 작업을 수행할 수 있습니다.
 * */
@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
                    , AuthenticationException exception) throws IOException, ServletException {

        log.info("Login Fail Handler!!!!");

        log.info("exception Mag ::: {}", exception.getMessage());

        // 반환할 JSON 데이터 생성
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Error
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JSONObject json = new JSONObject();
        json.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        json.put("msg", exception.getMessage());
        PrintWriter out = response.getWriter();
        out.println(json);

    }
}
