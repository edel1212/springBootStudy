package com.yoo.multipleDB_mongo.repository;

import com.yoo.multipleDB_mongo.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {
}
