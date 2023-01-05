package com.yoo.toy.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "tbl_guestbook")
public class GuestBook extends BaseEntity{ // @MappedSuperClass 인 BaseEntity  상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gnum;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    /*
     * Entity Class 에서는 가능하면 setter 관련 기능을 만들지 않는것을 권장하지만
     * 현재는 테스트를 위하여 작성함!
     * */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
