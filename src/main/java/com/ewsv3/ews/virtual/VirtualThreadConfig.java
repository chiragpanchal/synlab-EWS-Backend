package com.ewsv3.ews.virtual;

import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;

//@Configuration
public class VirtualThreadConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(1000);
        // taskExecutor.setThreadFactory(Executors.newVirtualThreadPerTaskExecutor().);
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().factory();
        taskExecutor.setThreadFactory(virtualThreadFactory);

        taskExecutor.initialize();
        return taskExecutor;
    }

}
