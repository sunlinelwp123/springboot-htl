/**
 * Copyright (c) 2018 Sunshine Insurance Group Inc
 * Created by xiaozhendong on 2018/7/29.
 */
package com.open.boot.util.timertask;

import com.alibaba.fastjson.JSON;
import com.open.boot.util.dao.TimerTaskRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description 定时任务的管理线程，定时任务的创建，删除，修改都在这里，这个类不需要交由spring管理
 */
public class KeeperTask implements Job {
    private static final Logger log = LoggerFactory.getLogger(KeeperTask.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TimerTaskRepository timerTaskRepository = (TimerTaskRepository) context.getJobDetail().getJobDataMap().get(CronTimerTask.KEEPER_NAME);
        List<TimerTaskModel> dbTimerTaskList = timerTaskRepository.findAllTimerTask("2");
        for (TimerTaskModel dbTimerTask : dbTimerTaskList) {
            String cacheListStr = JSON.toJSONString(CronTimerTask.cacheTimerTaskList);
            String db = dbTimerTask.getId();
            if(cacheListStr.contains(db)){
                for ( TimerTaskModel cacheTimerTask :CronTimerTask.cacheTimerTaskList) {
                    if(dbTimerTask.getId().equals(cacheTimerTask.getId())){
                        if (dbTimerTask.getStatus().equals(cacheTimerTask.getStatus()) && dbTimerTask.getCronExpression().equals(cacheTimerTask
                                .getCronExpression()) && dbTimerTask.getClassName().equals(cacheTimerTask.getClassName())) {
                            break;
                        }
                        refreshTimerTask(dbTimerTask,cacheTimerTask);
                    }
                }
            }else{
                createCronTimerTask(dbTimerTask);
            }
        }
    }

    /**
     * @descrption 将数据库中存储的定时任务和缓存中存储的定时任务进行对比如果不一样，则证明修改了，就要刷新缓存缓存中的定时任务
     * @param dbTimerTask 数据库中存储的定时任务
     * @param cacheTimerTask 缓存中存储的定时任务
     */
    private void refreshTimerTask(TimerTaskModel dbTimerTask, TimerTaskModel cacheTimerTask) {

        try {
            destoryCronTimerTask(cacheTimerTask);
            createCronTimerTask(dbTimerTask);
            log.info("将定时任务：{}修改为：{}", cacheTimerTask, dbTimerTask);
        } catch (Exception e) {
            log.error("修改缓存中的任务{}为数据库中的任务{}异常，异常原因为：", cacheTimerTask, dbTimerTask, e);
        }
    }

    /**
     * @descrption 创建定时任务
     */
    private void createCronTimerTask(TimerTaskModel timerTask) {
            log.info("开始创建定时任务：{}", JSON.toJSONString(timerTask));
            try {
                Class<Job> clazz = (Class<Job>) Class.forName(timerTask.getClassName());
                JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(timerTask.getId()).build();
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(timerTask.getCronExpression());
                CronTrigger trigger = TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).build();
                Scheduler scheduler = CronTimerTask.stdSchedulerFactory.getScheduler();
                scheduler.scheduleJob(jobDetail, trigger);
                scheduler.start();
                log.info("创建定时任务：{}", JSON.toJSONString(timerTask));
                CronTimerTask.cacheTimerTaskList.add(timerTask);
            } catch (Exception e) {
                log.error("创建定时任务：{}时异常，异常原因为：", timerTask, e);
            }

    }


    /**
     * @descrption 销毁一个定时任务
     */
    private void destoryCronTimerTask(TimerTaskModel timerTask) {

        try {
            Scheduler scheduler = CronTimerTask.stdSchedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(timerTask.getId());
            scheduler.deleteJob(jobKey);
            log.info("销毁定时任务：{}", timerTask);
            CronTimerTask.cacheTimerTaskList.remove(timerTask);
        } catch (Exception e) {
            log.error("销毁定时任务：{}时异常，异常原因为：", timerTask, e);
        }
    }


}
