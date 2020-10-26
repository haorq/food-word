package com.meiyuan.catering.order.mq.sender;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.dto.base.PayActivityDTO;
import com.meiyuan.catering.core.dto.base.PayGoodsDTO;
import com.meiyuan.catering.core.dto.base.PaySuccessNotifyDTO;
import com.meiyuan.catering.core.dto.order.goods.OrderGoods;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.util.yly.PrintUtils;
import com.meiyuan.catering.core.util.yly.YlyUtils;
import com.meiyuan.catering.merchant.dto.shop.config.OrderNoticeInfoDTO;
import com.meiyuan.catering.merchant.feign.ShopPrintingConfigClient;
import com.meiyuan.catering.order.entity.CateringOrdersActivityEntity;
import com.meiyuan.catering.order.enums.ActivityTypeEnum;
import com.meiyuan.catering.order.enums.DeliveryWayEnum;
import com.meiyuan.catering.order.enums.OrderTypeEnum;
import com.meiyuan.catering.order.service.CateringOrdersActivityService;
import com.meiyuan.catering.order.service.CateringOrdersGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 功能描述:  订单支付成功通知信息
 *
 * @author:
 * @date:
 */
@Slf4j
@Component
public class OrderPaySuccessMqSender {


    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private CateringOrdersActivityService ordersActivityService;
    @Autowired
    private CateringOrdersGoodsService goodsService;
    @Autowired
    private ShopPrintingConfigClient shopPrintingConfigClient;

    @Async
    public void sendPaySuccessMsg(Order order, Boolean firstOrder) {

        Long orderId = order.getId();
        PaySuccessNotifyDTO notifyDTO = new PaySuccessNotifyDTO();
        notifyDTO.setOrderId(orderId);
        notifyDTO.setUserId(order.getMemberId());
        notifyDTO.setOrderType(order.getOrderType());
        notifyDTO.setOrderNumber(order.getOrderNumber());
        notifyDTO.setUserName(order.getMemberName());
        notifyDTO.setPayTime(order.getPaidTime());
        notifyDTO.setDeliveryWay(order.getDeliveryWay());
        notifyDTO.setMerchantId(order.getMerchantId());
        // V1.3.0 增加门店ID
        notifyDTO.setShopId(order.getStoreId());
        notifyDTO.setFirstOrder(firstOrder);
        List<CateringOrdersActivityEntity> activitys = ordersActivityService.getByOrderId(orderId);

        List<OrderGoods> list = goodsService.getByOrderId(orderId);
        List<PayGoodsDTO> dtoList = list.stream().filter(goods -> !goods.getGifts()).map(goods -> {
            PayGoodsDTO goodsDTO = new PayGoodsDTO();
            goodsDTO.setGoodsId(goods.getGoodsId());
            goodsDTO.setGoodsType(goods.getGoodsType());
            goodsDTO.setQuantity(goods.getQuantity());
            goodsDTO.setMGoodsId(goods.getGoodsId());
            goodsDTO.setGoodsSkuCode(goods.getGoodsSkuCode());
            return goodsDTO;
        }).collect(Collectors.toList());
        notifyDTO.setList(dtoList);

        List<PayActivityDTO> activityDtoS = activitys.stream().map(activity -> {
            PayActivityDTO dto = new PayActivityDTO();
            dto.setOfId(activity.getActivityId());
            dto.setOfType(getOfType(activity.getActivityType()));
            return dto;
        }).collect(Collectors.toList());
        notifyDTO.setActivityList(activityDtoS);

        log.debug("\n支付成功通知信息:{}", JSONObject.toJSONString(notifyDTO));
        sendPaySuccessMsg(notifyDTO);
        if (!Objects.equals(order.getOrderType(), OrderTypeEnum.BULK.getStatus())) {
            //订单处理成功、通知app对应登陆设备有新订单产生相关处理
            OrderNoticeInfoDTO orderNoticeDto = new OrderNoticeInfoDTO();
            orderNoticeDto.setOrderId(orderId);
            orderNoticeDto.setShopId(order.getStoreId());
            //@TODO 订单归属点给出语音提示【当前自提点不需要给出语音提示】
            if (Objects.equals(order.getDeliveryWay(), DeliveryWayEnum.invite.getCode())) {
                orderNoticeDto.setPickupId(null);
            }
            shopPrintingConfigClient.saveShopOrderNotice(orderNoticeDto);
        }
    }

    /**
     * 发送订单支付成功订单消息
     *
     * @param msg 订单id
     */
    public void sendPaySuccessMsg(PaySuccessNotifyDTO msg) {
        try {
            rabbitTemplate.convertAndSend(
                    OrderMqConstant.ORDER_PAY_EXCHANGE,
                    OrderMqConstant.ORDER_PAY_KEY,
                    JSONObject.toJSONString(msg).getBytes());
            log.info("成功发送");
        } catch (Exception e) {
            log.error("发送消息失败:msg={},error={}", msg, e);
        }
    }

    /**
     * activityType转OfType
     *
     * @param activityType
     * @return
     */
    private Integer getOfType(Integer activityType) {
        ActivityTypeEnum activityTypeEnum = ActivityTypeEnum.parse(activityType);
        Integer ofType;
        switch (activityTypeEnum) {
            case SECKILL:
                ofType = 1;
                break;
            case GROUPON:
                ofType = 3;
                break;
            default:
                ofType = activityType;
                break;
        }
        return ofType;
    }


}
