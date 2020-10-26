package com.meiyuan.catering.admin.log.producer;

import com.alicp.jetcache.anno.CreateCache;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.meiyuan.catering.admin.log.BaseLog;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 日志通过redis队列，异步保存到数据库
 * @author yaozou
 */
@Slf4j
@Component
public class LogProducer {

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SYS_LOG_KEY,area = JetcacheAreas.ADMIN_AREA))
    private AdvancedCache cache;
    
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("log-producer-pool-%d").build();

    ExecutorService pool = new ThreadPoolExecutor(5, 200, 0L,TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 保存Log到Redis消息队列
     */
    public void saveLog(BaseLog logInfo){
        String key = "info";
        if (cache!=null){
            //异步保存到队列
            try {
                pool.execute(() -> cache.leftPush(key, logInfo,-1));
            } catch (Exception e) {
                log.error("LogProducer Error:{}",e);
            }
        }
    }
}
