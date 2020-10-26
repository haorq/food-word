package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.order.JobOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yaoozu
 * @description 订单定时处理
 * @date 2020/4/1310:46
 * @since v1.0.0
 */
@RestController
@RequestMapping(value = "order")
@Slf4j
public class JobOrderController {
    @Autowired
    private JobOrderService orderService;

    /**
     * @description 关闭未取餐的订单
     * <ul>
     *     <li>自动关闭时间是预计取餐第二天的0点</li>
     * </ul>
     * @author yaozou
     * @date 2020/4/13 11:17
     * @param
     * @since v1.0.0
     * @return
     */
    @ApiOperation("关闭未取餐的订单")
    @GetMapping("/closeWaitingTokeOrder")
    public void closeWaitingTokeOrder(){
        orderService.closeWaitingTokeOrder();
    }
}
