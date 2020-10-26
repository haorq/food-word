package com.meiyuan.catering.wx.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author admin
 */
@Configuration
public class ExecutorConfig {
    /** 日志*/
    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

//    @Bean("indexExecutor")
//    public ThreadPoolTaskExecutor indexExecutor(){
//
//        logger.info("start indexExecutor");
//
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        // 核心线程数 cpu + 1(空闲)
//        executor.setCorePoolSize(4);
//        // 最大线程数
//        executor.setMaxPoolSize(8);
//        // 队列等待数
//        executor.setQueueCapacity(999);
//        // 线程名称前缀
//        executor.setThreadNamePrefix("index-");
//        // 拒绝策略: 由调用线程（提交任务的线程）处理该任务
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        // 初始化
//        executor.initialize();
//        return executor;
//    }

    @Bean("cartExecutor")
    public ThreadPoolTaskExecutor cartExecutor(){

        logger.info("start cartExecutor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数 cpu + 1(空闲)
        executor.setCorePoolSize(2);
        // 最大线程数
        executor.setMaxPoolSize(4);
        // 队列等待数
        executor.setQueueCapacity(999);
        // 线程名称前缀
        executor.setThreadNamePrefix("cart-");
        // 拒绝策略: 由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    @Bean("shareBillExecutor")
    public ThreadPoolTaskExecutor shareBillExecutor(){

        logger.info("start shareBillExecutor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数 cpu + 1(空闲)
        executor.setCorePoolSize(2);
        // 最大线程数
        executor.setMaxPoolSize(4);
        // 队列等待数
        executor.setQueueCapacity(999);
        // 线程名称前缀
        executor.setThreadNamePrefix("shareBill-");
        // 拒绝策略: 由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
