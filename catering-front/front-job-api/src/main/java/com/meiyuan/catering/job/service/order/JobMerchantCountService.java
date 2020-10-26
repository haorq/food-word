package com.meiyuan.catering.job.service.order;

import com.meiyuan.catering.order.feign.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName JobMerchantCountService
 * @Description
 * @Author xxj
 */
@Slf4j
@Service
public class JobMerchantCountService {
    @Autowired
    private OrderClient orderClient;

    /**
     * 商户月订单数、评分统计定时任务
     */
    public void merchantCount(){
        try{
            orderClient.merchantCount();
        }catch (Exception e){
            log.error("同步缓存的月销量定时任务执行异常：",e);
          }
    }
}
