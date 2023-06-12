package com.yoo.quartzStudy;

import com.yoo.quartzStudy.job.TestJobA;
import org.quartz.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuartzStudyApplication {

	public static void main(String[] args) throws SchedulerException {
		SpringApplication.run(QuartzStudyApplication.class, args);
	}

}
