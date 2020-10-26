package com.meiyuan.catering.marketing.mq.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponUpDownDTO;
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
 * @author luohuan
 * @date 2020/4/2
 * 团购延迟任务
 **/
@Slf4j
@Component
public class MarketingGrouponUpDownMqSender {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendGrouponTimedTaskMsg(Long id, LocalDateTime targetTime, MarketingUpDownStatusEnum statusEnum) {
        MarketingGrouponUpDownDTO dto = new MarketingGrouponUpDownDTO();
        dto.setId(id);
        dto.setEndTime(targetTime);
        dto.setUpDown(statusEnum.getStatus());
        String msg = JSON.toJSONString(dto);
        try {
            rabbitTemplate.convertAndSend(
                    MarketingMqConstant.MARKETING_DELAYED_EXCHANGE,
                    MarketingMqConstant.MARKETING_GROUPON_EXPIRE_KEY,
                    msg.getBytes(),
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, Duration.between(LocalDateTime.now(), targetTime).toMillis());
                        return message;
                    });
            log.info("成功发送团购定时上/下架延迟消息，{}时间：{},msg={}", statusEnum.getDesc(),
                    FORMATTER.format(targetTime), msg);
        } catch (Exception e) {
            log.error("发送团购定时上/下架延迟消息失败:msg={},error={}", msg, e);
        }
    }


}
