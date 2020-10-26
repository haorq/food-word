package com.meiyuan.catering.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author admin
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    /** 日志*/
    private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    @Bean
    public Executor asyncServiceExecutor(){

        logger.info("start asyncServiceExecutor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数 cpu + 1(空闲)
        executor.setCorePoolSize(5);
        // 最大线程数
        executor.setMaxPoolSize(5);
        // 队列等待数
        executor.setQueueCapacity(9999);
        // 线程名称前缀
        executor.setThreadNamePrefix("asyncService-");
        // 拒绝策略: 由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
