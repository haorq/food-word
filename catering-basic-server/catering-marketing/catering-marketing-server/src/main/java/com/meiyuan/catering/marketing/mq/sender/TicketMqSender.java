package com.meiyuan.catering.marketing.mq.sender;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
import com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @ClassName TicketSender
 * @Description 优惠券mq消息
 * @Author gz
 * @Date 2020/3/23 15:36
 * @Version 1.1
 */
@Slf4j
@Component
public class TicketMqSender {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送优惠券发放信息
     * @param map 优惠券ids
     * @param targetTime 延迟时限
     */
    public void sendTicketPushMsg(Map<String,Object> map, LocalDateTime targetTime){
        String msg = JSONObject.toJSONString(map);
        try {
            rabbitTemplate.convertAndSend(
                    MarketingMqConstant.MARKETING_DELAYED_EXCHANGE,
                    MarketingMqConstant.MARKETING_TICKET_EXPIRE_KEY,
                    msg,
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, getTtl(targetTime));
                        return message;
                    });
            log.info("成功发送优惠券发放延迟消息，发放时间：{},msg={}",
                    FORMATTER.format(targetTime), msg);
        } catch (Exception e) {
            log.error("发送优惠券发放延迟消息失败:msg={},error={}", msg,e);
        }
    }

    /**
     * 发送平台券
     * @param activityId
     * @param targetTime
     */
    public void sendPlatFormTicketPushMsg(Long activityId,LocalDateTime targetTime){
        String msg = activityId.toString();
        try {
            rabbitTemplate.convertAndSend(
                    MarketingMqConstant.MARKETING_DELAYED_EXCHANGE,
                    MarketingMqConstant.MARKETING_TICKET_PLATFORM_EXPIRE_KEY,
                    msg,
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, getTtl(targetTime));
                        return message;
                    });
            log.info("成功发送优惠券发放延迟消息，发放时间：{},msg={}",
                    FORMATTER.format(targetTime), msg);
        } catch (Exception e) {
            log.error("发送优惠券发放延迟消息失败:msg={},error={}", msg,e);
        }
    }



    /**
     * 获取延迟消息时限
     *
     * @param target 目标时间
     * @return 延迟消息时限
     */
    private Long getTtl(LocalDateTime target) {
        Long ttl = Duration.between(LocalDateTime.now(), target).toMillis();
        if (ttl > RabbitMqConstant.MAX_X_DELAY_TTL) {
            log.info("目标时间[{}]超过延迟最大限制，延迟时间采用最大延迟限制",
                    FORMATTER.format(target));
            ttl = RabbitMqConstant.MAX_X_DELAY_TTL;
        }
        return ttl < 0L ? 0L : ttl;
    }

}
