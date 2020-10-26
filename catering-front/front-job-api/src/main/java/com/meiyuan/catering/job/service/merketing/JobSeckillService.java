package com.meiyuan.catering.job.service.merketing;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.feign.MarketingSeckillClient;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @ClassName JobSeckillService
 * @Description
 * @Author gz
 * @Date 2020/4/1 16:04
 * @Version 1.1
 */
@Slf4j
@Service
public class JobSeckillService {
    @Autowired
    private MarketingSeckillClient seckillClient;

    /**
     * 秒杀定时任务
     */
    public void seckillTimedTask(){
        try{
            seckillClient.seckillTimedTask();
            log.debug("=====秒杀定时任务执行成功=====");
        }catch (Exception e){
            log.error("秒杀定时任务执行异常：error={}",e);
          }
    }

    /**
     * 任务描述：刷新有效秒杀活动的第二天每个场次的商品库存
     * 执行时间：每天晚上00:00:30开始执行定时任务
     */
    public void seckillEventGoodsTask() {
        try {
            log.info("开始执行刷新有效秒杀活动的第二天每个场次的商品库存定时任务，目前时间为：{}", LocalDateTime.now());
            Result<Boolean> taskResult = seckillClient.seckillEventGoodsTask();
            if(taskResult.getData()) {
                log.debug("=====刷新有效秒杀活动的第二天每个场次的商品库存定时任务执行成功=====");
            } else {
                log.error("=====刷新有效秒杀活动的第二天每个场次的商品库存定时任务执行失败=====");
            }
        }catch (Exception e) {
            log.error("刷新有效秒杀活动的第二天每个场次的商品库存定时任务执行异常：{}", e.getMessage());
        }
    }

}
