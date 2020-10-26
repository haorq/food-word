package com.meiyuan.catering.order.feign;

import com.meiyuan.catering.core.dto.base.GrouponMemberQuitDTO;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.yly.YlyUtils;
import com.meiyuan.catering.merchant.dto.shop.config.OrderNoticeInfoDTO;
import com.meiyuan.catering.merchant.dto.shop.config.YlyDeviceInfoVo;
import com.meiyuan.catering.merchant.feign.ShopPrintingConfigClient;
import com.meiyuan.catering.order.dto.OrderPickSmsMqDTO;
import com.meiyuan.catering.order.dto.OrderSecKillMqDTO;
import com.meiyuan.catering.order.dto.query.merchant.OrdersDetailMerchantDTO;
import com.meiyuan.catering.order.enums.DeliveryWayEnum;
import com.meiyuan.catering.order.enums.OrderTypeEnum;
import com.meiyuan.catering.order.mq.sender.*;
import com.meiyuan.catering.order.utils.Prient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @author zengzhangni
 * @date 2020/5/20 9:39
 * @since v1.1.0
 */
@Service
@Slf4j
public class OrderMqSenderClient {
    @Resource
    private GroupOrderCancelMqSender cancelMqSender;
    @Resource
    private OrderPaySuccessMqSender orderPaySuccessMqSender;
    @Resource
    private OrderPickSmsMqSender orderPickSmsMqSender;
    @Resource
    private OrderSecKillMqSender orderSecKillMqSender;
    @Resource
    private OrderTimeOutMqSender orderTimeOutMqSender;
    @Resource
    OrderClient orderClient;
    @Autowired
    ShopPrintingConfigClient shopPrintingConfigClient;
    @Resource
    YlyUtils ylyUtils;

    /**
     * 描述:支付成功通知
     *
     * @param order
     * @param firstOrder
     * @return void
     * @author zengzhangni
     * @date 2020/5/20 11:25
     * @since v1.1.0
     */
    public Result sendPaySuccessMsg(Order order, Boolean firstOrder) {
        orderPaySuccessMqSender.sendPaySuccessMsg(order, firstOrder);
        //非团购订单订单订单结束后打印小票
        if (!Objects.equals(order.getOrderType(), OrderTypeEnum.BULK.getStatus())) {
            Result<OrdersDetailMerchantDTO> result = this.orderClient.orderDetailQueryMerchant(Long.valueOf(order.getId()));
            //订单处理成功、通知app对应登陆设备有新订单产生相关处理
            OrdersDetailMerchantDTO ordersDetail = result.getData();
            if (ObjectUtils.isEmpty(ordersDetail)) {
                log.error("门店订单查询失败，订单号 ： " + order.getId());
            }else {
                //打印小票
                Result<List<YlyDeviceInfoVo>> listResult = shopPrintingConfigClient.ylyDevicePage(order.getStoreId());
                if (BaseUtil.judgeResultList(listResult)){
                    List<YlyDeviceInfoVo> ylyDeviceInfoList = listResult.getData().stream().filter(distinctByKey(YlyDeviceInfoVo::getDeviceNumber)).collect(Collectors.toList());
                    this.printOrderInfo(ylyDeviceInfoList,ordersDetail);
                }
            }
        }
        return Result.succ();
    }

    /**
     * 描述:发送取消团购订单的消息
     *
     * @param quitDTO
     * @return void
     * @author zengzhangni
     * @date 2020/5/20 11:25
     * @since v1.1.0
     */
    public Result sendGroupOrderCancelMsg(GrouponMemberQuitDTO quitDTO) {
        cancelMqSender.sendGroupOrderCancelMsg(quitDTO);
        return Result.succ();
    }

    /**
     * 描述:下单成功距自提1小时短信通知
     *
     * @param smsMqDTO
     * @return void
     * @author zengzhangni
     * @date 2020/5/20 11:25
     * @since v1.1.0
     */
    public Result sendPickSmsPushMsg(OrderPickSmsMqDTO smsMqDTO) {
        orderPickSmsMqSender.sendPickSmsPushMsg(smsMqDTO);
        return Result.succ();
    }

    public Result sendSeckillMsg(List<OrderSecKillMqDTO> secKillMqDTOList) {
        orderSecKillMqSender.sendSeckillMsg(secKillMqDTOList);
        return Result.succ();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    private void printOrderInfo(List<YlyDeviceInfoVo> ylyDeviceInfoList, OrdersDetailMerchantDTO ordersDetail) {
        ylyDeviceInfoList.forEach(ylyDeviceInfoVo -> {
            for (int i=0;i<ylyDeviceInfoVo.getPrintingTimes();i++){
                ylyUtils.printIndex(ylyDeviceInfoVo.getDeviceNumber(),
                        Prient.getTakeOutTemplate(ordersDetail, new StringBuilder()).toString(),
                        String.valueOf(ordersDetail.getOrderId()));
            }
            for (int i=0;i<ylyDeviceInfoVo.getCookTimes();i++){
                ylyUtils.printIndex(ylyDeviceInfoVo.getDeviceNumber(),
                        Prient.getKitchenTemplate(ordersDetail, new StringBuilder()),
                        String.valueOf(ordersDetail.getOrderId()));
            }
        });
    }
}

