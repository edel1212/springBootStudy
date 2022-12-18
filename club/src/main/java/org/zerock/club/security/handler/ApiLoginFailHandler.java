package org.zerock.club.security.handler;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description : 인증 실패 Handler
 *              AuthenticationFailureHandler - interface 구현
 * */
@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("login Fail Handler!!!");
        log.info("exception Message :: " + exception.getMessage());

        //ERROR JSON Setting
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 ERROR
        response.setContentType("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        String message = exception.getMessage();
        json.put("code","401");
        json.put("message",message);
        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
