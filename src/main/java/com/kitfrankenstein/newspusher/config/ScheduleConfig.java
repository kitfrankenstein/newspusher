package com.kitfrankenstein.newspusher.config;

import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时任务线程池配置
 *
 * <p>{@link org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor#finishRegistration()}
 * 中会使用SchedulingConfigurer.configureTasks方法给ScheduledTaskRegistrar设置scheduler来多线程的执行
 * 否则实际执行任务时{@link org.springframework.scheduling.config.ScheduledTaskRegistrar#scheduleTasks()}会使用单线程的线程池
 *
 * <p>另外也可以注入{@link TaskScheduler}类型的bean实现多线程执行
 * {@link org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor#finishRegistration()}
 * 中也会处理
 * <p>spring boot自动配置了一个单线程的{@link TaskScheduler}
 * 见{@link org.springframework.boot.autoconfigure.task.TaskSchedulingProperties}和{@link TaskSchedulingAutoConfiguration}
 *
 * <p>使用{@link org.springframework.scheduling.annotation.Async}注解配合{@link TaskScheduler}配置也能实现多线程执行任务
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
