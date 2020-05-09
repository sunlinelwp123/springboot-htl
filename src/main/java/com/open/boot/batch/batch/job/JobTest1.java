package com.open.boot.batch.batch.job;

import com.open.boot.batch.batch.step.StepTest1;
import com.open.boot.batch.listener.JobListener;
import com.open.boot.batch.model.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class JobTest1 {
    private static final Logger log = LoggerFactory.getLogger(JobTest1.class);
    @Resource
    private JobBuilderFactory jobBuilderFactory;    //用于构建JOB

    @Resource
    private JobListener jobListener;            //简单的JOB listener

    @Resource
    private StepBuilderFactory stepBuilderFactory;  //用于构建Step

    @Bean
    public Tasklet stepTest1(){
        return  new StepTest1();
    }

    @Bean
    public Job testHandleJob() {
        //return null;
        Job job =jobBuilderFactory.get("testHandleJob").
                    incrementer(new RunIdIncrementer()).
                    start(step1()).next(step2()).next(step3()).next(step4()).next(handleDataStep()). //start是JOB执行的第一个step
                    listener(jobListener).      //设置了一个简单JobListener
                    build();
        return job;
    }
    @Bean
    public Step step1(){
        Step step= stepBuilderFactory.get("getData").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step1-->Hello Spring Batch....");
                return RepeatStatus.FINISHED;
            }
        }).build();
        log.info("step1------------");
        return step;
    }

    @Bean
    public Step step2(){
        Step step= stepBuilderFactory.get("getData").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step2-->Hello Spring Batch....");
                return RepeatStatus.FINISHED;
            }
        }).build();
        log.info("step2------------");
        return step;
    }

    @Bean
    public Step step3(){
        Step step= stepBuilderFactory.get("getData").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step3-->Hello Spring Batch....");
                return RepeatStatus.FINISHED;
            }
        }).build();
        log.info("step3------------");
        return step;
    }

    @Bean
    public Step step4(){
        Step step= stepBuilderFactory.get("getData").tasklet(stepTest1()).build();
        log.info("step4------------");
        return step;
    }


    @Bean
    public Step handleDataStep() {
        return stepBuilderFactory.get("getData").
                <Access, Access>chunk(100).        // <输入,输出> 。chunk通俗的讲类似于SQL的commit; 这里表示处理(processor)100条后写入(writer)一次。
                faultTolerant().retryLimit(3).retry(Exception.class).skipLimit(100).skip(Exception.class). //捕捉到异常就重试,重试100次还是异常,JOB就停止并标志失败
                reader(getDataReader()).         //指定ItemReader
                processor(getDataProcessor()).   //指定ItemProcessor
                writer(getDataWriter()).         //指定ItemWriter
                build();
    }

    @Resource
    private EntityManagerFactory emf;           //注入实例化Factory 访问数据

    @Bean
    public ItemReader<? extends Access> getDataReader() {
        //读取数据,这里可以用JPA,JDBC,JMS 等方式 读入数据
        JpaPagingItemReader<Access> reader = new JpaPagingItemReader<>();
        //这里选择JPA方式读数据 一个简单的 native SQL
        String sqlQuery = "SELECT * FROM access";
        try {
            JpaNativeQueryProvider<Access> queryProvider = new JpaNativeQueryProvider<>();
            queryProvider.setSqlQuery(sqlQuery);
            queryProvider.setEntityClass(Access.class);
            queryProvider.afterPropertiesSet();
            reader.setEntityManagerFactory(emf);
            reader.setPageSize(3);
            reader.setQueryProvider(queryProvider);
            reader.afterPropertiesSet();
            //所有ItemReader和ItemWriter实现都会在ExecutionContext提交之前将其当前状态存储在其中,如果不希望这样做,可以设置setSaveState(false)
            reader.setSaveState(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;
    }

    @Bean
    public ItemProcessor<Access, Access> getDataProcessor() {
        return new ItemProcessor<Access, Access>() {
            @Override
            public Access process(Access access) throws Exception {
                log.info("processor data : " + access.toString());  //模拟  假装处理数据,这里处理就是打印一下
                return access;
            }
        };
    }

    @Bean
    public ItemWriter<Access> getDataWriter() {
        return new ItemWriter<Access>() {
            @Override
            public void write(List<? extends Access> list) throws Exception {
                for (Access access : list) {
                    log.info("write data : " + access); //模拟 假装写数据 ,这里写真正写入数据的逻辑
                }
            }
        };
    }
}
