package com.yoo.p6spy.config;

import com.p6spy.engine.spy.P6SpyOptions;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Log4j2
@Configuration
public class P6spyLogMessageFormatConfig {
    // 애플리케이션이 완전히 초기화되거나 리프레시된 후에 특정 작업을 실행할 수 있게 해주는 Spring의 이벤트 리스너 애너테이션
    @EventListener(ContextRefreshedEvent.class)
    public void configureP6SpyLogging() {
        String formatClassName = CustomP6spySqlFormat.class.getCanonicalName();
        log.info("P6Spy Log Message Format set to: {}", formatClassName);
        P6SpyOptions.getActiveInstance().setLogMessageFormat(formatClassName);
    }
}
