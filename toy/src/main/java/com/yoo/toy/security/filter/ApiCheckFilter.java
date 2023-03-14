package com.yoo.toy.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description : 현재 필터의 Bean 등록은 Security Config에서 해주고 있다.
 *
 *                - 별도의 설정 없이 현재 필터는 Spring Security 의 필터가 끝난 후 에
 *                  log가 찍히는 것을 확인 할 수 있다.
 *                  👉 Security -> OncePerRequestFilter -> Controller
 * */
@Log4j2
//@Component  <- 해당 방법으로 주입도 가능함. 단 Bean Life Cycle을 생각하면 Security Config에서 해주는것이 더 안전
public class ApiCheckFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
                                        , FilterChain filterChain) throws ServletException, IOException {
        log.info("ApiCheckFilter ........... doFilterInternal()");
        log.info("ApiCheckFilter ........... doFilterInternal()");
        log.info("ApiCheckFilter ........... doFilterInternal()");
        filterChain.doFilter(request,response);
    }
}
