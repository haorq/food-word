package com.meiyuan.catering.order.mq.sender;

import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
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
 * 功能描述:  订单相关mq
 * @author: XiJie Xie
 * @date: 2020/3/24 16:42
 */
@Slf4j
@Component
public class OrderTimeOutMqSender {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送订单超时未支付取消订单消息
     * @param msg 订单id
     * @param targetTime 延迟时限
     */
    public void sendTicketPushMsg(String msg,LocalDateTime targetTime){
        try {
            rabbitTemplate.convertAndSend(
                    OrderMqConstant.ORDER_DELAYED_EXCHANGE,
                    OrderMqConstant.ORDER_TIME_OUT_EXPIRE_KEY,
                    msg,
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, getTtl(targetTime));
                        return message;
                    });
            log.info("成功发送【订单[超时未支付]取消订单】延迟消息，发放时间：{},msg={}",
                    FORMATTER.format(targetTime), msg);
        } catch (Exception e) {
            log.error("发送【订单[超时未支付]取消订单】延迟消息失败:msg={},error={}", msg,e);
        }
    }

    /**
     * 发送订单完成超时不能申请售后
     * @param msg 订单id
     * @param targetTime 延迟时限
     */
    public void sendOrderAfterSalesPushMsg(String msg,LocalDateTime targetTime){
        try {
            rabbitTemplate.convertAndSend(
                    OrderMqConstant.ORDER_DELAYED_EXCHANGE,
                    OrderMqConstant.ORDER_AFTER_SALES_EXPIRE_KEY,
                    msg,
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, getTtl(targetTime));
                        return message;
                    });
            log.info("成功发送【订单[完成超时]不能申请售后】延迟消息，发放时间：{},msg={}",
                    FORMATTER.format(targetTime), msg);
        } catch (Exception e) {
            log.error("发送【订单[完成超时]不能申请售后】延迟消息失败:msg={},error={}", msg,e);
        }
    }

    /**
     * 发送订单完成超时自动五星好评
     * @param msg 订单id
     * @param targetTime 延迟时限
     */
    public void sendOrderAppraisePushMsg(String msg,LocalDateTime targetTime){
        try {
            rabbitTemplate.convertAndSend(
                    OrderMqConstant.ORDER_DELAYED_EXCHANGE,
                    OrderMqConstant.ORDER_APPRAISE_EXPIRE_KEY,
                    msg,
                    message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
                        messageProperties.setHeader(RabbitMqConstant.MessageHeaders.X_DELAY, getTtl(targetTime));
                        return message;
                    });
            log.info("成功发送【订单[完成超时]自动五星好评】延迟消息，发放时间：{},msg={}",
                    FORMATTER.format(targetTime), msg);
        } catch (Exception e) {
            log.error("发送【订单[完成超时]自动五星好评】延迟消息失败:msg={},error={}", msg,e);
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
            log.info("目标时间[{}]超过延迟最大限制，延迟时间采用最大延迟限制，实际业务由队列监听器进行重发处理",
                    FORMATTER.format(target));
            ttl = RabbitMqConstant.MAX_X_DELAY_TTL;
        }
        return ttl < 0L ? 0L : ttl;
    }

}
