package test;

import com.how2java.DateFormat;
import com.how2java.concurrent.PrintJob;
import com.how2java.listenerexample.MailJob;
import com.how2java.exception.ExceptionHandler2;
import com.how2java.listenerexample.MailJobListener;
import org.junit.Test;
import org.quartz.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Author:Created by wx on 2019/6/19
 * Desc:
 */
public class QuartzTests {

    private Logger log  = LoggerFactory.getLogger(QuartzTests.class);

    public void call(Class jobClass,int intervalSeconds,int repeatCount,int sleepSeconds){
        try {
            // 1.创建调度器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            // 2.创建触发器
            Trigger trigger = newTrigger()
                    .withIdentity("T1", "trigger_group1")
                    .startNow()
                    .withSchedule(simpleSchedule().withIntervalInSeconds(intervalSeconds).withRepeatCount(repeatCount))
                    .build();
            // 3.定义JobDetail
            JobDetail jobDetail = newJob(jobClass) //指定干活的类MailJob
                    .withIdentity("J1", "printJob_group1") //定义任务名称和分组
                    .usingJobData("text", "Are you ok ?") //定义属性
                    .build();
            // 4.由调度器绑定job和触发器
            scheduler.scheduleJob(jobDetail, trigger);
            // 5.启动job
            scheduler.start();

            Thread.sleep(sleepSeconds);
            // 6.等待所有Job执行结束后，关闭
            scheduler.shutdown(true);
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void callWithInterrupt(Class jobClass,int intervalSeconds,int repeatCount,int sleepSeconds){
        log.error("callWithInterrupt执行:"+ DateFormat.formateDate(new Date()));
        try {
            // 1.创建调度器
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            Date firstFireTime = DateBuilder.nextGivenSecondDate(null, 5);

            // 2.创建触发器
            Trigger trigger = newTrigger()
                    .withIdentity("T1", "trigger_group1")
                    .startAt(firstFireTime)
                    .withSchedule(simpleSchedule().withIntervalInSeconds(intervalSeconds).withRepeatCount(repeatCount))
                    .build();

//            JobDataMap jobDataMap1 = trigger.getJobDataMap();
//            jobDataMap1.putAsString("aaa", true);

            // 3.定义JobDetail
            JobDetail jobDetail = newJob(jobClass) //指定干活的类MailJob
                    .withIdentity("J1", "printJob_group1") //定义任务名称和分组
                    .usingJobData("text", "Are you ok ?") //定义属性
                    .build();
            // 4.由调度器绑定job和触发器
            scheduler.scheduleJob(jobDetail, trigger);
            // 5.启动job
            scheduler.start();
            // 给job一定执行时间，保证至少执行一次
            Thread.sleep(sleepSeconds);
            boolean isInterrupt = scheduler.interrupt(jobDetail.getKey());
            assert isInterrupt;
            // 再次休眠，会解除Job的中断状态
//            Thread.sleep(sleepSeconds*2);
            // 6.等待所有Job执行结束后，关闭
            scheduler.shutdown(true);
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void callListenerExample() throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail = newJob(MailJob.class)
                                .withIdentity("listenerExampleJob","group1")
                                .withDescription("用于测试JobListener")
                                .usingJobData("email", "10086")
                                .build();

        CronTrigger cronTrigger = newTrigger()
                .withIdentity("listenerExampleTrigger", "group1")
                .startAt(DateBuilder.futureDate(3, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ? "))
                .build();

        MailJobListener mailJobListener= new MailJobListener();
        KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(jobDetail.getKey());
        scheduler.getListenerManager().addJobListener(mailJobListener, keyMatcher);

        scheduler.scheduleJob(jobDetail, cronTrigger);

        scheduler.start();

        Thread.sleep(20000);

        scheduler.shutdown(true);
    }
    @Test
    public void testExample2(){
        call(PrintJob.class, 2, 3, 20000);
    }
    @Test
    public void testExceptionHandler(){
//        call(ExceptionHandler1.class, 2, 1, 4000);
        call(ExceptionHandler2.class, 2, 2, 3000);
    }
    @Test
    public void testInterrupt(){
        callWithInterrupt(PrintJob.class, 3, 5, 6000*2);
    }

    @Test
    public void testJobListener(){
        try {
            callListenerExample();
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
