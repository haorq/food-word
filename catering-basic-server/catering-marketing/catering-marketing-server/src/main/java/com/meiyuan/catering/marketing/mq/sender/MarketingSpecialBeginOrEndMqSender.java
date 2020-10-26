package com.meiyuan.catering.marketing.mq.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialBeginOrEndMsgDTO;
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
 * @author GongJunZheng
 * @date 2020/09/07 11:09
 * @description 营销特价商品活动延迟消息
 **/

@Slf4j
@Component
public class MarketingSpecialBeginOrEndMqSender {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendSpecialTimedTaskMsg(LocalDateTime targetTime, MarketingSpecialBeginOrEndMsgDTO dto) {
        String msg = JSON.toJSONString(dto);
        try {
            rabbitTemplate.convertAndSend(
                    MarketingMqConstant.MARKETING_DELAYED_EXCHANGE,
                    MarketingMqConstant.MARKETING_SPECIAL_EXPIRE_KEY,
                    msg.getBytes(),
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, Duration.between(LocalDateTime.now(), targetTime).toMillis());
                        return message;
                    });
            log.info("成功发送营销特价商品定时开始/结束延迟消息，时间：{},msg={}", FORMATTER.format(targetTime), msg);
        } catch (Exception e) {
            log.error("发送团购定时开始/结束延迟消息失败:msg={},error={}", msg, e);
        }
    }

}
