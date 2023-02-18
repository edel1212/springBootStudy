package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //@EntityGraph(attributePaths ={"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    //메서드 명으로 삭제하지만 Member 의 개수가 여러개일경우 비효율적으로 개수만 큼 delete 를 사용함
    @Deprecated
    void deleteByMember(Member member);
    
    //위의 비효율을 막기위해서는 JPQL 을 사용해서 사용하는것이 효율적임
    @Query("Delete from Review mr where mr.member =:member ")
    @Modifying // U, D 를 사용시 필요하다!
    void fixDeleteByMember(Member member);
    

}
