package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") //ToString 은 항상 exclude
@Builder
public class Board  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;
    
    @ManyToOne  // N : 1 관계임!
    private Member writer; //연관관계 지정 -- > 자동으로 커럼명은 writer_email 로 됨 변수명_PK 로 된다

}
