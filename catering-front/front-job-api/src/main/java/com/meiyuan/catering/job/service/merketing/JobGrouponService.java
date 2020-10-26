package com.meiyuan.catering.job.service.merketing;

import com.meiyuan.catering.marketing.feign.MarketingGrouponClient;
import com.meiyuan.catering.marketing.service.CateringMarketingGrouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luohuan
 * @date 2020/4/2
 **/
@Slf4j
@Service
public class JobGrouponService {
    @Autowired
    private MarketingGrouponClient grouponClient;

    /**
     * 团购定时下架任务
     */
    public void grouponDownTimedTask() {
        try {
            log.info("团购定时结束任务开始执行");
            grouponClient.grouponDownTimedTask();
            log.info("团购定时结束任务开始执行");
        } catch (Exception e) {
            log.error("团购定时结束任务异常", e);
        }
    }
}
