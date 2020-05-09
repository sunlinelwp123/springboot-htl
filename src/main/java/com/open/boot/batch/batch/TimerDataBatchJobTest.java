package com.open.boot.batch.batch;

import com.open.boot.util.spring.SpringContextHolder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class TimerDataBatchJobTest implements Job {
    private static final Logger log = LoggerFactory.getLogger(TimerDataBatchJobTest.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private org.springframework.batch.core.Job job;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("开始创建job");
        try{
            init();
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        }catch (Exception e){
            e.printStackTrace();
            log.info("创建job失败");
        }
    }


    public void init() {
        job = SpringContextHolder.getBean("testHandleJob");
        jobLauncher =(JobLauncher) SpringContextHolder.getBean("jobLauncher",JobLauncher.class);
    }

}
