package com.yoo.toy.service.securiry;

import com.yoo.toy.entity.ClubMember;
import com.yoo.toy.entity.ClubMemberRole;
import com.yoo.toy.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository clubMemberRepository;

    private final PasswordEncoder passwordEncoder;

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


        String email = null;

        if("Google".equals(userRequest.getClientRegistration().getClientName())){
            email = oAuth2User.getAttribute("email");
        }

        log.info("email :: {}",email);

        ClubMember member = this.saveSocialMember(email);

        //TODO OAuth2User 생성 처리

        return oAuth2User;
    }

    /**
     * @Description : Google Api Login 후 인증받은  OAuth2User 객체에서 email값을
     *                받아와 저장하는 Method
     *                💬 다만 로그인 방법이 대하여 생각해 볼면하다
     *                   - Pw값을 받아 올 수 없기 때문에 고정 값으로 해야하기 함.
     *                   - 따라서 로직을 변경하여 재가입을 유도하는 방법으로 하거나
     *                   - 소셜 로그인의 경우 form을 사용할수 없도록 하는 방식을 사용해야함.
     *
     * @param : String email
     *
     * @return ClubMember
     * */
    private ClubMember saveSocialMember(String email){
        // 1 . 전달 받은 email이 가입되어 있는지 확인
        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);

        // 2 . 존재한다면 해당 정보로 return
        if(result.isPresent()) return result.get();

        // 3 . 없다면 ClubMember 객체 생성
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();
        // 3-1 . 권한 추가
        clubMember.addMemberRole(ClubMemberRole.USER);

        // 4 . 저장
        clubMemberRepository.save(clubMember);

        return clubMember;
    }

}
