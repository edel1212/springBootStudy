package com.yoo.quartzStudy;

import com.yoo.quartzStudy.job.TestJobA;
import org.quartz.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuartzStudyApplication {

	public static void main(String[] args) throws SchedulerException {
		SpringApplication.run(QuartzStudyApplication.class, args);

		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		Scheduler scheduler = schedFact.getScheduler();
		scheduler.start();

		JobDetail job = JobBuilder.newJob(TestJobA.class)
				.withIdentity("DumbJob")
				.build();

		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("DumbTrigger")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(2)
						.repeatForever())
				.build();

		scheduler.scheduleJob(job, trigger);

	}

}
