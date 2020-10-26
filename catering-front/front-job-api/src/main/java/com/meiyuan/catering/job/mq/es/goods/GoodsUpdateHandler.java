package com.meiyuan.catering.job.mq.es.goods;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.es.MarketingSpecialSku;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.marketing.enums.MarketingGoodsStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author gz
 * @date 2020/7/13 17:46
 * @description 处理平台/商户商品修改消息
 **/
@Slf4j
@Component
@RabbitListener(queues = GoodsMqConstant.PLATFORM_OR_MERCHANT_GOODS_UPDATE_QUEUE)
public class GoodsUpdateHandler {
    @Resource
    EsGoodsClient esGoodsClient;
    @Resource
    MarketingGoodsClient marketingGoodsClient;
    @Resource
    EsMarketingClient esMarketingClient;
    @Resource
    MerchantGoodsClient merchantGoodsClient;
    @Resource
    ShopGoodsClient shopGoodsClient;
    @Resource
    MerchantUtils merchantUtils;
    @Resource
    MarketingSpecialClient specialClient;

    @RabbitHandler
    public void process(byte[] recived) {
        this.process(new String(recived, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String recived) {
        log.debug("接收到平台/商户-商品修改消息:{}", recived);
        try {
            GoodsExtToEsDTO dto = JSON.parseObject(recived, GoodsExtToEsDTO.class);
            // V1.4.0 查询特价商品SKU信息
            Result<Map<Long, Map<String, MarketingSpecialSku>>> shopSpecialSkuMapResult = specialClient.selectGoodsSkuByGoodsId(Long.valueOf(dto.getGoodsId()));
            dto.setShopSpecialSkuMap(shopSpecialSkuMapResult.getData());
            // 更新商品ES数据
            esGoodsClient.goodsExtUpdate(dto);

            // V1.3.0 营销活动商品同步商品上下架状态
            // 商户ID不为空
            String merchantId = dto.getMerchantId();
            // 门店ID
            String shopId = dto.getShopId();
            // 商品ID
            long goodsId = Long.parseLong(dto.getGoodsId());
            // 商户操作了商品上下架
            if(null != dto.getMerchantGoodsStatus() && null != merchantId) {
                long longMerchantId = Long.parseLong(merchantId);
                if(MarketingGoodsStatusEnum.LOWER_SHELF.getStatus().equals(dto.getMerchantGoodsStatus())) {
                    // 商户下架，所有下属门店直接下架
                    lowerShelf(longMerchantId, null, goodsId);
                } else {
                    // 商户上架，判断下属门店的商品是否是上架状态
                    Result<List<CateringShopGoodsSpuEntity>> listResult = shopGoodsClient.selectShopGoodsSpu(longMerchantId, goodsId);
                    List<CateringShopGoodsSpuEntity> list = listResult.getData();
                    list.forEach(item -> {
                        if(GoodsStatusEnum.UPPER_SHELF.getStatus().equals(item.getShopGoodsStatus())) {
                            // 上架，商品为上架状态
                            upperShelf(item.getShopId(), goodsId);
                        }
                    });
                }
            }
            // 门店操作了商品上下架
            if(null != dto.getGoodsStatus() && null != shopId) {
                long longShopId = Long.parseLong(shopId);
                if(MarketingGoodsStatusEnum.LOWER_SHELF.getStatus().equals(dto.getGoodsStatus())) {
                    // 门店下架，直接下架商品
                    lowerShelf(null, longShopId, goodsId);
                } else {
                    // 门店上架，判断上级商户商品是否是上架状态
                    ShopInfoDTO shop = merchantUtils.getShop(longShopId);
                    Long ofMerchantId = shop.getMerchantId();
                    // 商户商品是否是上架状态
                    Result<Boolean> goodsIsUpStateResult = merchantGoodsClient.goodsIsUpState(ofMerchantId, goodsId);
                    if(goodsIsUpStateResult.getData()) {
                        // 是上架状态
                        upperShelf(longShopId, goodsId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("处理平台/商户商品修改消息异常:{}",e);
        }
    }

    private void lowerShelf(Long merchantId, Long shopId, Long goodsId) {
        if(null != merchantId) {
            // 商品已被商户下架，ES同步该商户的活动商品下架状态
            // 同步数据库
            marketingGoodsClient.goodsUpDownSync(merchantId, null, goodsId, MarketingGoodsStatusEnum.LOWER_SHELF.getStatus());
            // 同步ES
            esMarketingClient.goodsUpDownSync(merchantId, null, goodsId, MarketingGoodsStatusEnum.LOWER_SHELF.getStatus());
        }
        if(null != shopId) {
            marketingGoodsClient.goodsUpDownSync(null, shopId, goodsId, MarketingGoodsStatusEnum.LOWER_SHELF.getStatus());
            esMarketingClient.goodsUpDownSync(null, shopId, goodsId, MarketingGoodsStatusEnum.LOWER_SHELF.getStatus());
        }
    }

    private void upperShelf(Long shopId, Long goodsId) {
        marketingGoodsClient.goodsUpDownSync(null, shopId, goodsId, MarketingGoodsStatusEnum.UPPER_SHELF.getStatus());
        esMarketingClient.goodsUpDownSync(null, shopId, goodsId, MarketingGoodsStatusEnum.UPPER_SHELF.getStatus());
    }

}
