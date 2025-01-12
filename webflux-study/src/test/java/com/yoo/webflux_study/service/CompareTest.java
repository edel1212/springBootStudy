package com.yoo.webflux_study.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CompareTest {

    @Autowired
    private AsyncSampleServiceImpl asyncSampleService;
    @Autowired
    private SyncServiceImpl syncService;

    @Test
    public void testAsync() {
        // 비동기식 테스트
        long asyncStart = System.currentTimeMillis();
        System.out.println("asyncStart ::: " + asyncStart);
        for (int i = 0; i < 5; i++) {
            System.out.println(asyncSampleService.getPathVariable("path" + i));  // 동기식 호출
        }
        System.out.println("멈추지 않고 하위 로직 진행 가능!!!");
        System.out.println("멈추지 않고 하위 로직 진행 가능!!!");
        System.out.println("멈추지 않고 하위 로직 진행 가능!!!");
        System.out.println("멈추지 않고 하위 로직 진행 가능!!!");
        System.out.println("멈추지 않고 하위 로직 진행 가능!!!");
        long asyncElapsed = System.currentTimeMillis() - asyncStart;
        System.out.println("Total Async Time: " + asyncElapsed + "ms");

    }

    @Test
    public void testSync() {
        // 비동기식 테스트
        long syncStart = System.currentTimeMillis();
        System.out.println("syncStart ::: " + syncStart);
        for (int i = 0; i < 5; i++) {
            syncService.getPathVariable("path" + i);  // 비동기식 호출
        }
        System.out.println("멈추지 않고 하위 로직 진행 불가능!!!");
        long syncElapsed = System.currentTimeMillis() - syncStart;
        System.out.println("Total Sync Time: " + syncElapsed + "ms");
    }
}
