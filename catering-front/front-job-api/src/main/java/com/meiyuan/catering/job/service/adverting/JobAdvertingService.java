package com.meiyuan.catering.job.service.adverting;

import com.meiyuan.catering.admin.fegin.AdvertisingClient;
import com.meiyuan.catering.core.enums.base.AdvertisingShowsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/6/1 16:18
 * @since v1.1.0
 */
@Slf4j
@Service
public class JobAdvertingService {

    @Resource
    private AdvertisingClient advertisingClient;


    /**
     * 广告定时上架
     */
    private void upTimedTask() {
        try {
            advertisingClient.updateShow(AdvertisingShowsEnum.SHOW.getFlag(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("广告定时上架任务异常", e);
        }
    }


    /**
     * 广告定时下架
     */
    private void downTimedTask() {
        try {
            advertisingClient.updateShow(AdvertisingShowsEnum.NOT_SHOW.getFlag(), LocalDateTime.now().plusDays(-1));
        } catch (Exception e) {
            log.error("广告定时下架任务异常", e);
        }
    }

    public void downAndUp() {
        this.downTimedTask();
        this.upTimedTask();
        advertisingClient.resetShowAdvertising();
    }
}
