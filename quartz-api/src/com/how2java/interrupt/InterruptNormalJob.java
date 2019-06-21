package com.how2java.interrupt;

import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
/**
 * Author:Created by wx on 2019/6/19
 * Desc:
 */
public class InterruptNormalJob implements InterruptableJob {

        //必须实现InterruptableJob 而非 Job才能够被中断
        private boolean stop = false;
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            while(true){

                if(stop)
                    break;
                try {
                    System.out.println("每隔1秒，进行一次检测，看看是否停止");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("持续工作中。。。");
            }

        }
        public void interrupt() throws UnableToInterruptJobException {
            System.out.println("被调度叫停");
            stop = true;
        }

}
