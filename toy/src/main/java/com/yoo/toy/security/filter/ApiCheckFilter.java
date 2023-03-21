package com.yoo.toy.security.filter;

import com.nimbusds.common.contenttype.ContentType;
import com.sun.net.httpserver.HttpContext;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
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

            /**
             * 내가 지정한 Header의 값을 확인
             * - 💬 ApiCheckFilter에서는 현재 스프링 시큐리티가 사용하는
             *      쿠키나 세션을 사용 하지 않기 때문에 Client에서 요청 시
             *      chkAuthHeader()에서 체크하는 Authorization 해더 값이 없어도
             *      이상없이 200을 반환하는 문제가있다.
             *
             * - 👉 AuthenticationManager를 사용하거나 JSON 포맷을 사용하여
             *      에러 메세지 및 HTTP 상태를 반환해 주는 방법을 사용할 수 있다.
             *      현재는 간단하게 JSON 반환을 사용함.
             * */
            if(this.chkAuthHeader(request)){
                filterChain.doFilter(request,response);
            } else {
                // 403 Error
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                JSONObject json = new JSONObject();
                String msg = "FAIL CHECK API TOKEN";
                json.put("code" , HttpServletResponse.SC_FORBIDDEN);
                json.put("msg"  , msg);
                PrintWriter out = response.getWriter();
                out.println(json);
                return;
            }//if - else


        }//if

        // doFilter()를 사용하지 않으면 다음 필터로 넘어가지 않음
        filterChain.doFilter(request,response);
    }

    /**
     * @Description : Request로 넘어몬 Header값을 확인 하는 메서드
     * */
    private boolean chkAuthHeader(HttpServletRequest request){
        boolean checkResult = false;

        // 1 . Authorization라는 값으로 넘어온 Header 값을 추출한다.
        String authHeader = request.getHeader("Authorization");

        // 2. 값의 유무를 체크함
        if(!StringUtils.hasText(authHeader)) return checkResult;

        log.info("Authorization exist :: {}", authHeader);

        // 3 . 내가 지정한 값과 일치한다면 true로 변환
        if("123456789".equals(authHeader)) checkResult = true;

        // 4 . 반환
        return checkResult;
    }

}
