package com.meiyuan.catering.admin.log.consumer;

import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.admin.entity.CateringLogErrorEntity;
import com.meiyuan.catering.admin.entity.CateringLogLoginEntity;
import com.meiyuan.catering.admin.entity.CateringLogOperationEntity;
import com.meiyuan.catering.admin.log.BaseLog;
import com.meiyuan.catering.admin.log.enums.LogTypeEnum;
import com.meiyuan.catering.admin.service.CateringLogErrorService;
import com.meiyuan.catering.admin.service.CateringLogLoginService;
import com.meiyuan.catering.admin.service.CateringLogOperationService;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 从Redis队列中获取Log，保存到DB
 *
 * @author admin
 */
@Slf4j
@Component
public class LogConsumer implements CommandLineRunner {

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SYS_LOG_KEY, area = JetcacheAreas.ADMIN_AREA))
    private AdvancedCache cache;

    @Autowired
    private CateringLogErrorService sysLogErrorService;
    @Autowired
    private CateringLogLoginService sysLogLoginService;
    @Autowired
    private CateringLogOperationService sysLogOperationService;

    private ScheduledExecutorService scheduledService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("log-consumer-schedule-pool-%d").daemon(true).build());

    @Override
    public void run(String... args) {
        //上次任务结束后，等待2分钟，再执行下次任务
        scheduledService.scheduleWithFixedDelay(() -> {
            try {
                receiveQueue();
            } catch (Exception e) {
                log.error("LogConsumer Error：{}" + e);
            }
        }, 1, 2, TimeUnit.MINUTES);
    }

    private void receiveQueue() {
        String key = "info";
        //每次插入100条
        int count = 100;
        for (int i = 0; i < count; i++) {
            BaseLog logInfo = (BaseLog) cache.rightPop(key);
            if (logInfo == null) {
                return;
            }

            //登录日志
            if (logInfo.getType() == LogTypeEnum.LOGIN.value()) {
                CateringLogLoginEntity entity = ConvertUtils.sourceToTarget(logInfo, CateringLogLoginEntity.class);
                sysLogLoginService.save(entity);
            }

            //操作日志
            if (logInfo.getType() == LogTypeEnum.OPERATION.value()) {
                CateringLogOperationEntity entity = ConvertUtils.sourceToTarget(logInfo, CateringLogOperationEntity.class);
                sysLogOperationService.save(entity);
            }

            //异常日志
            if (logInfo.getType() == LogTypeEnum.ERROR.value()) {
                CateringLogErrorEntity entity = ConvertUtils.sourceToTarget(logInfo, CateringLogErrorEntity.class);
                sysLogErrorService.save(entity);
            }
        }
    }

}
