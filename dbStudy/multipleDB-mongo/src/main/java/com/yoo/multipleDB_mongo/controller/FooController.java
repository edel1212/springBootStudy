package com.yoo.multipleDB_mongo.controller;

import com.yoo.multipleDB_mongo.entity.Dog;
import com.yoo.multipleDB_mongo.entity.Member;
import com.yoo.multipleDB_mongo.repository.DogRepository;
import com.yoo.multipleDB_mongo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FooController {

    // Maraidb
    private final DogRepository dogRepository;
    // MongoDB
    private final MemberRepository memberRepository;

    @GetMapping("/dog")
    public ResponseEntity<List<Dog>> getAll(){
        return ResponseEntity.ok(dogRepository.findAll());
    }

    @GetMapping("/member")
    public ResponseEntity<List<Member>> getAllMember(){
        return ResponseEntity.ok(memberRepository.findAll());
    }
}
