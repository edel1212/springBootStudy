package com.yoo.toy.repository;

import com.yoo.toy.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember,String> {

    /**
     * ClubMember의 roleSet은 지연로딩으로 설정되어 있는데 해당 컬럼만을 로딩방법을
     * EAGER로딩으로 바꾸어 Proxy객체가 아닌 같이 SELECT 할수 있게끔 설정함
     * */
    @EntityGraph(attributePaths = "roleSet", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM ClubMember m WHERE m.fromSocial = :social AND " +
            "m.email = :email")
    Optional<ClubMember> findByEmail(String email, boolean social);

}
