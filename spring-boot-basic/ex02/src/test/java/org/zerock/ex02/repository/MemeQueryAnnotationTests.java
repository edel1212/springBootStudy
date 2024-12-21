package org.zerock.ex02.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.ex02.entity.Memo;

import java.util.List;

@SpringBootTest
public class MemeQueryAnnotationTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void getListWithQueryAno(){
        List<Memo> list =  memoRepository.getListDesc();
        list.forEach(System.out::println);
    }

    @Test
    void updateMemoTextWithQueryAno() {
        int result = memoRepository.updateMemoText(40L, "흑곰2");
    }

    @Test
    void updateMemoTextWithQueryAnoObjVer() {
        Memo param = Memo.builder().mno(10L).memoText("흑곰2").build();
        System.out.println(param.getMemoText());
        System.out.println(param.getMno());
        int result = memoRepository.updateMemoTestWithObj(param);
    }

    @Test
    void getListWithQueryAnoReturnPage() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Memo> page = memoRepository.getListWithQuery(45L,pageable);
        page.forEach(System.out::println);
    }

    @Test
    public void getListWitQueryObjectTest(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Object[]> result = memoRepository.getListWithQueryObject(56L, pageable);
        result.stream().forEach(System.out::println);
    }

    @Test
    void getListWithNativeQuery() {
        List<Object[]>  result = memoRepository.getNativeResult();
        result.forEach(System.out::println);
    }
}
