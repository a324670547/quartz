package com.quartz.example;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *	要执行的周期任务
 */
public class Task implements Job {
	final static Logger logger = LoggerFactory.getLogger(Task.class);
	
	/**
	 * 执行的任务
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("执行周期任务");
	}

}
