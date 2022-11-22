package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
/**
 * @Description : 해당 클래스는 @ManyToMany 대신에 사용하는 방법으로
 *                해당 Class는 중간 다리 역할을 하며 동시에 정보 기록까지 같이 할수있다.
 *
 *                해당 테이블은 매핑 테이블이라 하며 주로 동사나 히스토리를 의미하는 테이블이다
 *                 - 해당 예제에서는 회원이 영화에 대한 평점을 준다를 구성할때 여기서서 <b>평점을 준다</b>
 *                   부분이 해당 Class의 역할이라 볼수있다.
 *
 *                 - 해당 Entity 구조를 보면  Movie -< Review >- m_member
 *                   로 Review 테이블을 중간에 두고 서로를 연결하고 있는 구조이다.
 *
 *                 ✔ 여기서 잊으면 안되는 Tip
 *                    - FK 기준은 항상 외래키를 가지고 있는 테이블을 기준으로 작성하자!!
 * */
public class Review extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int grade;

    private String text;

    public void changeGrade(int grade){
        this.grade = grade;
    }

    public void changeText(String text){
        this.text = text;
    }

}
