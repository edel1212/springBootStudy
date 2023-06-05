package com.yoo.loginServer.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoo.loginServer.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private JWTUtil jwtUtil;

    public AuthSuccessHandler(){
        this.jwtUtil = new JWTUtil();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("LoginSuccess Handler");

        log.info("id :: {}"     , authentication.getName());
        log.info("roles :: {}"   , authentication.getAuthorities());

        Map<String, String> resultState = new HashMap<>();
        resultState.put("state"     , "200");
        resultState.put("message"   , "success");
        // Jwt Token
        getToken(resultState, authentication);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(resultState));
    }

    /**
     * JWT Token 생성 및 주입
     * */
    private void getToken(Map<String, String> resultState,Authentication authentication ) {
        try {
            String id = authentication.getName();
            List<String> roles = authentication.getAuthorities().stream().map(String::valueOf).collect(Collectors.toList());
            String result = jwtUtil.generateToken(id, roles);
            resultState.put("token"     , result);
        }catch (Exception io){
            io.printStackTrace();
        }
    }

}
