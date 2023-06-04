package com.yoo.loginServer.service.member;

import com.yoo.loginServer.dto.member.MemberDTO;
import com.yoo.loginServer.entity.member.Member;

public interface MemberService {
    boolean findMember(MemberDTO memberDTO);

    String registerMember(MemberDTO memberDTO);


    default Member dtoToEntity(MemberDTO memberDTO){
        return Member.builder()
                .id(memberDTO.getId())
                .pw(memberDTO.getPw())
                .name(memberDTO.getName())
                .role(memberDTO.getRole())
                .build();
    }

    default MemberDTO entityToDto(Member member){
        return MemberDTO.builder()
                .id(member.getId())
                .pw(member.getPw())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }
}
