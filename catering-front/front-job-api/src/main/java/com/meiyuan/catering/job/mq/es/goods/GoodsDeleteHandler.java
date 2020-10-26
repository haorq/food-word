package com.meiyuan.catering.job.mq.es.goods;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.enums.base.OperateTypeEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.goods.dto.es.GoodsEsDeteleDTO;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author lhm
 * @date 2020/6/2 11:18
 * @description 处理商品删除--平台取消授权
 **/
@Slf4j
@Component
@RabbitListener(queues = GoodsMqConstant.GOODS_DELETE_FANOUT_QUEUE)
public class GoodsDeleteHandler {
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    MarketingGoodsClient marketingGoodsClient;
    @Resource
    EsMarketingClient esMarketingClient;
    @Autowired
    MarketingSpecialClient marketingSpecialClient;


    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String recived) {
        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
        try {
            GoodsEsDeteleDTO dto = JSON.parseObject(recived, GoodsEsDeteleDTO.class);
            esGoodsClient.cancelAuthGoods(dto.getGoodsId(), dto.getMerchantId());

            log.info("处理商品删除/取消授权成功");
            log.info("=====V1.3.0商品删除或者取消授权，同步营销活动的商品信息开始=====");
            if(OperateTypeEnum.GOODS_AUTH_CANCEL.getStatus().equals(dto.getOperateType())) {
                log.info("==========商品取消授权==========");
                esMarketingClient.goodsCancelSync(dto.getMerchantId(), dto.getGoodsId());
            }
            if(OperateTypeEnum.GOODS_DEL.getStatus().equals(dto.getOperateType())) {
                log.info("==========商品删除==========");
                // V1.3.0 设置数据库中营销商品为删除状态
                marketingGoodsClient.goodsDelSync(dto.getMerchantId(), dto.getGoodsId());
                // V1.3.0 删除ES中的营销商品
                esMarketingClient.goodsDelSync(dto.getMerchantId(), dto.getGoodsId());
                // V1.4.0 删除营销特价商品信息
                marketingSpecialClient.goodsDelSync(dto.getMerchantId(), dto.getGoodsId());
            }
            log.info("=====V1.3.0商品删除或者取消授权，同步营销活动的商品信息结束=====");
        } catch (Exception e) {
            log.error("处理商品删除/取消授权异常", e);
        }
    }
}
