package com.meiyuan.catering.job.mq.es.goods;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.dto.goods.MerchantGoodsToEsDTO;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author gz
 * @date 2020/7/13 17:46
 * @description 处理平台/商户商品修改消息
 **/
@Slf4j
@Component
@RabbitListener(queues = GoodsMqConstant.MERCHANT_GOODS_CATEGORY_UPDATE_QUEUE)
public class GoodsCategoryUpdateHandler {
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    MarketingGoodsClient marketingGoodsClient;


    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String recived) {
        log.debug("接收到商户修改分类消息:{}", recived);
        try {
            MerchantGoodsToEsDTO dto = JSON.parseObject(recived, MerchantGoodsToEsDTO.class);
            BaseUtil.jsonLog(dto);
            // 更新商品ES数据
            esGoodsClient.goodsCategoryUpdate(dto);

            // 修改分类名称
            marketingGoodsClient.updateCategoryName(dto.getCategoryId(), dto.getCategoryName(), dto.getDefaultCategoryId(), dto.getDefaultCategoryName());
        } catch (Exception e) {
            log.error("处理商户修改分类消息异常:{}",e);
        }
    }
}
