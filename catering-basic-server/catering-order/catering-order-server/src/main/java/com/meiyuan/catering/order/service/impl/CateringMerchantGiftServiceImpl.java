package com.meiyuan.catering.order.service.impl;


import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dto.gift.GiftDTO;
import com.meiyuan.catering.goods.feign.GoodsGiftClient;
import com.meiyuan.catering.marketing.feign.MarketingPickupPointClient;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftVO;
import com.meiyuan.catering.merchant.dto.pickup.SelfMentionGiftDTO;
import com.meiyuan.catering.merchant.feign.SelfMentionGiftServiceClient;
import com.meiyuan.catering.order.service.CateringMerchantGiftService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  商户赠信息
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
@Service
public class CateringMerchantGiftServiceImpl implements CateringMerchantGiftService {
    @Autowired
    private MarketingPickupPointClient marketingPickupPointClient;

    @Autowired
    private GoodsGiftClient goodsGiftClient;
    @Autowired
    private SelfMentionGiftServiceClient selfMentionGiftServiceClient;

    /**
     * 功能描述:  通过店铺id查询店铺自提赠品信息
     * @param shopId 店铺id
     * @return: 订单列表信息
     */
    @Override
    public List<ShopGiftVO> listShopGift(Long shopId){
        //查询未删除的赠品id
        List<GiftDTO> goodsGiftEntities = goodsGiftClient.listGiftGood(null).getData();

        if (!BaseUtil.judgeList(goodsGiftEntities)){
            return null;
        }

        List<Long> giftIds = goodsGiftEntities.stream().map(GiftDTO::getId).collect(Collectors.toList());

        //查询当前商家自提赠品信息
        List<SelfMentionGiftDTO> mentionGiftEntities = selfMentionGiftServiceClient.listShopGift(shopId,giftIds).getData();

        if(!BaseUtil.judgeList(mentionGiftEntities)){
            return null;
        }

        //获取赠品活动相关信息
        List<Long> pickupIds = mentionGiftEntities.stream().map(SelfMentionGiftDTO::getPickupId).collect(Collectors.toList());
        List<ShopGiftVO> shopGiftVos = marketingPickupPointClient.listShopGift(pickupIds, shopId).getData();

        Map<Long, GiftDTO> giftMap = goodsGiftEntities.stream().collect(Collectors.toMap(GiftDTO::getId, shopGift -> shopGift,(k1, k2)->k2));

        Map<Long, SelfMentionGiftDTO> mentionGiftEntityMap = mentionGiftEntities.stream().collect(Collectors.toMap(SelfMentionGiftDTO::getPickupId, shopGift -> shopGift,(k1,k2)->k2));

        //设置赠品基本信息
        shopGiftVos.forEach(shopGiftVO -> {
            SelfMentionGiftDTO selfMentionGiftEntity = mentionGiftEntityMap.get(shopGiftVO.getPickupId());
            //设置赠品赠送数量
            shopGiftVO.setNumber(selfMentionGiftEntity.getNumber());
            shopGiftVO.setGiftId(selfMentionGiftEntity.getGiftId());
            GiftDTO giftEntity = giftMap.get(shopGiftVO.getGiftId());
            Long giftStock = shopGiftVO.getGiftStock();
            BeanUtils.copyProperties(giftEntity,shopGiftVO);
            shopGiftVO.setGiftStock(giftStock);
        });
        return shopGiftVos;
    }


}
