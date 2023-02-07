package com.yoo.exGraphQL.controller;

import com.yoo.exGraphQL.entity.Member;
import com.yoo.exGraphQL.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GraphQLController {
    private final MemberService memberService;

    @QueryMapping
    public List<Member> allFindMembers(){
        log.info("----- graphQL QueryType -----");
        return memberService.getList();
    }

}
