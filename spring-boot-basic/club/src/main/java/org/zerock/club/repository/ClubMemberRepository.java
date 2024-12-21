package org.zerock.club.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.club.entity.ClubMember;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    /**
     * @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD) 사용으로로
     * 지연처리 가능
     * 
     * 
     * select
     *         clubmember0_.email as email1_0_,
     *         clubmember0_.moddate as moddate2_0_,
     *         clubmember0_.regdate as regdate3_0_,
     *         clubmember0_.from_social as from_soc4_0_,
     *         clubmember0_.name as name5_0_,
     *         clubmember0_.password as password6_0_,
     *         roleset1_.club_member_email as club_mem1_1_0__,
     *         roleset1_.role_set as role_set2_1_0__
     *     from
     *         club_member clubmember0_
     *     left outer join
     *         club_member_role_set roleset1_
     *             on clubmember0_.email=roleset1_.club_member_email
     *     where
     *         clubmember0_.from_social=?
     *         and clubmember0_.email=?
     * */
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM ClubMember m WHERE m.fromSocial = :social and m.email = :email ")
    Optional<ClubMember> findByEmail(String email, boolean social);
}
