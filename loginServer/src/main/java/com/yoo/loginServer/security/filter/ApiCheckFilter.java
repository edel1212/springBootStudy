package com.yoo.loginServer.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoo.loginServer.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;

    private String formatPattern;

    private JWTUtil jwtUtil;

    public ApiCheckFilter(String formatPattern){
        this.antPathMatcher = new AntPathMatcher();
        this.formatPattern = formatPattern;
        this.jwtUtil = new JWTUtil();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("접근 URI :: {}", request.getRequestURI());

        boolean matchFlag = antPathMatcher.match(formatPattern, request.getRequestURI());

        log.info("MatchFlag :: {}", matchFlag);

        // URI 매칭이 아닐경우 스킵
        if(!matchFlag){
            filterChain.doFilter(request,response);
            return;
        }

        // 해더값 체크 - 없을경우 403 Error
        if(!this.checkAuthHeader(request)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String msg = "FAIL CHECK API TOKEN";
            Map<String, String> obj = new HashMap<>();
            obj.put("timestamp" , Calendar.getInstance().getTime().toString());
            obj.put("status"    , String.valueOf(HttpServletResponse.SC_FORBIDDEN));
            obj.put("error"     , msg);
            obj.put("message"   , msg);
            obj.put("path"      , request.getRequestURI());
            ObjectMapper objectMapper = new ObjectMapper();
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(objectMapper.writeValueAsString(obj));
            return;
        }

        //안해주면 안넘어가
        filterChain.doFilter(request,response);
    }

    /**
     * 요청의 Head 값 확인
     * */
    private boolean checkAuthHeader(HttpServletRequest httpServletRequest){
        boolean result = false;

        String authorizationKey = httpServletRequest.getHeader("Authorization");

        log.info("authorizationKey ::: {}", authorizationKey);

        if(!StringUtils.hasText(authorizationKey)) return false;

        String jwtDecode = "";
        try {
            jwtDecode = jwtUtil.validateAndExtract(authorizationKey);
        } catch (Exception e){
            e.printStackTrace();
        }

        log.info("jwt Decode Result ::: {}", jwtDecode);

        if(StringUtils.hasText(jwtDecode)) result = true;

        return result;
    }

}
