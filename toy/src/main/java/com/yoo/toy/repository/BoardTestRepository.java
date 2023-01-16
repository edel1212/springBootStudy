package com.yoo.toy.repository;

import com.yoo.toy.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public interface BoardTestRepository// extends JpaRepository<Board,Long>
        //QuerydslRepositorySupport { // 해당  QuerydslRepositorySuppor는 Class 이기때문에 interface에 상속이 불가능함.
{
    Board search1CheckTest();
}
