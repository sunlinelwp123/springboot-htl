package com.open.boot.util.timertask;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @308497741@qq.com
 */
@Entity
@Table(name = "t_microservice_timer_task")
public class TimerTaskModel implements Serializable {


    private static final long serialVersionUID = 1532850004243L;


    /**
     * id
     * isNullAble:1
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * 任务描述
     * isNullAble:1
     */
    private String desc;

    /**
     * 处理任务的类名
     * isNullAble:1
     */
    private String className;

    /**
     * cron表达式：秒 分 时 日 月 周 年
     * isNullAble:1
     */
    private String cronExpression;

    /**
     * 任务状态：0关闭，1开启
     * isNullAble:1
     */
    private Integer status;

    /**
     * 创建时间
     * isNullAble:1
     */
    private Date createTime;

    /**
     * 修改时间
     * isNullAble:1
     */
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
