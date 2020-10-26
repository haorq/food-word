package com.meiyuan.catering.job.service.merketing;

import com.meiyuan.catering.core.constant.OrderGroupConstant;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.util.SpringContextUtils;
import com.meiyuan.catering.order.dto.OrderOffDTO;
import com.meiyuan.catering.order.enums.OrderOffTypeEnum;
import com.meiyuan.catering.order.enums.OrderOperationEnum;
import com.meiyuan.catering.order.enums.OrderOperationTypeEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.service.CateringOrdersOperationService;
import com.meiyuan.catering.pay.enums.PayEnum;
import com.meiyuan.catering.pay.service.MyPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述:
 *
 * @author zengzhangni
 * @date 2020/9/10 16:56
 * @since v1.4.0
 */
@Slf4j
@Service
public class JobExceptionGrouponService {

    @Autowired
    private OrderClient orderClient;
    @Resource
    private CateringOrdersOperationService ordersOperationService;

    public void disposeExceptionGroupon() {
        try {
            log.info("处理异常团购订单开始");
            exceptionGroupon();
            log.info("处理异常团购订单结束");
        } catch (Exception e) {
            log.error("处理异常团购订单异常", e);
        }
    }


    private void exceptionGroupon() {
        List<Order> list = orderClient.getExGrouponOrder();

        log.error("异常团购订单:{}", list);

        list.forEach(order -> {

            PayEnum payEnum = PayEnum.parse(order.getPayWay());
            MyPayService payService = SpringContextUtils.getBean(payEnum.getImpl(), MyPayService.class);
            payService.cancel(order);

            OrderOffDTO dto = new OrderOffDTO();
            dto.setOrderId(order.getId().toString());
            dto.setOffReason(OrderGroupConstant.GROUP_AUTO_FAIL_MSG);
            dto.setOffUserId(OrderOffTypeEnum.AUTO_OFF.getCode().longValue());
            dto.setOffUserName(OrderOffTypeEnum.AUTO_OFF.getDesc());
            dto.setOffUserPhone("1");
            orderClient.groupFailCancelOrder(dto);
            ordersOperationService.saveOperation(order, "拼团失败,取消订单", OrderOperationEnum.CANCELED, OrderOperationTypeEnum.SYSTEM);
            ordersOperationService.saveOperation(order, "拼团失败,自动退款", OrderOperationEnum.REFUND, OrderOperationTypeEnum.SYSTEM);
        });
    }
}
