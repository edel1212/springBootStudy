package org.zerock.mreview.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .email("r"+ i + "@naver.com")
                    .pw("1111")
                    .nickname("reviewer"+i)
                    .build();
            memberRepository.save(member);
        });
    }

    @Transactional
    @Commit
    @Test
    public void testDeleteMember(){
        Long mid = 2L; //Member 의 mid

        Member member = Member.builder().mid(mid).build();

        /**
         * 참조 하고있을 경우 순서가 중요함
         * PK를 먼저 삭제 X
         * 👍 FK 를 가지는 Review를 삭제 후 Member를 삭제해주자
         * 👍 연관관계가 있는경우 U,D 가 있을경우 @Transactional 은 필수다 Error 발생함
         * 👍 @commit 이 없으면 에러는 없지만 해당 테스트의 결과가 DB에 적용 X
         * */
        //reviewRepository.deleteByMember(member); // FK << 비효율
        reviewRepository.fixDeleteByMember(member); // JQPL 사용
        memberRepository.delete(member);         // PK

    }

}
