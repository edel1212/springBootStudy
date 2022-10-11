package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer;
    
    @ManyToOne // N : 1
    private Board board; //연관관계 지정 -- > 자동으로 커럼명은 board_bno 로 됨 변수명_PK 로 된다

}
