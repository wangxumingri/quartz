package com.how2java.concurrent;

import com.how2java.DateFormat;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Author:Created by wx on 2019/6/19
 * Desc:
 */
@DisallowConcurrentExecution
public class PrintJob implements InterruptableJob {
    private Logger log = LoggerFactory.getLogger(PrintJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {


        // 获取时间
        Date previousFireTime = context.getPreviousFireTime(); // 上次执行时间
        Date fireTime = context.getFireTime();// 此次执行时间
        Date nextFireTime = context.getNextFireTime();// 下次执行时间
        Date scheduledFireTime = context.getScheduledFireTime();// 与此次执行时间相同
        // 获取map参数
        JobDetail jobDetail = context.getJobDetail();
        JobKey key = jobDetail.getKey();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        System.out.println(jobDataMap.size());

        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();


        System.out.println("上次执行时间："+DateFormat.formateDate(previousFireTime));
        System.out.println("此次执行时间："+DateFormat.formateDate(fireTime));
        System.out.println("下次执行时间："+DateFormat.formateDate(nextFireTime));
        System.out.println("jobKey:"+key);
        System.out.println("****************************");
        Class<? extends Job> jobClass = jobDetail.getJobClass();
        assert jobClass == PrintJob.class;

        try { //休眠5秒
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.out.println(this.getClass()+"被调度叫停");
    }
}
