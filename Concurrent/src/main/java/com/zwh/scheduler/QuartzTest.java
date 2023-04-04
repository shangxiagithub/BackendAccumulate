package com.zwh.scheduler;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.Date;

public class QuartzTest implements Job {

	@Override
	//该方法实现需要执行的任务
	public void execute(JobExecutionContext executionContext) throws JobExecutionException {
		System.out.println("Generating report - "
				+ executionContext.getJobDetail() + ", type ="
				+ executionContext.getJobDetail().getJobDataMap().get("type"));
		System.out.println(new Date().toString());
	}
	public static void main(String[] args) {
		try {
			// 创建一个Scheduler
			SchedulerFactory schedFact = 
			new org.quartz.impl.StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			sched.start();
			// 创建一个JobDetail，指明name，groupname，以及具体的Job类名，
			//该Job负责定义需要执行任务
			JobDetailImpl jobDetail = new JobDetailImpl();
			jobDetail.setName("myJob");
			jobDetail.setGroup("myJobGroup");
			jobDetail.setJobClass(QuartzTest.class);
			jobDetail.getJobDataMap().put("type", "FULL");
            // 创建一个每周触发的Trigger，指明星期几几点几分执行
			CronTriggerImpl trigger = new CronTriggerImpl();
			trigger.setGroup("myTriggerGroup");
			// 指明trigger的name
			trigger.setName("myTrigger");
			trigger.setCronExpression(new CronExpression("0 24 15 ? * TUE"));
			// 用scheduler将JobDetail与Trigger关联在一起，开始调度任务
			sched.scheduleJob(jobDetail, trigger);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
} 
//Output:
//Generating report - myJobGroup.myJob, type =FULL
//Tue Feb 8 16:38:00 CST 2011
//Generating report - myJobGroup.myJob, type =FULL
//Tue Feb 15 16:38:00 CST 2011