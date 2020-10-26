package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.finance.JobChargeOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhm
 * @date 2020/4/7 14:51
 **/
@RestController
@RequestMapping(value = "charge/order")
@Slf4j
public class JobChargeOrderController {

    @Autowired
    private JobChargeOrderService chargeOrderService;

    @ApiOperation("充值订单过期自动取消")
    @GetMapping("/autoOrderCancle")
    public void autoOrderCancle() {
        log.debug("充值订单过期自动取消 开始");
        chargeOrderService.autoOrderCancle();
        log.debug("充值订单过期自动取消 结束");
    }
}
