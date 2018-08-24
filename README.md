# quartz

## quartz周期计划开源框架 项目演示

# Quartz定时任务

> 一个定时周期任务的开源框架

------

[toc]

## 简介

uartz是OpenSymphony开源组织在Job scheduling领域又一个开源项目，它可以与J2EE与J2SE应用程序相结合也可以单独使用。Quartz可以用来创建简单或为运行十个，百个，甚至是好几万个Jobs这样复杂的程序。Jobs可以做成标准的Java组件或 EJBs。

------

## 使用例子

### maven引用

创建一个Maven项目，并引入一下jar包

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.test</groupId>
	<artifactId>quartz</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<dependencies>
		<dependency>
			<groupId>org.quartz-scheduler</groupId>
			<artifactId>quartz</artifactId>
			<version>2.3.0</version>
		</dependency>
		<dependency>
			<groupId>log4j</groupId>
			<artifactId>log4j</artifactId>
			<version>1.2.17</version>
		</dependency>
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-log4j12</artifactId>
			<version>1.7.7</version>
		</dependency>
	</dependencies>
</project>
```

### 配置log4j

创建一个log4j.properties文件，放置到resources目录下

```xml
### set log levels - for more verbose logging change 'info' to 'debug' ###
#\u8FD9\u91CC\u6307\u5B9A\u4E86\u8F93\u51FAinfo\u4EE5\u4E0A\u7EA7\u522B\u7684\u4FE1\u606F\uFF0C\u5E76\u53EF\u4EE5\u8F93\u51FA\u5230\u63A7\u5236\u53F0stdout\uFF0C\u4EE5\u53CAfile\u4E2D
log4j.rootLogger=info,stdout,file

### direct log messages to stdout ###
#\u63A7\u5236\u53F0
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy/MM/dd HH:mm:ss,SSS} %5p %c{1}:%L - %m%n

### direct messages to file mylog.log ###
#\u6587\u4EF6 \u53EA\u8F93\u51FA\u5230\u4E00\u4E2A\u6587\u4EF6\u4E2D
log4j.appender.logFile=org.apache.log4j.FileAppender
log4j.appender.logFile.File=./logs/mylog.txt
log4j.appender.logFile.encoding=UTF-8
log4j.appender.logFile.layout=org.apache.log4j.PatternLayout
log4j.appender.logFile.layout.ConversionPattern=%d{yyyy/MM/dd HH:mm:ss,SSS} %5p %c{1}:%L - %m%n


### the file overd some size will create a new log file ###
#\u6EDA\u52A8\u6587\u4EF6 \u53EF\u4EE5\u8BBE\u7F6E\u6587\u4EF6\u7684\u5927\u5C0F\uFF08\u8D85\u8FC7\u5927\u5C0F\u7684\u81EA\u52A8\u521B\u5EFA\u65B0\u7684\u65E5\u5FD7\u6587\u4EF6\u6765\u5B58\u653E\uFF09\uFF0C\u4EE5\u53CA\u6587\u4EF6\u7684\u4E2A\u6570\uFF08\u6587\u4EF6\u4E2A\u6570\u8D85\u8FC7\u540E\u4E0D\u518D\u91CD\u65B0\u521B\u5EFA\uFF0C\u4F1A\u5C06\u65B0\u7684\u5185\u5BB9\u4ECE\u7B2C\u4E00\u4E2A\u6587\u4EF6\u5F00\u59CB\u5B58\u653E\uFF09
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.Append=true
log4j.appender.file.File=./logs/log.txt
log4j.appender.file.encoding=UTF-8
log4j.appender.file.MaxFileSize=10MB
log4j.appender.file.MaxBackupIndex=10
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{yyyy/MM/dd HH:mm:ss,SSS} %5p %c{1}:%L - %m%n


### error information ### 
#\u6587\u4EF6 \u53EA\u8F93\u51FAerror\u4FE1\u606F\u5230\u6B64\u6587\u4EF6
log4j.appender.errorfile=org.apache.log4j.FileAppender 
log4j.appender.errorfile.File=./logs/errlog.txt
log4j.appender.errorfile.Threshold=ERROR
log4j.appender.errorfile.Append=false 
log4j.appender.errorfile.layout=org.apache.log4j.PatternLayout 
log4j.appender.errorfile.layout.ConversionPattern=%d{yyyy/MM/dd HH\:mm\:ss,SSS} %5p %c{1}\:%L - %m%n
```

### 创建周期任务计划

```java
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

```

### 创建任务

```java
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
```

------

## 时间设定

**Quartz 定时器的时间设置**

时间大小由小到大排列，从秒开始，顺序为 秒，分，时，天，月，年    *为任意 ？为无限制。 

```xml
具体时间设定可参考
"0/10 * * * * ?" 每10秒触发
"0 0 12 * * ?" 每天中午12点触发 
"0 15 10 ? * *" 每天上午10:15触发 
"0 15 10 * * ?" 每天上午10:15触发 
"0 15 10 * * ? *" 每天上午10:15触发 
"0 15 10 * * ? 2005" 2005年的每天上午10:15触发 
"0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发 
"0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发 
"0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发 
"0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发 
"0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发 
"0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发 
"0 15 10 15 * ?" 每月15日上午10:15触发 
"0 15 10 L * ?" 每月最后一日的上午10:15触发 
"0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发 
"0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发 
"0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
"0 0 06,18 * * ?"  在每天上午6点和下午6点触发 
"0 30 5 * * ? *"   在每天上午5:30触发
"0 0/3 * * * ?"    每3分钟触发
```