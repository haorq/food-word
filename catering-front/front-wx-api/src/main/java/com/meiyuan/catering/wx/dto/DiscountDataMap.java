package com.meiyuan.catering.wx.dto;

import com.google.common.collect.Lists;
import com.meiyuan.catering.es.vo.ShopDiscountInfoVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zengzhangni
 * @date 2020/9/3 14:53
 * @since v1.1.0
 */
@Data
public class DiscountDataMap {

    /**
     * 折扣商品
     */
    Map<String, ShopDiscountInfoVo> goodsDiscountMap;
    /**
     * 秒杀商品
     */
    Map<String, ShopDiscountInfoVo> seckillDiscountMap;
    /**
     * 团购商品
     */
    Map<String, ShopDiscountInfoVo> grouponDiscountMap;
    /**
     * 进店领券/店内领券
     */
    Map<String, ShopDiscountInfoVo> ticketGetDiscountMap;
    /**
     * 满减优惠券/(店外发券/平台优惠券)
     */
    Map<String, ShopDiscountInfoVo> ticketSendDiscountMap;
    /**
     * 免配送优惠
     */
    Map<String, ShopDiscountInfoVo> dispatchingDiscountMap;


    public List<ShopDiscountInfoVo> getDiscountList(String shopId) {
        List<ShopDiscountInfoVo> list = Lists.newArrayList();
        if (goodsDiscountMap != null) {
            ShopDiscountInfoVo infoVo = goodsDiscountMap.get(shopId);
            if (infoVo != null) {
                list.add(infoVo);
            }
        }
        if (ticketGetDiscountMap != null) {
            ShopDiscountInfoVo infoVo = ticketGetDiscountMap.get(shopId);
            if (infoVo != null) {
                list.add(infoVo);
            }
        }
        if (seckillDiscountMap != null) {
            ShopDiscountInfoVo infoVo = seckillDiscountMap.get(shopId);
            if (infoVo != null) {
                list.add(infoVo);
            }
        }
        if (grouponDiscountMap != null) {
            ShopDiscountInfoVo infoVo = grouponDiscountMap.get(shopId);
            if (infoVo != null) {
                list.add(infoVo);
            }
        }
        if (ticketSendDiscountMap != null) {
            ShopDiscountInfoVo infoVo = ticketSendDiscountMap.get(shopId);
            if (infoVo != null) {
                list.add(infoVo);
            }
        }
        if (dispatchingDiscountMap != null) {
            ShopDiscountInfoVo infoVo = dispatchingDiscountMap.get(shopId);
            if (infoVo != null) {
                list.add(infoVo);
            }
        }
        return list;
    }


}
