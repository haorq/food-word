package com.meiyuan.catering.marketing.mq.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.marketing.dto.groupon.GrouponFailMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 功能描述:  团购失败消息发送（接收方进行退款等操作）
 *
 * @author: luohuan
 * @date: 2020-04-02
 */
@Slf4j
@Component
public class GroupFailMqSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送团购失败订单退款消息
     *
     * @param dto 团购失败信息
     */
    public void groupFailMsg(GrouponFailMsgDTO dto) {
        try {
            rabbitTemplate.convertAndSend(
                    MarketingMqConstant.GROUP_FAIL_EXCHANGE,
                    MarketingMqConstant.GROUP_FAIL_KEY,
                    JSON.toJSONString(dto).getBytes());
            log.info("成功发送团购失败消息");
        } catch (Exception e) {
            log.error("团购失败消息发送失败:failOrderNumbers={},error={}", dto.toString(), e.getMessage());
        }
    }

}
