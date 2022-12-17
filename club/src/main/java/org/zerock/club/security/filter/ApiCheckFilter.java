package org.zerock.club.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description : 해당 Class 는 필터역활을 하는 Class 이다. 
 *               - Spring Security는 원하는 필터를 사용자가 작성하고
 *                    이를 설정에서 Application이 동작할때 동작의 일부로 추가가 가능하다
 *
 *               - abstract Class 인 OncePerRequestFilter 를 상속받아 구현
 *                 ✔ 해당 abstract Class 는 필터로 가장 일반적이며 매번 동작하는 기본적인
 *                   필터로 보면된다.
 *
 *               🎈 중요! 해당 Class 는 상속만 해준다고 끝나는게 아닌
 *                  - @Configuration  설정을 해준 SecurityConfig 파일에 Bean 등록을 해주자!
 * */
@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
    /**
     * URL 경로에 맞는 것만 Filter 하기 위함
     * */
     private final AntPathMatcher antPathMatcher;
     private final String pattern;

    public ApiCheckFilter(String pattern){
        this.pattern = pattern;
        this.antPathMatcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("RequestURI :: " + request.getRequestURI());

        boolean matchFlag = this.antPathMatcher.match(this.pattern, request.getRequestURI()); //URI 와 URI Pattern 비교
        log.info("matchFlag ::: " + matchFlag);

        if(matchFlag){
            log.info("ApiCheckFilter...........................");
            log.info("ApiCheckFilter...........................");
            log.info("ApiCheckFilter...........................");
            return;
        }//if

        filterChain.doFilter(request,response); // 다음 단계로 넘어가는 역할을 해줌
    }
}
