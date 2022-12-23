package org.zerock.bimovie.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "tbl_movie")
public class Movie extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String title;

    /**
     * @Description : 아래의 Code 에서 mappedBy = "해당주체" 를 넣어주지 않으면
     *               해당 Movie Entity 에서의 주인으로 파악하여
     *               Movie  -  Poster
     *              ㄴ> 2개를 이으는 tbl_movie_poster_list 라는 테이블이 생성되어 버림.
     *
     *              ✅ 따라서  @OneToMany 로 지정하고 있지만 해당 개체의 주인이 아니라는
     *              mappedBy ="주체" 선은을 해줌으로써
     *              - tbl_movie(1) : tbl_poster(N) 형태의 테이블 관계가 생성된다!
     * */

    @ToString.Exclude
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY        // 나는 한개 대상은 여러개 -> OneToMany
                , mappedBy = "movie"         // 실제 데이터베이스에서 자신은 연관관계의 주인이 아니라는것을 명시 한것
                , cascade = CascadeType.ALL  //상위 엔티티의 모든 상태변화 전파
    )
    private List<Poster> posterList = new ArrayList<>();

    public void addPoster(Poster poster){
        poster.setIdx(this.posterList.size());
        poster.setMovie(this);
        posterList.add(poster);
    }

}

