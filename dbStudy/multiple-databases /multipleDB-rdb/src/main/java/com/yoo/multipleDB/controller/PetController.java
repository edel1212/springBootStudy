package com.yoo.multipleDB.controller;

import com.yoo.multipleDB.entity.primary.Dog;
import com.yoo.multipleDB.entity.sub.Book;
import com.yoo.multipleDB.entity.sub.Cat;
import com.yoo.multipleDB.repository.mapper.CatMybatisRepository;
import com.yoo.multipleDB.repository.primary.DogRepository;
import com.yoo.multipleDB.repository.sub.BookRepository;
import com.yoo.multipleDB.repository.sub.CatRepository;
import com.yoo.multipleDB.vo.CatVO;
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
    private final CatRepository catRepository;
    private final BookRepository bookRepository;
    private final CatMybatisRepository catMybatisRepository;

    @GetMapping("/dog")
    public ResponseEntity<List<Dog>> getAllDog(){
        return ResponseEntity.ok(dogRepository.findAll());
    }

    @PostMapping("/dog")
    public ResponseEntity<String> registerDog(){
        dogRepository.save(Dog.builder().age(10).name(UUID.randomUUID().toString().substring(0,5)).build());
        return ResponseEntity.ok("success");
    }

    @GetMapping("/cat")
    public ResponseEntity<List<Cat>> getAllCat(){
        return ResponseEntity.ok(catRepository.findAll());
    }

    @PostMapping("/cat")
    public ResponseEntity<String> registerCat(){
        catRepository.save(Cat.builder().age(10).name(UUID.randomUUID().toString().substring(0,5)).breed("none").build());
        return ResponseEntity.ok("success");
    }

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getAllBook(){
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @GetMapping("/cat-mybatis")
    public ResponseEntity<List<CatVO>> getAllCatByMybatis(){
        return ResponseEntity.ok(catMybatisRepository.getAllList());
    }
}
