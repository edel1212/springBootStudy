package com.yoo.toy.service.securiry;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    /**
     * @Description : OAuth Login을 커스텀 하는 Method
     *               간단하게 설명하면 Spring Security의 DetailsService와 같은 기능을 한다
     *               생각하면 된다.
     *
     * @param       : OAuth2UserRequest userRequest - 로그인 요청에 관한 정보를 갖고있다.
     *
     * @return      : OAuth2User
     * */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth에 사용된 Client Name => 현 테스틑 Goolge Social Login이기에 Goole 출력
        log.info("clientName :: {}",userRequest.getClientRegistration().getClientName());
        // id_token 값을 확인 할 수 있다.
        log.info("additionalParameters ::: {}",userRequest.getAdditionalParameters());

        //반환 객요청 : sub, picture, email, email_verified(이메일 확인) 정보를 갖고 있다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("-----------------------------");
        oAuth2User.getAttributes().forEach((k,v)->{
            log.info("Key :: {} ,  Value ::{}",k,v);
        });
        log.info("-----------------------------");

        return oAuth2User;
    }

}
