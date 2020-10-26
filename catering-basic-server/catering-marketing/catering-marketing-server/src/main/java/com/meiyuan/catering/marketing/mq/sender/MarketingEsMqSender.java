package com.meiyuan.catering.marketing.mq.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.marketing.dto.es.MarketingToEsDTO;
import com.meiyuan.catering.marketing.dto.es.MarketingEsStatusUpdateDTO;
import com.meiyuan.catering.marketing.enums.MarketingEsStatusUpdateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName MarketingEsSender
 * @Description
 * @Author gz
 * @Date 2020/3/25 10:38
 * @Version 1.1
 */
@Slf4j
@Component
public class MarketingEsMqSender {
    @Autowired
    AmqpTemplate amqpTemplate;


    /**
     * 新增/编辑 活动(团购/秒杀) 同步数据到es
     * @param list
     */
    public void sendMarketingToEs(List<MarketingToEsDTO> list){
        log.info("==============新增/编辑同步数据到ES==========");
        String sendMsg = JSON.toJSONString(list);
        amqpTemplate.convertAndSend(MarketingMqConstant.MARKETING_EXCHANGE,MarketingMqConstant.MARKETING_ADD_BATCH_QUEUE,sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("==============完成es活动数据同步=============");
    }

    /**
     * 同步es状态
     * @param id 活动id
     * @param statusEnum  状态枚举
     */
    public void sendMarketingStatus(Long id, MarketingEsStatusUpdateEnum statusEnum){
        MarketingEsStatusUpdateDTO dto = MarketingEsStatusUpdateDTO
                .builder()
                .id(id)
                .statusType(statusEnum.getStatus()).build();
        String msg = JSON.toJSONString(dto);
        log.info("==============开始更新Es营销状态:msg={}==========",msg);
        amqpTemplate.convertAndSend(MarketingMqConstant.MARKETING_EXCHANGE,MarketingMqConstant.MARKETING_UPDATE_STATUS_QUEUE,msg.getBytes(StandardCharsets.UTF_8));
        log.info("==============完成es营销状态数据同步=============");
    }

    /**
    * 发送冻结营销特价商品活动消息
    * @param specialId 营销特价商品活动ID
    * @author: GongJunZheng
    * @date: 2020/9/7 18:15
    * @return: void
    * @version V1.4.0
    **/
    public void sendMarketingSpecialStatusUpdate(Long specialId, Integer status) {
        JSONObject json = new JSONObject();
        json.put("specialId", specialId);
        json.put("status", status);
        String msg = json.toJSONString();
        log.info("==============开始发送冻结营销特价商品活动消息：msg={}==========",msg);
        amqpTemplate.convertAndSend(MarketingMqConstant.MARKETING_EXCHANGE,MarketingMqConstant.MARKETING_SPECIAL_STATUS_UPDATE_QUEUE,msg.getBytes(StandardCharsets.UTF_8));
        log.info("==============发送冻结营销特价商品活动消息结束=============");
    }

}
