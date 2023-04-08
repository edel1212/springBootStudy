package com.yoo.toy.security.filter;

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

/**
 * 외부에서 접근시 시 CORS문제를 해결하기 위한 Class
 * - @Component 사용으로 BeanContainer에 자동 등록
 * */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class CORSFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
                , FilterChain filterChain) throws ServletException, IOException {

        log.info("CORSFilter Setting...");

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

        log.info("request.getMethod() ::: "  + request.getMethod()); // HTTP요청 방식을 가져옴
        /**
         * 요청 전에 필요한 설정을 수행: beforeSend 콜백을 사용하면, AJAX 요청이 서버로 보내기 전에 필요한 설정을 수행할 수 있습니다. 예를 들어, 헤더 값을 추가하거나 특정한 데이터를 변경하는 등의 작업을 수행할 수 있습니다.
         *
         * 인증 토큰 등의 인증 정보를 추가: beforeSend를 사용하여 요청 헤더에 인증 토큰 등의 인증 정보를 추가할 수 있습니다. 이를 통해 AJAX 요청이 서버에서 인증을 통과하도록 할 수 있습니다.
         *
         * 로딩 표시 등의 UI 업데이트: beforeSend를 사용하여 AJAX 요청이 보내지기 전에 로딩 표시 등의 UI 업데이트를 수행할 수 있습니다. 이를 통해 사용자에게 요청이 처리되고 있음을 시각적으로 알려줄 수 있습니다.
         *
         * 요청 취소: beforeSend에서 return false;를 반환하면 AJAX 요청이 취소됩니다. 이를 통해 특정 조건에 따라 요청을 중단할 수 있습니다.
         * */
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }//if-else

    }
}
