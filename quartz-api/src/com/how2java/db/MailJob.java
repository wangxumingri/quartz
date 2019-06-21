package com.how2java.db;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.*;

public class MailJob implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	
        JobDetail detail = context.getJobDetail();
        String email = detail.getJobDataMap().getString("email");
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String now = sdf.format(new  Date());
        
        
        
        System.out.printf("给邮件地址 %s 发出了一封定时邮件, 当前时间是: %s (%s)%n" ,email, now,context.isRecovering());
    }
}