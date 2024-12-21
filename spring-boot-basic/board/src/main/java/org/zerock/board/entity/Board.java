package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Board  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;
    
    //@ManyToOne  // N : 1 관계임!          ✔ 그냥 사용시 Eager loading 이 Default 값임
    @ManyToOne (fetch = FetchType.LAZY) // ✔ 명시적으로  Lazy Loading 지정!
    @ToString.Exclude                   // 해당 변수는 ToString 에서 제외 시킴!
    private Member writer;              //연관관계 지정 -- > 자동으로 커럼명은 writer_email 로 됨 변수명_PK 로 된다

    //////////////////////////////////////
    // Board 수정을 위한 Method
    //////////////////////////////////////
    // 하위 2개의 메서드는 게시글 수정에 사용된
    // Entity 자체 수정은 좋지 못하기에
    // Setter를 사용하지 않고 필요한 Method만을 사용함
    public void changeTitle(String title){
        this.title = title;
    }
    public void changeContent(String content){
        this.content = content;
    }

}
