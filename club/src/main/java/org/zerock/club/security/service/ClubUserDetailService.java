package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description : 해당 Service Class 에서 주목해야 할점은
 *               - @Service 어노테이션 만으로도  Bean 에 주입이 가능하다는 점이다.
 *
 *               ❌ 해당 UserDetailService 를 상속받는 Service 를 생성 시
 *                  이전 SecurityConfig 의 InMemoryUserDetailsManager() 메서드를
 *                  제거해줘야한다! - 로그조차 안찍힘!
 *                  <b>
 *                  Error : No AuthenticationProvider found
 *                          for org.springframework.security.authentication.UsernamePasswordAuthenticationToken
 *                  </b>
 *                  이유는 ❔
 *                  ClubUSerDetailsService 가 Bean 으로 등록되면 이르 자동으로 Spring Security 에서
 *                  USerDetailsService 로 인식하기 때문임.
 * */
@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailService implements UserDetailsService {

    final private ClubMemberRepository clubMemberRepository;
    
    /**
     * - from Login 시 
     *   해당 loadUserByUsername()가 동작
     * 
     * - 소셜 로그인 버튼 시에는 해당 메서드가 아닌
     *   ClubOAuth2UserDetailService.loadUser()가 동작
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService userName(ID):::" + username);

        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);

        if(result.isEmpty()) throw new UsernameNotFoundException("Check Email or Social");

        ClubMember clubMember = result.get();

        log.info("-----------------------");
        log.info(clubMember);

        /***
         * ✅✅ 해당  생성자를 만들때 id Pw 검사를 하는것임!! ✅✅
         *
         *  1) clubMember 객체에는 DB에서 가져온 encoding 된 PW를 가지고있고
         *  2) Client 단에서 넘어온 password 를 encoding 한 후 비교
         *  3) 다르면 당연히 비밀번호가 다른것이기 떄문에 UsernameNotFoundException!!
         * */
        //User 을 상속받은 Class 이기떄문에 권한 체크가 가능함!
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
          clubMember.getEmail(),
          clubMember.getPassword(),
          clubMember.isFromSocial(),
          clubMember.getRoleSet().stream()
                  .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                  .collect(Collectors.toList())
        );

        /**
         * Spring Security 에는 없는 것들 추가!
         * **/
        clubAuthMember.setName(clubMember.getName()); //진짜 이름
        clubAuthMember.setFromSocial(clubMember.isFromSocial()); //소셜 구분

        return clubAuthMember;
    }
}
