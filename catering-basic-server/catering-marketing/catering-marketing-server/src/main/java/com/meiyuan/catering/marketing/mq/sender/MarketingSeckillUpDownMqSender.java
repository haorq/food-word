package com.meiyuan.catering.marketing.mq.sender;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName MarketingSeckillUpDownMqSender
 * @Description 秒杀延迟任务
 * @Author gz
 * @Date 2020/3/25 13:17
 * @Version 1.1
 */
@Slf4j
@Component
public class MarketingSeckillUpDownMqSender {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendSeckillTimedTaskMsg(Long id, LocalDateTime targetTime, MarketingUpDownStatusEnum statusEnum){
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("upDownStatus",statusEnum.getStatus());
        String msg = json.toJSONString();
        try {
            rabbitTemplate.convertAndSend(
                    MarketingMqConstant.MARKETING_DELAYED_EXCHANGE,
                    MarketingMqConstant.MARKETING_SECKILL_EXPIRE_KEY,
                    msg,
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, Duration.between(LocalDateTime.now(), targetTime).toMillis());
                        return message;
                    });
            log.info("成功发送秒杀定时上/下架延迟消息，{}时间：{},msg={}",statusEnum.getDesc(),
                    FORMATTER.format(targetTime), msg);
        } catch (Exception e) {
            log.error("发送秒杀定时上/下架延迟消息失败:msg={},error={}", msg,e);
        }
    }


}
