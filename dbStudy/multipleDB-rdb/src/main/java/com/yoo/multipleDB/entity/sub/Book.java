package com.yoo.multipleDB.entity.sub;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "book")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class Book {
    @Id
    private Long id;

    @Column(name = "bookCnt")
    @Getter
    private int bookCnt;
}
