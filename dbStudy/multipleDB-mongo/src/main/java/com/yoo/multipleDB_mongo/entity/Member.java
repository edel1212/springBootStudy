package com.yoo.multipleDB_mongo.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Document("member")
public class Member {
    // MongoDB의 DefaultId
    @Id
    private String id;

    // 고유값 성정
    @Indexed(unique = true)
    private String accountId;

    @Setter
    private int age;

    @Setter
    private LocalDateTime joinedDate;
}
