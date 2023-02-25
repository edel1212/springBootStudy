package com.yoo.toy.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity //Entity Class 지정
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity{
    @Id //PK
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    /**
     * @Description : club_member_role_set 으로 Table이 만들어짐
     *                -  ClubMember(email) 기준으로 1 : N Table 생성
     *                - 해당  Table 의 FK : club_member_email
     *
     *              @ElementCollection 란❔
     *              - 값 타입 컬렉션을 매핑할때 사용함.
     *                => RDB에서는 컬렉션과 같은 형태의 데이터를 컬럼에 저장할 수 없기 떄문에,
     *                   별도의 테이블을 생성하여 컬렉션을 관리하위한 테이블이라는 설정이
     *
     *             @Builder.Default 란❔
     *             - 빌더 패텅을 통해 인스턴스를 만들때 특정 필드를 특정 값으로 초기화 하고 싶을때 사용
     * */
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ClubMemberRole> roleSet = new HashSet<>();

    //해당 메서드는 권한을 넣을때 사용된다.
    public void addMemberRole(ClubMemberRole clubMemberRole){
        roleSet.add(clubMemberRole);
    }
}
