package com.yoo.multipleDB.repository.mapper;

import com.yoo.multipleDB.vo.CatVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CatMybatisRepository {
    List<CatVO> getAllList();
}
