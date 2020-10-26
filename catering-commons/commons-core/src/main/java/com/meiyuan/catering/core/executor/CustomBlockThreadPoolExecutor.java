package com.meiyuan.catering.core.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author GongJunZheng
 * @date 2020/09/11 14:09
 * @description 自定义阻塞提交的ThreadPoolExecutor
 **/

@Slf4j
public class CustomBlockThreadPoolExecutor {

    private ThreadPoolExecutor pool = null;
    /**
     * 核心线程池大小
     */
    private static final  Integer  POOL_SIZE = 2;
    /**
     * 最大线程池大小
     */
    private static final  Integer  MAX_POOL_SIZE = 4;
    /**
     * 线程池中超过corePoolSize数目的空闲线程最大存活时间
     */
    private static final  Long  KEEP_ALIVE_TIME = 30L;
    /**
     * 阻塞队列大小
     */
    private static final  Integer  ARRAY_BLOCKING_QUEUE_SIZE = 30;

    public void init() {
        pool = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(ARRAY_BLOCKING_QUEUE_SIZE), new CustomThreadFactory(), new CustomRejectedExecutionHandler());
    }

    public void destroy() {
        if(null != pool) {
            pool.shutdownNow();
        }
    }

    public ExecutorService getCustomThreadPoolExecutor() {
        return this.pool;
    }


    /**
     * 自定义线程工厂类
     * 生成的线程名词前缀、是否为守护线程以及优先级等
     */
    private static class CustomThreadFactory implements ThreadFactory {

        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName =  CustomBlockThreadPoolExecutor.class.getSimpleName() + count.addAndGet(1);
            t.setName(threadName);
            return t;
        }
    }


    /**
     * 自定义拒绝策略对象
     */
    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //核心改造点,将blockingqueue的offer改成put阻塞提交
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                log.warn("Interrupted!", e);
                Thread.currentThread().interrupt();
            }
        }
    }

}
