package com.yoo.exGraphQL.service;

import com.yoo.exGraphQL.dto.MemberDTO;
import com.yoo.exGraphQL.entity.Member;

import java.util.List;

public interface MemberService {
    //Query Type
    MemberDTO findMember(String email);

    List<MemberDTO> allFindMembers();


    ///////////////////////////////////////////


    //Mutation Type
    String registerMember(MemberDTO member);

    Boolean updateMember(MemberDTO member);

    Boolean deleteMember(String email);

    ////////////////////////////////////////////////

    //dtoToEntity
    default Member dtoToEntity(MemberDTO memberDTO){
        return Member.builder()
                .email(memberDTO.getEmail())
                .password(memberDTO.getPassword())
                .name(memberDTO.getName())
                .build();
    }

    //entityToDto
    default MemberDTO entityToDto(Member member){
        return MemberDTO.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .regDate(member.getRegDate())
                .modDate(member.getModDate())
                .build();
    }

}
