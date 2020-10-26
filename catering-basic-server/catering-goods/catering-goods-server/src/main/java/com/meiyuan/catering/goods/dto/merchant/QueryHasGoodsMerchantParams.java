package com.meiyuan.catering.goods.dto.merchant;

import lombok.Builder;
import lombok.Data;

/**
 * @author yaoozu
 * @description 查询有商品的商户查询条件
 * @date 2020/4/1618:20
 * @since v1.0.0
 */
@Data
@Builder
public class QueryHasGoodsMerchantParams {
    private Integer dataBindType;
    private Long    merchantId;
}
