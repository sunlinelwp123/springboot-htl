/**
 * Copyright (c) 2018 Sunshine Insurance Group Inc
 * Created by xiaozhendong on 2018/7/28.
 */
package com.open.boot.util.timertask;

import com.open.boot.util.dao.TimerTaskRepository;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 定时任务类初始化类
 * @author 308497741@qq.com
 */
@Component
public class CronTimerTask {
    private static final Logger log = LoggerFactory.getLogger(CronTimerTask.class);

    public static List<TimerTaskModel> cacheTimerTaskList = new ArrayList<TimerTaskModel>();

    public static final StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
    /**
     * 定时任务管理线程扫描任务状态时间间隔
     */
    private static final int DELAYED = 30;

    @Autowired
    private TimerTaskRepository timerTaskRepository;
    /**
     * 管理线程名称
     */
    public static final String KEEPER_NAME = "cronTimerKeeper";

    @PostConstruct
    public void init() throws Exception {
        log.info("开始初始化定时任务");
        log.info("开始初始化定时任务管理线程");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(KEEPER_NAME, timerTaskRepository);
        jobDataMap.put("123",cacheTimerTaskList);
        JobDetail keeperJobDetail = JobBuilder.newJob(KeeperTask.class).withIdentity(KEEPER_NAME).setJobData(jobDataMap).build();
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(DELAYED).repeatForever();
        Trigger keeperTrigger = TriggerBuilder.newTrigger().startNow().withSchedule(simpleScheduleBuilder).build();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        scheduler.scheduleJob(keeperJobDetail, keeperTrigger);
        scheduler.start();
        log.info("始化定时任务管理线程初始化完成");
    }

}