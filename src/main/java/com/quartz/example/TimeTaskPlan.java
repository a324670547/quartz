package com.quartz.example;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 周期任务计划
 */
public class TimeTaskPlan {
	final static Logger logger = LoggerFactory.getLogger(TimeTaskPlan.class);

	/**
	 * 周期任务定制方法
	 * @param timePeriodicTask 周期计划字符串
	 * @throws Exception
	 */
	public void go(String timePeriodicTask) throws Exception {
		// 首先，必需要取得一个Scheduler的引用
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		// job
		JobDetail job = newJob(Task.class)
				.withIdentity("job", "group")
				.build();
		CronTrigger trigger = newTrigger()
				.withIdentity("trigger", "group")
				.withSchedule(cronSchedule(timePeriodicTask))
				.build();
		Date ft = sched.scheduleJob(job, trigger);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		logger.info("定时任务已经已被安排执行于" + sdf.format(ft) + "并且以如下重复规则重复执行: " + trigger.getCronExpression());
		sched.start();
	}

	// 测试
	public static void main(String[] args) throws Exception {
		logger.info("123");
		// 周期计划,每隔10秒执行一次  
		String data = "0/10 * * * * ?";
		// 创建计划对象
		TimeTaskPlan timeTask = new TimeTaskPlan();
		// 执行周期任务
		timeTask.go(data);
	}
}
