package org.zerock.club.check;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootTest
public class BeanCheck {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void beansCheck () throws  Exception{

        if(applicationContext != null){
            String[] beans = applicationContext.getBeanDefinitionNames();
            Arrays.stream(beans).forEach(System.out::println);
        }

    }

}
