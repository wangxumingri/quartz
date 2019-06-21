package com.how2java.listenerexample;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * Author:Created by wx on 2019/6/20
 * Desc:
 */
public class MailJobListener implements JobListener {
    @Override
    public String getName() {
        return "MailJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        System.out.println("mailJob即将执行"+context.getMergedJobDataMap().getString("email"));
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        System.out.println("TriggerListener拒绝mailJob执行");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        System.out.println("mailJob执行结束");
    }
}
