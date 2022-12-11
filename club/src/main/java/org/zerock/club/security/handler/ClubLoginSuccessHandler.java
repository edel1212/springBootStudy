package org.zerock.club.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * @Description : 인증이 성공 후 다음 결과처리를 위한 Class
 *                - AuthenticationSuccessHandler[Interface]를 구한함
 * 
 */
@Log4j2
public class ClubLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;
    
    //생성자 추가 -> PasswordEncoder 요구함
    public ClubLoginSuccessHandler(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
    
    
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---------------------------------");
        log.info("onAuthenticationSuccess");

        log.info("getPrincipal :::: " + authentication.getPrincipal());
        // 반환 값 : getPrincipal :::: ClubAuthMemberDTO(.......);  <<- 이유는 ClubUserDetailService 에서
        //          loadUserByUsername() 에서 넘겨주는 returns 값이
        //          ClubAuthMemberDTO 이기 때문이다
        //          - [ form 로그인일 경우  user 상속받은 DTO 이기 때문에 가능하다 ] ClubUserDetailService
        //          - [ social 로그인일 경우  OAuth2User 구현하였기에 때문에 가능하다 ] ClubOAuth2USerDetailsService
        log.info("---------------------------------");

        ClubAuthMemberDTO authMember = (ClubAuthMemberDTO) authentication.getPrincipal();


        boolean fromSocial = authMember.isFromSocial();

        log.info("Need Modify Member? : " + fromSocial);
    
        //현재 Social PW 는 "111" 로 지정했음
        boolean passwordResult = passwordEncoder.matches("1111",authMember.getPassword());

        if(fromSocial && passwordResult){
            this.redirectStrategy.sendRedirect(request,response,"/member/modify?from=social");
        }//if

    }




}
