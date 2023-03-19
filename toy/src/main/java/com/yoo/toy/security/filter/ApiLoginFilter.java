package com.yoo.toy.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description : AbstractAuthenticationProcessingFilter 이란?
 *               - 추상클래스로 설계 되어있다.
 *               - attemptAuthentication() 추상 메서드가 반드시 필요하다.
 *               - AbstractAuthenticationProcessingFilter가 요구하는 문자열을 받느 생성자가 반드시 필요하다
 *
 *               💬 사용 이유
 *               - Spring Security에서 인증을 처리하는 데 사용됩니다.
 *               - 인증을 위해 사용자가 제출한 자격 증명(예: 사용자 이름과 비밀번호)을 검증하고,
 *                 인증이 성공하면 해당 사용자의 보안 주체(Principal)를 생성하여 SecurityContext에 저장합니다.
 *                 이 필터는 인증이 실패하면 실패한 이유에 대한 적절한 응답을 생성합니다.
 *
 *              👉 인증 처리를 위한 Filter
 * */
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    public ApiLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request
                        , HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("--------------------------------------------");
        log.info("ApiLoginFilter");
        log.info("--------------------------------------------");

        // 이메일을 받는 테스트 코드
        String email = request.getParameter("email");
        String pw = "1111";

        if(email == null) throw new BadCredentialsException("email cannot be null");

        return null;
    }
}
