package com.meiyuan.catering.goods.dto.mq;

import lombok.Data;

/**
 * @author wxf
 * @date 2020/4/8 10:28
 * @description 简单描述
 **/
@Data
public class MerchantGoodsUpDownFanoutDTO {
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 1-下架,2-上架
     */
    private Integer goodsStatus;
    /**
     * 商户id
     */
    private Long merchantId;
    public MerchantGoodsUpDownFanoutDTO(){}
    public MerchantGoodsUpDownFanoutDTO(Long goodsId, Integer goodsStatus, Long merchantId) {
        this.goodsId = goodsId;
        this.goodsStatus = goodsStatus;
        this.merchantId = merchantId;
    }
}
