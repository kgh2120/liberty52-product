package com.liberty52.product.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
public class SchedulerConfig implements AsyncConfigurer, SchedulingConfigurer {

    public ThreadPoolTaskScheduler customThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        int poolSize = scheduler.getPoolSize();
        log.info("MY-SCHEDULER set {} pool size", poolSize);
        scheduler.setThreadNamePrefix("MY-SCHEDULER-");
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(customThreadPoolTaskScheduler());
    }

    @Override
    public Executor getAsyncExecutor() {
        return customThreadPoolTaskScheduler();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("Method name : {}, param Count : {}\nException Cause -{}", method.getName(), params.length, ex.getMessage());
    }
}
