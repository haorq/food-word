package com.meiyuan.catering.marketing.dto.seckill;

import lombok.Data;

import java.util.List;

/**
 * @ClassName ActivityGoodsFilterDTO
 * @Description
 * @Author gz
 * @Date 2020/3/24 18:21
 * @Version 1.1
 */
@Data
public class ActivityGoodsFilterDTO {
    /**
     * 活动id
     */
    private Long id;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 商家id
     */
    private Long merchantId;
    /**
     * 商品id集合
     */
    private List<String> goodsIds;
    /**
     * 商品SKU集合
     */
    private List<ActivityGoodsSkuDTO> goodsSkuList;
}
