package com.yoo.toy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoo.toy.dto.security.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : Security Success 시 처리하는
 *                Handler class
 *
 *                👉 Failure Handler 와 다르게 SuccessHandler는 Interface이다!
 *
 * ---------------------------------------------------------------------------
 *
 * Error msg : The dependencies of some of the beans in the application context form a cycle:
 *
 * 💬 원인 : 어플리케이션 컨텍스트에서 일부 Bean의 종속성의 순환 주기 형성과정에서 생기는 문제
 *          - 1 . 기존 CustomAuthSuccessHandler에서 따로 @Component를 사용하여
 *          Bean Container에 등록하여 사용하였다
 *          - 2 .그렇게만 사용하면 문제가 없었는데 해당 Class를 요청하는 SecurityConfig에서도 PasswordEncoder를 @Bean등록
 *          - 3 . 여기서도 사용하므로 순환 관계가 섞이는 문제가 발생
 *
 * 👉 해결방법 : 1 . @Lazy 어노테이션을 통해 지연로딩으로 임시방편으로 문제를 해결한다. [ 추천하지 않음 ]
 *            2 .  해당 Bean 연관관계를 구성하는 부분을 수정한다.
 *
 *
 * */
@Log4j2
public class CustomAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private PasswordEncoder passwordEncoder;

    public CustomAuthSuccessHandler(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {

        log.info("Success!!");

        //TODO Business logic [ 비밀번호 오류 횟수 초기화등 여러가지 로직이 처리 가능하다. ]

        // 로그인에 성공한 정볼르 가져옴
        ClubAuthMemberDTO authMemberDTO = (ClubAuthMemberDTO) authentication.getPrincipal();

        boolean fromSocial = authMemberDTO.isFromSocial();
        log.info("fromSocial ::: {}",fromSocial);
        if(fromSocial){
            boolean passwordMatch = passwordEncoder.matches("1111", authMemberDTO.getPassword());
            if(passwordMatch) redirectStrategy.sendRedirect(request,response,"/member/modify");
        }//if



        log.info("login user authentication :: " + authentication.getAuthorities());

        Map<String, String> result = new HashMap<>();
        result.put("status","200");
        result.put("Msg","Success");

        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(result));

        /**
         * 해당 방법으로 리다이렉트 이동이 불가능하다.
         * 💬 Login 로직을 비동기 방식으로 진행했기에
         * 404 에러가 떨어짐 URL 이동은 스크립트로 처리가 필요하다
         * */
        //response.sendRedirect("/sample/member");

    }
}
