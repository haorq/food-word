package com.meiyuan.catering.wx.mq.receive;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.wx.utils.SeckillRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName WxSeckillRestoreStockMsgReceive
 * @Description 秒杀恢复库存
 * @Author gz
 * @Date 2020/4/1 10:15
 * @Version 1.1
 */
@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.MARKETING_SECKILL_STOCK_QUEUE)
public class WxSeckillRestoreStockMsgReceive {
    @Autowired
    private SeckillRedisUtil redisUtil;
    @RabbitHandler
    public void seckillReceive(byte[] recived) {
        this.seckillRestoreStock(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     * 恢复秒杀库存--消息生产者
     * 1、清空购物车，购物车有秒杀商品
     * 2、秒杀订单超时未支付-订单关闭
     * 3、已支付订单--主动取消；
     * @param msg
     */
    @RabbitHandler
    public void seckillRestoreStock(String msg) {
        log.debug("接收到秒杀恢复库存消息:{}",msg);
        try{
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Long seckillGoodsId = jsonObject.getLong("seckillGoodsId");
            Long userId = jsonObject.getLong("userId");
            Integer number = jsonObject.getInteger("number");
            // 标识是否生成订单
            Boolean order = jsonObject.getBoolean("order");
            // 标识是否支付
            Boolean pay = jsonObject.getBoolean("pay");
            if(order==null){
                order = Boolean.FALSE;
            }
            if(pay==null){
                pay = Boolean.FALSE;
            }

            Long seckillEventId = jsonObject.getLong("seckillEventId");
            redisUtil.asynRestoreInvertory(seckillGoodsId,number,userId,order,pay,seckillEventId);
        }catch (Exception e){
            log.error("恢复秒杀库存异常:error={}",e);
        }
    }
}
