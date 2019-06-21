package com.how2java.exception;

import com.how2java.concurrent.PrintJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author:Created by wx on 2019/6/19
 * Desc:
 */
public class ExceptionHandler2 implements Job {
    private Logger log = LoggerFactory.getLogger(PrintJob.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("ExceptionHandler2执行");
        int i = 0;
        try {
            JobDetail jobDetail = context.getJobDetail();
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            String text = jobDataMap.getString("text");

            int error = 1/i;

            System.out.println("jobDetail中的参数:"+text);
        } catch (Exception e) {
            JobExecutionException jobException = new JobExecutionException(e);
            i = 1;
            jobException.setRefireImmediately(true);
            throw jobException;
        }
    }
}
