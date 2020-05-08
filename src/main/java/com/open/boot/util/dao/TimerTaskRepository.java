package com.open.boot.util.dao;

import com.open.boot.util.timertask.TimerTaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimerTaskRepository extends JpaRepository<TimerTaskModel, Long>, JpaSpecificationExecutor<TimerTaskModel> {

    @Query(value="select * from t_microservice_timer_task where status=?1 ", nativeQuery=true)
    List<TimerTaskModel> findAllTimerTask(String status);

}
