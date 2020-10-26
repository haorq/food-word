package com.meiyuan.catering.marketing.mq.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 团购成功发送消息
 *
 * @author luohuan
 * @date 2020/4/3
 **/
@Slf4j
@Component
public class GroupSuccessMqSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送团购成功消息
     *
     * @param successOrderNumbers 订单编号集合
     */
    public void groupSuccessMsg(List<String> successOrderNumbers) {
        try {
            rabbitTemplate.convertAndSend(
                    MarketingMqConstant.GROUP_SUCCESS_EXCHANGE,
                    MarketingMqConstant.GROUP_SUCCESS_KEY,
                    JSON.toJSONString(successOrderNumbers).getBytes());
            log.info("成功发送团购成功消息");
        } catch (Exception e) {
            log.error("团购成功消息发送失败:successOrderNumbers={},error={}", successOrderNumbers, e);
        }
    }
}
