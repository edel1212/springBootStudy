package com.yoo.loginServer.security.service;

import com.yoo.loginServer.entity.member.Member;
import com.yoo.loginServer.repository.member.MemberRepository;
import com.yoo.loginServer.security.dto.MemberAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("userDetailService..............");

        log.info("id ::{}",username);

        Member member = memberRepository.findById(username).orElse(null);

        log.info("member::: {}",member);

        MemberAuthDTO memberAuthDTO = new MemberAuthDTO(
                member.getId()
                , member.getPw()
                , Arrays.asList(new SimpleGrantedAuthority("ROLE_"+member.getRole()))
        );

        return memberAuthDTO;
    }
}
