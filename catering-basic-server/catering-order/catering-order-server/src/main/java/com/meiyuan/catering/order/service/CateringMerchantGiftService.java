package com.meiyuan.catering.order.service;


import com.meiyuan.catering.marketing.vo.pickup.ShopGiftVO;

import java.util.List;

/**
 *  商户赠信息
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringMerchantGiftService{

    /**
     * 功能描述:  通过店铺id查询店铺自提赠品信息
     * @param shopId 店铺id
     * @return: 订单列表信息
     */
    List<ShopGiftVO> listShopGift(Long shopId);


}
