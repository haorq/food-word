package com.meiyuan.catering.merchant.sender;

/**
 * @author lhm
 * @date 2020/4/7 17:03
 **/

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.MerchantMqConstant;
import com.meiyuan.catering.merchant.dto.merchant.MerchantSellTypeMqDTO;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MerchantSendMq {

    @Resource
    AmqpTemplate rabbitTemplate;


    /**
     * 发送修改的消息
     *
     * @param dto
     */
        public void sendMerchantMq(MerchantSellTypeMqDTO dto) {
        log.info("-----------------------------开始 发送商户修改售卖模式信息--------------------------------");
        String sendMsg = JSON.toJSONString(dto);
        log.info("-----------------------------结束 发送商户修改售卖模式信息--------------------------------");
    }

    /**
     * 方法描述 : 商户基本信息修改发送mq
     *          1、商户地址修改
     * @Author: MeiTao
     * @Date: 2020/6/5 0005 10:30
     * @param shopDTO
     * @return: void
     * @Since version-1.0.0
     */
    public void sendMerchantInfoMq(ShopDTO shopDTO){
        log.info("-----------------------------开始 发送商户 : "+ shopDTO.getMerchantId() +" 基本信息修改--------------------------------");
        String sendMsg = JSON.toJSONString(shopDTO);
        rabbitTemplate.convertAndSend(
                MerchantMqConstant.MERCHANT_INFO_FANOUT_EXCHANGE,
                MerchantMqConstant.MERCHANT_INFO_FANOUT_EXCHANGE_ROUTING,
                sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 发送商户 : "+ shopDTO.getMerchantId() +" 基本信息修改--------------------------------");
    }

}
