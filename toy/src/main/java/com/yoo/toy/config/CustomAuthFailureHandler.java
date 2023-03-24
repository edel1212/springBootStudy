package com.yoo.toy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : Security Exception 발생시 처리하는
 *                Handler class
 *
 *              - 💬 AuthenticationFailureHandler를 구현한 클래스 중 하나로,
 *                   로그인 실패 시 리다이렉트할 URL을 지정하는 역할을 합니다.
 *                   SimpleUrlAuthenticationFailureHandler를 사용하면 인증 실패 시
 *                   자동으로 지정한 URL로 리다이렉트되도록 설정할 수 있습니다.
 * */
@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMsg;
        if (exception instanceof BadCredentialsException) {
            errorMsg = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMsg = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMsg = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMsg = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMsg = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
        }

        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("status"   , "401");
        errorMap.put("errorMsg" , errorMsg);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(errorMap));

    }

}
