package com.meiyuan.catering.job.mq.es.goods;

import com.alibaba.fastjson.JSONArray;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsMerchantMenuGoodsDTO;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.job.mq.es.goods.service.GoodsMenuPushService;
import com.meiyuan.catering.marketing.dto.marketing.MarketingPcMenuUpdateSyncDTO;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author wxf
 * @date 2020/3/30 10:29
 * @description 商品/菜单
 **/
@Slf4j
@Component
@RabbitListener(queues = GoodsMqConstant.GOODS_MENU_PUSH_QUEUE_NAME)
public class GoodsMenuPushHandler {

    @Autowired
    MerchantGoodsClient merchantGoodsClient;
    @Autowired
    ShopGoodsClient shopGoodsClient;
    @Autowired
    GoodsMenuPushService goodsMenuPushService;
    @Autowired
    MarketingGoodsClient marketingGoodsClient;
    @Autowired
    EsMarketingClient esMarketingClient;
    @Autowired
    EsGoodsClient esGoodsClient;

    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     * 描述：1.shopIdList
     *
     * @param recived
     * @return {@link }
     * @author lhm
     * @date 2020/7/15
     * @version 1.2.0
     **/
    @RabbitHandler
    public void process(String recived) {
        log.debug("\n-从Mq读出事件实体-\n{}\n", recived);
        try {
            List<EsMerchantMenuGoodsDTO> dtoList = JSONArray.parseArray(recived, EsMerchantMenuGoodsDTO.class);
            if (BaseUtil.judgeList(dtoList)) {
                goodsMenuPushService.pushEs(dtoList);
                // V1.3.0 同步修改营销商品信息
                // 第一步 删除数据库中的营销商品信息
                // 组合商品SKU集合
                Set<String> skuCodeSet = new HashSet<>();
                Set<Long> shopIdSet = new HashSet<>();
                dtoList.forEach(item -> {
                    List<String> skuCodes = item.getSkuCodes();
                    if(BaseUtil.judgeList(skuCodes)) {
                        skuCodeSet.addAll(skuCodes);
                    }
                    shopIdSet.add(item.getShopId());
                });
                shopIdSet.forEach(shopId -> {
                    // 营销秒杀/团购
                    marketingSeckillAndGrouponMenuUpdateSync(shopId, skuCodeSet);
                });
            }
            log.info("推送商品同步ES商品索引成功");
        } catch (Exception e) {
            log.error("推送商品同步ES商品索引异常", e);
        }
    }

    private void marketingSeckillAndGrouponMenuUpdateSync(Long shopId, Set<String> skuCodeSet) {
        Result<MarketingPcMenuUpdateSyncDTO> marketingResult = marketingGoodsClient.pcMenuUpdateSync(shopId, skuCodeSet);
        MarketingPcMenuUpdateSyncDTO marketingSyncDTO = marketingResult.getData();
        List<Long> delMarketingGoodsIdList = marketingSyncDTO.getDelMarketingGoodsIdList();
        List<Long> returnMarketingGoodsIdList = marketingSyncDTO.getReturnMarketingGoodsIdList();
        // 第二步
        // 删除ES中的数据
        if(BaseUtil.judgeList(delMarketingGoodsIdList)) {
            esMarketingClient.pcMenuUpdateSync(delMarketingGoodsIdList, DelEnum.DELETE.getFlag());
        }
        // 还原ES中的数据
        if(BaseUtil.judgeList(returnMarketingGoodsIdList)) {
            esMarketingClient.pcMenuUpdateSync(returnMarketingGoodsIdList, DelEnum.NOT_DELETE.getFlag());
        }
    }

}
