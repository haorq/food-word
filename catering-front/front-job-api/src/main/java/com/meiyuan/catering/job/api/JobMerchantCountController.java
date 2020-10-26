package com.meiyuan.catering.job.api;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.job.service.merketing.JobSeckillService;
import com.meiyuan.catering.job.service.order.JobMerchantCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName JobMerchantCountController
 * @Description
 * @Author xxj
 */
@RestController
@RequestMapping(value = "merchant/order")
@Slf4j
public class JobMerchantCountController {
    @Autowired
    private JobMerchantCountService jobMerchantCountService;

    /**
     * 每小时统计商户月订单数、评分
     * @return
     */
    @GetMapping(value = "count")
    public Result merchantCount(){
        log.debug("统计店铺月订单数和评分任务执行......");
        jobMerchantCountService.merchantCount();
        return Result.succ();
    }
}
