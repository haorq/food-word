package com.meiyuan.catering.order.mq.sender;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
import com.meiyuan.catering.order.dto.OrderPickSmsMqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 功能描述:  下单成功距自提1小时短信通知
 *
 * @author: zengzhangni
 * @date: 2020/4/7
 */
@Slf4j
@Component
public class OrderPickSmsMqSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 下单成功距自提1小时短信通知
     *
     * @param msg
     */
    public void sendPickSmsPushMsg(OrderPickSmsMqDTO msg) {
        try {
            rabbitTemplate.convertAndSend(
                    OrderMqConstant.ORDER_PICK_SMS_EXCHANGE,
                    OrderMqConstant.ORDER_PICK_SMS_KEY,
                    JSONObject.toJSONString(msg).getBytes(),
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, getTtl(msg.getCreateTime(), msg.getEstimateEndTime()));
                        return message;
                    });
            log.info("成功发送");
        } catch (Exception e) {
            log.error("发送异常:{}", e);
        }
    }


    /**
     * 获取延迟消息时限
     *
     * @param target 目标时间
     * @return 延迟消息时限
     */
    private Long getTtl(LocalDateTime createTime, LocalDateTime target) {
        //距自提1小时短信通知
        target = target.plusHours(-1L);
        Long ttl = Duration.between(createTime, target).toMillis();
        if (ttl > RabbitMqConstant.MAX_X_DELAY_TTL) {
            ttl = RabbitMqConstant.MAX_X_DELAY_TTL;
        }
        return ttl < 0L ? 0L : ttl;
    }

}
