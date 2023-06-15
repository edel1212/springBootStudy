package com.yoo.quartzStudy;

import com.yoo.quartzStudy.job.TestJobA;
import org.quartz.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuartzStudyApplication {

	public static void main(String[] args) throws SchedulerException {
		SpringApplication.run(QuartzStudyApplication.class, args);
	}

}
