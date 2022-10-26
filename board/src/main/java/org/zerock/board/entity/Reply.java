package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer;
    
    @ManyToOne(fetch = FetchType.LAZY) // N : 1
    @ToString.Exclude //ToString 에서 제외시킴 - 이유는 해당 변수를 호출하기 위해서는 데이타ㅓ베이스에 연결이 필요하기 때문임
    private Board board; //연관관계 지정 -- > 자동으로 커럼명은 board_bno 로 됨 변수명_PK 로 된다

}
