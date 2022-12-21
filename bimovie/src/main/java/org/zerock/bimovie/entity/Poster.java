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

}
