package com.meiyuan.catering.job.service.finance;

import com.meiyuan.catering.finance.feign.RechargeActivityClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/4/3 10:52
 **/
@Slf4j
@Service
public class JobRechargeActivityService {
    @Resource
    private RechargeActivityClient activityClient;


    /**
     * 充值活动到期自动取消
     */
    private void autoCancleExpiredTime() {
        try {
            activityClient.autoCancleExpiredTime();
        } catch (Exception e) {
            log.error("充值活动到期自动取消任务异常", e);
        }
    }

    /**
     * 充值活动自动进行
     */
    private void autoRun() {
        try {
            activityClient.autoRun();
        } catch (Exception e) {
            log.error("充值活动自动进行任务异常", e);
        }
    }

    public void autoStartAndPast() {
        autoCancleExpiredTime();
        autoRun();
    }
}
