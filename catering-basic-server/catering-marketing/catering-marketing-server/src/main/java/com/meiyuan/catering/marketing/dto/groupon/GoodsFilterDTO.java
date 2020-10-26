package com.meiyuan.catering.marketing.dto.groupon;

import lombok.Data;

/**
 * @ClassName ActivityGoodsFilterDTO
 * @Description
 * @Author gz
 * @Date 2020/3/24 18:21
 * @Version 1.1
 */
@Data
public class GoodsFilterDTO {
    /**
     * 商家id
     */
    private Long merchantId;
    /**
     * 商品id集合
     */
    private String goodsId;
}
