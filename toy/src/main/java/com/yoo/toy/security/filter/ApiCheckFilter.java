package com.yoo.toy.security.filter;

import com.nimbusds.common.contenttype.ContentType;
import com.sun.net.httpserver.HttpContext;
import com.yoo.toy.security.util.JWTUtil;
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

    // 해더 메세지를 통해 Jwt를 확인하기 위한 객체 변수
    private JWTUtil jwtUtil;

    // 생성자 메서드를 사용
    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
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
            if(!this.chkAuthHeader(request)){
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
            }//if

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
        if(!StringUtils.hasText(authHeader)) return false;

        log.info("Authorization exist :: {}", authHeader);

        // 3. ⭐️ 기존 하드코딩 값에서 변경 --> JWT 체크
        try {
            /**
             * 💬 받아온 Authorization에서 substring(7)하는 이유 ?
             * - 확장 가능성: "Bearer"는 인증 타입을 표시하는 문자열로, 향후에 다양한 타입의 토큰이 나올 경우에도 "Bearer"
             *   외의 다른 인증 타입을 사용할 수 있습니다. 예를 들어, "Basic", "Digest", "OAuth" 등 다양한 인증 스킴이 존재하며,
             *   "Bearer"를 사용함으로써 나중에 다른 인증 스킴을 추가하거나 교체할 때 유연성을 가질 수 있습니다.
             *
             *    따라서 "Bearer"를 JWT 토큰의 타입을 나타내는 문자열로 사용하는 것은 인증
             *    스킴의 명시성과 확장성을 갖출 수 있는 이점을 가지고 있습니다.
             * */
            String email = jwtUtil.validateAndExtract(authHeader.substring(7));
            log.info("validationCheck Result Email :: {}",email);

            checkResult = email.length() > 0;
        } catch (Exception ex){
            ex.printStackTrace();
        }

        // 4 . 반환
        return checkResult;
    }

}
