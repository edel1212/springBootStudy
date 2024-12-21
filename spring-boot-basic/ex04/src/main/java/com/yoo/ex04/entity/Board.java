package com.yoo.ex04.entity;

import lombok.*;
import org.hibernate.id.IdentityGenerator;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne (fetch =FetchType.LAZY)
    @ToString.Exclude
    private Member writer;

    public void changeTitle(String title) {this.title = title;}

    public void changeContent(String content) {this.content = content;}
}
