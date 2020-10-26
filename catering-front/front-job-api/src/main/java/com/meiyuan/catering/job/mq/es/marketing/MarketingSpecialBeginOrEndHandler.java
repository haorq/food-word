package com.meiyuan.catering.job.mq.es.marketing;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.job.service.merketing.MqSpecialService;
import com.meiyuan.catering.marketing.dto.special.MarketingSpecialBeginOrEndMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author GongJunZheng
 * @date 2020/09/07 11:09
 * @description 营销特价商品活动延迟开始/结束的消息
 **/

@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.MARKETING_SPECIAL_EXPIRE_QUEUE)
public class MarketingSpecialBeginOrEndHandler {

    @Autowired
    private MqSpecialService mqSpecialService;

    @RabbitHandler
    public void process(byte[] msg) {
        this.process(new String(msg, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String msg) {
        log.info("营销特价商品活动消息：{}", msg);
        try {
            MarketingSpecialBeginOrEndMsgDTO msgDTO = JSONObject.parseObject(msg, MarketingSpecialBeginOrEndMsgDTO.class);
            mqSpecialService.beginOrEnd(msgDTO);
        }catch (Exception e) {
            log.error("修改营销特价商品活动信息失败，异常", e);
        }
    }

}
