package com.meiyuan.catering.job.mq.marketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.feign.MarketingPullNewClient;
import com.meiyuan.catering.marketing.feign.MarketingRecordClient;
import com.meiyuan.catering.order.enums.OrderGoodsTypeEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName WxSeckillOrderPaySuccessMsgReceive
 * @Description 秒杀订单支付成功消息监听
 * @Author gz
 * @Date 2020/4/1 10:46
 * @Version 1.1
 */
@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.MARKETING_SECKILL_ORDER_PAY_SUCCESS_QUEUE)
public class WxSeckillOrderPaySuccessMsgReceive {
    @Autowired
    private MarketingRecordClient recordClient;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private MarketingPullNewClient pullNewClient;

    @RabbitHandler
    public void orderReceive(byte[] recived) {
        this.orderPaySuccessMsg(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     * 秒杀订单支付成功消息监听
     * @param msg
     */
    @RabbitHandler
    public void orderPaySuccessMsg(String msg) {
        log.info("秒杀订单支付成功消息监听:接收到订单支付成功广播消息msg={}",msg);
        try {
            JSONObject object = JSONObject.parseObject(msg);
            Long userId = object.getLong("userId");
            JSONArray list = object.getJSONArray("list");

            if(list != null && !list.isEmpty()){
                list.forEach(e->{
                    JSONObject jsonObject = (JSONObject) e;
                    Integer goodsType = jsonObject.getInteger("goodsType");
                    if(OrderGoodsTypeEnum.SECONDS.getValue().equals(goodsType)){
                        Long goodsId = jsonObject.getLong("goodsId");
                        Integer quantity = jsonObject.getInteger("quantity");
                        recordClient.syncSeckillUserHaveGought(goodsId,userId,quantity,false);
                    }
                });
            }
        } catch (Exception e) {
            log.error("订单支付成功广播消息消费异常:error={}",e);
        }
    }
}
