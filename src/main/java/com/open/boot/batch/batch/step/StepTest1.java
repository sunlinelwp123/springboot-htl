package com.open.boot.batch.batch.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class StepTest1 implements Tasklet, StepExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(StepTest1.class);
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("StepTest1......beforeStep.......");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("StepTest1......afterStep.......");
        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("StepTest1......execute.......");
        return RepeatStatus.FINISHED;
    }
}
