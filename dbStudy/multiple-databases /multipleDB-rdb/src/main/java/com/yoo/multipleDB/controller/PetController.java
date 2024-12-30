package com.yoo.multipleDB.controller;

import com.yoo.multipleDB.entity.primary.Dog;
import com.yoo.multipleDB.repository.primary.DogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@RestController
public class PetController {

    private final DogRepository dogRepository;

    @GetMapping("/dog")
    public ResponseEntity<List<Dog>> getAllDog(){
        return ResponseEntity.ok(dogRepository.findAll());
    }

    @PostMapping("/dog")
    public ResponseEntity<String> registerDog(){
        dogRepository.save(Dog.builder().age(10).name(UUID.randomUUID().toString().substring(0,5)).build());
        return ResponseEntity.ok("success");
    }

}
