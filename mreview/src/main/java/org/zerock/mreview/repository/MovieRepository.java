package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {


    /**
    * -@Query("select m, avg(coalesce(r.grade,0)),  count(r) from Movie m " +
    *                 "left outer join Review  r on r.movie = m group by m")
    *         Page<Object[]> getListPage(Pageable pageable);
    *
    * MAX(mi) 에는 문제가 있다.
    * - 이유 : 목룩울 가져오는 쿼리는 문제가 없지만 max()를 이용하는 부분에 들어가면 해당 영화의
    *          모든 이미지를 가져오는 쿼리가 실행되기 떄문이다.
    * 
    * ✔ N+1 문제란 ?
    *  - 한번의 쿼리로 N개의 데이터를 가져왔는데 N개의 데이터를 처리하기 위해서 필요한 추가적인 쿼리가
    *    각각 N개의 대하서 수행되는 것임
    * 
    *    쉽게 말하면
    *    - 해당 예제에서는 1페이지에 해당되는 10개의 데이터를 가여오는 쿼리 1번 실행 후
    *      각 영화의 모든 이미지를 가져오기 위한<b>Max()</b> 10번의 추가적인 쿼리가 실행되는것임
     *      
     * 👍 단 JPQL 에서는 별도의 처링벗이 위의 구조를 수정이 간능하다
    * */
    @Query("SELECT m" +                    //Movie 목록
            ", MAX(mi)" +                        //MovieImage
            ", AVG(coalesce(r.grade,0))" + // Review r 의 grade 값의 평균을 구함 coalesce -> Nvl 의 좀더 확작된 Oracle 함수
            ", COUNT(DISTINCT r) " +       // Review r 의 중복 제거 개수
            "FROM Movie m" +
            " LEFT OUTER JOIN MovieImage mi ON mi.movie = m" +
            " LEFT OUTER JOIN Review r ON r.movie = m group by m")
    Page<Object[]> getListPage(Pageable pageable);

    ///// 번외//////
    //getListPage() 에서 MovieImage 의 inum 이 높은 것을 가져온방식 - 위보다는 성능이 좋지 못함 서브쿼리가 들어감
    @Query("select m , i , count(r) from Movie m " +
            "LEFT JOIN MovieImage i on i.movie = m " +
            "and i.inum = (select max(i2.inum) from MovieImage i2 where i2.movie = m) " +
            "left outer join Review r on r.movie = m group by m")
    Page<Object[]> getListPageOrdeyByInum(Pageable pageable);


    //선택된 영화의 정보와 영화의 Image
    // 🔽  Movie + MovieImage
    /*@Query("select m , mi " +
            "from Movie m left outer join MovieImage mi on mi.movie = m where m.mno = :mno")*/
    // 🔽  Movie + MovieImage + Review
    @Query("Select m, im, avg(coalesce(r.grade,0)), count(r)" +
            "from Movie m" +
            " left outer join MovieImage im on im.movie = m" +
            " left outer join Review r on r.movie = m " +
            "where m.mno = :mno  group by im")
    List<Object[]> getMovieWithAll(Long mno);
}
