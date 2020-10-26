package com.meiyuan.catering.goods.dto.merchant;

import com.meiyuan.catering.goods.dto.goods.SimpleGoodsDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wxf
 * @date 2020/4/11 10:43
 * @description 简单描述
 **/
@Data
public class MerchantSearchInfoDTO {
    /**
     * 商家id
     */
    private Long merchantId;
    /**
     * 商家名称
     */
    private String merchantName;
    /**
     * 售卖模式
     */
    private Integer sellType;
    /**
     * 经纬度 ES中 经度 在前 纬度在后
     */
    private String location;
    /**
     * 订单起送金额
     */
    private BigDecimal leastDeliveryPrice;
    /**
     * 配送费
     */
    private BigDecimal deliveryPrice;
    /**
     * 商品集合
     */
    private List<SimpleGoodsDTO> goodsList;
}
