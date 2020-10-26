package com.meiyuan.catering.job.mq.es.goods;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.dto.merchant.EsMerchantSimpleGoods;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.goods.dto.mq.MerchantGoodsUpDownFanoutDTO;
import com.meiyuan.catering.user.dto.cart.ClearCartDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wxf
 * @date 2020/6/9 11:29
 * @description 简单描述
 **/
@Slf4j
@Component
@RabbitListener(queues = GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_ES_UPDATE_QUEUE)
public class MerchantGoodsStatusUpdateHandler {
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    EsMerchantClient esMerchantClient;
    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }
    @RabbitHandler
    public void process(String recived) {
        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
        try {
            MerchantGoodsUpDownFanoutDTO dto = JSON.parseObject(recived, MerchantGoodsUpDownFanoutDTO.class);
            Long merchantId = dto.getMerchantId();
            Long goodsId = dto.getGoodsId();
            Integer goodsStatus = dto.getGoodsStatus();
            Result<EsGoodsDTO> esGoodsDtoResult = esGoodsClient.getByGoodsIdAndMerchantId(goodsId, merchantId);
            if (BaseUtil.judgeResultObject(esGoodsDtoResult)) {
                EsGoodsDTO esGoodsDto = esGoodsDtoResult.getData();
                esGoodsDto.setMerchantGoodsStatus(goodsStatus);
                esGoodsDto.setGoodsStatus(goodsStatus);
                esGoodsClient.saveUpdate(esGoodsDto);
                // 同步商户索引的商品嵌套文档数据
                esMerchantClient.updateMerchantGoodsList(goodsId, goodsStatus, merchantId,null);
            }
            log.info("商户商品上下架同步ES成功");
        } catch (Exception e) {
            log.error("商户商品上下架同步ES消息异常", e);
        }
    }
}
