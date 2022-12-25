package org.zerock.bimovie.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
                , orphanRemoval = true       // 참조가 없는 하위 엔티티 객체 삭제 = boolean 이란 뜻
                                             //  - removePoster(Long ino)를 실행하므로써 특정 Poster 근 더 이상
                                             //    존재할 필요가 없어지므로 삭제되는 것이다!
    )
    private List<Poster> posterList = new ArrayList<>();

    public void addPoster(Poster poster){
        poster.setIdx(this.posterList.size());
        poster.setMovie(this);
        posterList.add(poster);
    }

    /**
     * 해당 Method 는 Poster 의 ino 를 찾아 PosterList에서 삭제하는 기능을하는
     * Method 이다 아래와 같은 로직을 진행하는 이유는
     *  - 하위 Entity를 삭제할때는 setMovie(null); 과 같이 참조관계를 완전히 끊어 준 후
     *    posterList.remove(p) 해당 객체를 삭제해줘야함!
     *  - Poster 객체들 에서도 idx 로  서로 의 순서를 관리 하고있었기에
     *    다시 한번 번로를 재정의 해줌 changeIdx();
     * */
    public void  removePoster(Long ino){
        Optional<Poster> result = this.posterList.stream()
                .filter(p-> Objects.equals(p.getIno(), ino)).findFirst();

        result.ifPresent(p->{
            p.setMovie(null);
            this.posterList.remove(p);
        });
        //포스터 번호 변경
        changeIdx();
    }

    private void changeIdx(){
        for(int i = 0 ; i < this.posterList.size(); i++){
            this.posterList.get(i).setIdx(i);
        }
    }

}

