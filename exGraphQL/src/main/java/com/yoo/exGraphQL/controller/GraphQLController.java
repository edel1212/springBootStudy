package com.yoo.exGraphQL.controller;

import com.yoo.exGraphQL.dto.MemberDTO;
import com.yoo.exGraphQL.entity.Member;
import com.yoo.exGraphQL.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GraphQLController {
    private final MemberService memberService;

    //Query Type

    @QueryMapping
    public MemberDTO findMember(String email){
        log.info("----- graphQL QueryType -----");
        return memberService.findMember(email);
    }

    @QueryMapping
    public List<MemberDTO> allFindMembers(){
        log.info("----- graphQL QueryType -----");
        return memberService.allFindMembers();
    }

    /////////////////////////////////////////

    //Mutation Type

    @MutationMapping
    public String registerMember(MemberDTO memberDTO){
        log.info("----- graphQL MutationType -----");
        return memberService.registerMember(memberDTO);
    }

    @MutationMapping
    public Boolean updateMember(MemberDTO memberDTO){
        log.info("----- graphQL MutationType -----");
        return memberService.updateMember(memberDTO);
    }

    @MutationMapping
    public Boolean deleteMember(String email){
        log.info("----- graphQL MutationType -----");
        return memberService.deleteMember(email);
    }

}
