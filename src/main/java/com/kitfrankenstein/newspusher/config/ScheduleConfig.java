package com.kitfrankenstein.newspusher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于接口的动态定时任务配置，和@Scheduled注解无关
 *
 * @author Kit
 * @date: 2019/8/14 12:26
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    /**
     * 注册定时任务
     *
     * @see SchedulingConfigurer
     * @see ScheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(4);
    }
}
