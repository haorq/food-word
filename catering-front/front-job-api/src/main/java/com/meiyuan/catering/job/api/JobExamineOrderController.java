package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.JobExamineOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zengzhangni
 * @date 2020/4/7 14:51
 **/
@RestController
@RequestMapping(value = "examine/order")
@Slf4j
public class JobExamineOrderController {

    @Autowired
    private JobExamineOrderService examineOrderService;

    @ApiOperation("检查待支付订单是否微信支付")
    @GetMapping("/examineOrder")
    public void examineOrder() {
        log.debug("检查待支付订单是否微信支付 开始");
        examineOrderService.examineOrder();
        log.debug("检查待支付订单是否微信支付 结束");
    }
}
