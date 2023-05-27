package com.yoo.instarServer.service;

import com.yoo.instarServer.mapper.user.TestAge;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class YooServiceImpl implements YooService{

    private final TestAge testAge;

    @Override
    public int getAage() {
        return testAge.getAge();
    }
}
