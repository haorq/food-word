package com.meiyuan.catering.job.service.merketing;

import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author GongJunZheng
 * @date 2020/09/07 17:09
 * @description 营销特价商品活动定时任务服务
 **/

@Slf4j
@Service
public class JobSpecialService {

    @Autowired
    private MarketingSpecialClient specialClient;

    public void beginOrEndTimedTask() {
        try {
            log.info("=====开始执行延迟定时开始/结束营销特价商品活动任务=====");
            specialClient.beginOrEndTimedTask();
            log.info("=====执行延迟定时开始/结束营销特价商品活动任务成功=====");
        }catch (Exception e) {
            log.error("=====执行延迟定时开始/结束营销特价商品活动任务失败=====，异常：{}", e.getMessage());
        }
    }
}
