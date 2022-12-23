package org.zerock.bimovie.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
@Table(name = "tbl_poster")
public class Poster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;
    
    private String fname;
    
    // 포스터 순번
    private int idx;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY) //난 하나지만 movie 는 여러개가 가능함! ->  ManyToOne
    private Movie movie;

    /**
     * @Description : Entity Class 에서는 SetMethod 는 가급적 사용하면 좋지않음
     *               - 하위 엔티티(현재Class)는 상위 엔티티(Movie)에서 관리가 편하도록 약간이 SetMethod를
     *                 사용하는 경우가 있하다
     * */
    
    public void setIdx(int idx){
        this.idx = idx;
    }

    public void setMovie(Movie movie){
        this.movie = movie;
    }
}
