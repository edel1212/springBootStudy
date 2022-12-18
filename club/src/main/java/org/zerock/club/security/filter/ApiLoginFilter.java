package org.zerock.club.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description : 로그인 인증 관련 Filter 
 *                - 추상 Class 인 : AbstractAuthenticationProcessingFilter 를 상속 받아 구현
 *     
 * */
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    public ApiLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("--------------ApiLoginFilter------------");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
        String pw    = request.getParameter("pw");

        //ClubUserDetailService.loadUserByUsername(String username) 에서 확인 후  token  생성
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, pw);

        if(email == null) throw new BadCredentialsException("email cannot be null"); //자격, 적격, 적성, 자격 증명서, 성적(인물)증명서이 없다는 Exception

        return getAuthenticationManager().authenticate(authToken);
    }

    //Success Handler Method
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("------------ApiLoginFilter :: success Handler!---------------");
        log.info("successfulAuthentication :: " + authResult);
        log.info("getPrincipal :::: " + authResult.getPrincipal());
    }

}
