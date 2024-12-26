package com.yoo.p6spy.config;

import com.p6spy.engine.spy.P6SpyOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class P6spyLogMessageFormatConfig {
    @PostConstruct
    public void configureP6SpyLogging() {
        String formatClassName = CustomP6spySqlFormat.class.getCanonicalName();
        log.info("P6Spy Log Message Format set to: {}", formatClassName);
        P6SpyOptions.getActiveInstance().setLogMessageFormat(formatClassName);
    }
}
