package com.how2java.exception;

import com.how2java.concurrent.PrintJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author:Created by wx on 2019/6/19
 * Desc:出现异常后，停止所有调度
 */
public class ExceptionHandler1 implements Job {
    private Logger log = LoggerFactory.getLogger(PrintJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.debug("ExceptionHandler1执行");
        try {
            JobDetail jobDetail = context.getJobDetail();
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            String text = jobDataMap.getString("text");

            int error = 1/0;

            System.out.println("jobDetail中的参数:"+text);
        } catch (Exception e) {
            JobExecutionException jobException = new JobExecutionException(e);
            jobException.setUnscheduleAllTriggers(true); // 停止调度未执行的触发器
            throw jobException;
        }
    }
}
