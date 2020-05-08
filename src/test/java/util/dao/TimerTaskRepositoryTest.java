package util.dao;

import base.BaseSpring;
import com.alibaba.fastjson.JSON;
import com.open.boot.util.dao.TimerTaskRepository;
import com.open.boot.util.timertask.TimerTaskModel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
public class TimerTaskRepositoryTest extends BaseSpring {
    private static final Logger log = LoggerFactory.getLogger(TimerTaskRepositoryTest.class);

    @Autowired
    private TimerTaskRepository timerTaskRepository;

    @Test
    public void findAllTimerTaskTest() {
        List<TimerTaskModel> dbTimerTaskList = timerTaskRepository.findAllTimerTask("1");
        for (TimerTaskModel dbTimerTask : dbTimerTaskList) {
            log.info("定时任务："+JSON.toJSONString(dbTimerTask));
        }
    }
}
