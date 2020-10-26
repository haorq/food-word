package com.meiyuan.catering.wx.mq.receive;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.UserMqConstant;
import com.meiyuan.catering.user.dto.user.UserIdDTO;
import com.meiyuan.catering.wx.service.marketing.WxMarketingRecommendPrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author luohuan
 * @date 2020/3/30
 * 新用户注册成功消息消费
 **/
@Slf4j
@Component
public class WxUserRegisterSuccessMsgReceive {

    @Autowired
    private WxMarketingRecommendPrizeService recommendPrizeService;

    /**
     * 新用户注册成功，处理推荐有奖
     *
     * @param receive
     */
    @RabbitListener(queues = UserMqConstant.USER_ADD__QUEUE)
    @RabbitHandler
    public void handleRecommendPrize(byte[] receive) {
        String msg = new String(receive, StandardCharsets.UTF_8);
        try {
            log.info("接受到新用户注册成功的消息，处理推荐有奖活动。msg={}", msg);
            UserIdDTO userIdDTO = JSON.parseObject(msg, UserIdDTO.class);
            recommendPrizeService.handleNewUserRegister(userIdDTO);
            log.info("新用户注册成功,推荐有奖活动处理完毕。");
        } catch (Exception e) {
            log.error("新用户注册成功，处理推荐有奖活动发生异常", e);
        }
    }
}
