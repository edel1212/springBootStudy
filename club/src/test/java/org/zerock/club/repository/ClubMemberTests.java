package org.zerock.club.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder; //password Encode

    @Test
    public void testPrint(){
        System.out.println(ClubMemberRole.MANAGER);
    }

    @Test
    public void interDummies(){

        //1 ~ 80 USER
        //81 ~ 80 USER, MANAGER
        //91 -` 100 USER, MANAGER, ADMIN

        IntStream.rangeClosed(1, 100).forEach(i->{
            
            /**
             * Entity 생성
             * */
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@naver.com" )
                    .name("사용자"+ i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();
        
            /**
             * Entity roleSet 지정
             * */
            // Default role  :: USER -- 모든 사용자는 USER 권한을 준다.
            clubMember.addMemberRole(ClubMemberRole.USER);

            if(i > 80) clubMember.addMemberRole(ClubMemberRole.MANAGER);

            if(i > 90) clubMember.addMemberRole(ClubMemberRole.ADMIN);

            repository.save(clubMember);
        });

    }

    @Test
    public void testRead(){
        Optional<ClubMember> result = repository.findByEmail("user95@naver.com", false);

        ClubMember clubMember = null;
        if (result.isPresent()) clubMember = result.get();

        System.out.println("------------------");
        System.out.println(clubMember);
        //ClubMember(email=user95@naver.com, password=$2a$10$CjmLmHj6GRivF2XoLmSBIugmnlbLB92d6LMMQWJsaDNKphgQOX5Fa, name=사용자95, fromSocial=false, roleSet=[USER, MANAGER, ADMIN])
        System.out.println("------------------");

    }

    //__Eof__
}
