package com.yoo.toy.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description : 현재 필터의 Bean 등록은 Security Config에서 해주고 있다.
 *
 *                - 별도의 필터 순서 설정 없다면 현재 필터는 Spring Security 의 필터가 끝난 후
 *                  log가 찍히는 것을 확인 할 수 있다.
 *                  👉 순서 설정이 없을 경우 순서 : Security -> OncePerRequestFilter -> Controller
 *
 *                - HTTP 요청이 들어올 때마다 실행됩니다.
 *                - 요청을 처리하기 전에 필요한 전처리 작업을 수행하고, 응답을 보내기 전에 후처리 작업을 수행합니다.
 *                - 청을 처리하는 동안 동일한 필터가 여러 번 실행되는 것을 방지할 수 있습니다.
 * */
@Log4j2
//@Component  <- 구조를 생각하면 해당 방법을 사용할수 없음 해당 CLass는
//               Interface도 없으며 객체 생성시 요구하는 매개변수가 존재한다.
public class ApiCheckFilter extends OncePerRequestFilter {

    // Ant Pattern으로 URL을 매칭용 객체 변수
    private AntPathMatcher antPathMatcher;
    // 넘어올 URL을 받을 변수
    private String pattern;

    // 생성자 메서드를 사용
    public ApiCheckFilter(String pattern){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
                                        , FilterChain filterChain) throws ServletException, IOException {

        log.info("RequestURI :: {}", request.getRequestURI());

        log.info("March Pattern to URI :: ,", antPathMatcher.match(pattern, request.getRequestURI()));


        if(antPathMatcher.match(pattern, request.getRequestURI())){
            log.info("ApiCheckFilter ........... doFilterInternal()");
            log.info("ApiCheckFilter ........... doFilterInternal()");
            log.info("ApiCheckFilter ........... doFilterInternal()");
            return;
        }

        filterChain.doFilter(request,response);
    }
}
