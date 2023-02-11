package com.yoo.exGraphQL.controller;

import com.yoo.exGraphQL.dto.MemberDTO;
import com.yoo.exGraphQL.entity.Member;
import com.yoo.exGraphQL.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GraphQLController {
    private final MemberService memberService;

    //Query Type

    /**
     * @Argument가 없을 시 넘어오는 Parameter를 인식하지 못하는 error가 있음
     *  - @Argument 는 @RequestBody, @RequestParam과 같은 인자값을 지정해줄 때 사용합니다.
     *
     * Error Msg : was not recognized by any resolver and there is no source/parent either.
     *             Please, refer to the documentation for the full list of supported parameters.
     * */
    @QueryMapping
    //public MemberDTO findMember(String email){  ☠️ Error  발생
    public MemberDTO findMember(@Argument String email){
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

    /**
     * mutation{
     *   registerMember(
     *     memberEntity : {
     *      email : "edel1212@naver.com"
     *       name : "GrpahQlInsert"
     *       password :"123"
     *     }
     *   ){
     *     email
     *   }
     * }
     *
     * - Client에서 넘겨주는 ParameterID와 다를경우 Argument("Key!!")로
     *   설정하여 받을수있다! 아닐경우 값을 인식하지 못해 Error 발생
     *
     * Error Msg : was not recognized by any resolver and there is no source/parent either.
     *             Please, refer to the documentation for the full list of supported parameters.
     * */
    @MutationMapping
    //public String registerMember(@Argument MemberDTO memberDTO){
    public String registerMember(@Argument("memberEntity") MemberDTO memberDTO){
        log.info("----- graphQL MutationType -----");
        return memberService.registerMember(memberDTO);
    }

    /**
     * mutation{
     *   updateMember(
     *     memberEntity : {
     *      email : "edel1212@naver.com4"
     *       name : "GrpahQlChanage!!"
     *       password :"123"
     *     }
     *   )
     * }
     * */
    @MutationMapping
    public Boolean updateMember(@Argument("memberEntity") MemberDTO memberDTO){
        log.info("----- graphQL MutationType -----");
        return memberService.updateMember(memberDTO);
    }

    @MutationMapping
    public Boolean deleteMember(@Argument String email){
        log.info("----- graphQL MutationType -----");
        return memberService.deleteMember(email);
    }

}
