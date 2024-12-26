package com.yoo.mybatis.repository;

import com.yoo.mybatis.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface MemberRepository {
    List<MemberVO> getAllList();
    MemberVO fineById(String id);
}
