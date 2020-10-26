package com.meiyuan.catering.merchant.goods.mq.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.dto.goods.MerchantGoodsToEsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName MerchantGoodsSenderMq
 * @Description 商户商品Mq发送者
 * @Author gz
 * @Date 2020/7/13 14:21
 * @Version 1.2.0
 */
@Slf4j
@Component
public class MerchantGoodsSenderMq {
    @Resource
    AmqpTemplate rabbitTemplate;
    /**
     * 方法描述: 同步ES--平台编辑<br>
     *
     * @author: gz
     * @date: 2020/7/13 11:37
     * @param dto
     * @return: {@link }
     * @version 1.2.0
     **/
    public void sendMerchantGoodsUpdateMsg(GoodsExtToEsDTO dto){
        log.info("-----------------------------开始 发送商户商品修改信息--------------------------------");
        String sendMsg = JSON.toJSONString(dto);
        rabbitTemplate.convertAndSend(GoodsMqConstant.GOODS_EXCHANGE, GoodsMqConstant.PLATFORM_OR_MERCHANT_GOODS_UPDATE_QUEUE, sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 发送商户商品修改信息--------------------------------");
    }

    /**
     * 方法描述: 同步ES--商户分类删除/编辑<br>
     *
     * @author: gz
     * @date: 2020/7/13 11:37
     * @param dto
     * @return: {@link }
     * @version 1.2.0
     **/
    public void sendMerchantGoodsCategoryUpdateMsg(MerchantGoodsToEsDTO dto){
        log.info("-----------------------------开始 发送商户商品分类修改信息--------------------------------");
        String sendMsg = JSON.toJSONString(dto);
        rabbitTemplate.convertAndSend(GoodsMqConstant.GOODS_EXCHANGE, GoodsMqConstant.MERCHANT_GOODS_CATEGORY_UPDATE_QUEUE, sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 发送商户商品分类修改信息--------------------------------");
    }
}
