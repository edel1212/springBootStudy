package com.yoo.loginServer.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class CORSFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        // 서버가 웹 페이지나 앱에서 특정 도메인에서 오는 요청을 허용할지 여부를 지정합니다.
        response.setHeader("Access-Control-Allow-Origin"    , "*");
        // 웹 브라우저가 요청에 대한 인증 정보를 포함할 수 있는지 여부를 지정합니다.
        response.setHeader("Access-Control-Allow-Credential", "true");
        // HTTP 메서드(예: GET, POST, PUT, DELETE 등)를 지정함.
        response.setHeader("Access-Control-Allow-Method"    , "*");
        // 정의된 시간 동안 클라이언트의 CORS 요청을 캐싱할 수 있도록 허용하는 역할을 합니다.
        response.setHeader("Access-Control-Allow-Max-Age"   , String.valueOf(60 * 60));
        // 클라이언트가 서버에게 어떤 종류의 헤더를 요청할 수 있는지를 제어합니다.
        //  - 헤더에 명시된 헤더 이외의 헤더는 클라이언트가 요청할 수 없습니다.
        response.setHeader("Access-Control-Allow-Headers"   ,
                "Origin, X-Requested-With" +
                        ", Content-Type" +
                        ", Accept" +
                        ", Key" +
                        ", Authorization");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }//if-else

    }
}