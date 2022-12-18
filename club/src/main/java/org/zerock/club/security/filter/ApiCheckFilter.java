package org.zerock.club.security.filter;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

        if(matchFlag){ // 내가 지정한 URI 와 접근한 URI 가 같을 경우 TRUE
            log.info("ApiCheckFilter...........................");
            log.info("ApiCheckFilter...........................");
            log.info("ApiCheckFilter...........................");
            
            //API 의 Header 값 체크 [내가 지정한값 - 보안을 위함 허용된 사용자만 사용하게끔 하기 위함]
            boolean checkHeader = this.checkAuthHeader(request);
            if(checkHeader){
               filterChain.doFilter(request,response); // 다음 단계로 넘어가는 역할을 해줌
               return;
            } else { //인증 Header 값이 다를경우
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403 ERROR
                //Return JSON encoding Header Setting
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", 403);
                json.put("message", message);
                
                // HttpServletResponse 를 이용해서
                // PrintWriter 객체 생성 후 거기에
                // json 을 담아 전달함.
                PrintWriter out = response.getWriter();
                out.println(json);
                return;
            }
        }//if

        filterChain.doFilter(request,response); // 다음 단계로 넘어가는 역할을 해줌
    }

    /**
     * @Description : Authorization 해더 처리를 위한 method
     *                - 특정한 API 를 호출 시 다른  Server 또는 Client 에서 실행 되기 때문에
     *                  쿠키, 세션을 사용할수 없는 문제가있음
     *                  ✅ 따라서 해당 정보는 Http Header Message 에 특별한 값을 담아서 전송한다.
     *
     *                - Client 에서 전송한 Request 에 포함된 Authorization 해더 값을 파악해서
     *                  사용자가 정상적인 요청인지 확인함.
     *
     * */
    private boolean checkAuthHeader(HttpServletRequest request){
        boolean checkResult = false;

        String  authHeader = request.getHeader("Authorization");
        
        //내가 정한 값으로 Equals 시키자
        if("12345678".equals(authHeader)){
            log.info("Authorization exist : " +authHeader);
            checkResult = true;
        }//if
        
        return checkResult;
    }

    //__Eof__
}
