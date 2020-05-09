CREATE TABLE `t_microservice_timer_task` (
  `id` varchar(40) DEFAULT NULL COMMENT 'id',
  `desc` varchar(50) DEFAULT NULL COMMENT '任务描述',
  `class_name` varchar(200) DEFAULT NULL COMMENT '处理任务的类名',
  `cron_expression` varchar(20) DEFAULT NULL COMMENT 'cron表达式：秒 分 时 日 月 周 年',
  `status` int(11) DEFAULT NULL COMMENT '任务状态：0关闭，1开启',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='定时任务配置表';

INSERT INTO `t_microservice_timer_task` VALUES ('a7b67b30-901d-11ea-9f84-5254e080846c', '测试使用可删', 'com.open.boot.batch.batch.TimerDataBatchJob', '00 01 18 * * ? *', 2, '2020-5-7 12:46:03', '2020-5-8 17:59:37');

INSERT INTO `t_microservice_timer_task` (`id`, `desc`, `class_name`, `cron_expression`, `status`, `create_time`, `update_time`)
VALUES (UUID(), '测试使用可删1', 'com.open.boot.batch.batch.TimerDataBatchJobTest', '00 45 16 * * ? *', '2', NOW(), NOW());
