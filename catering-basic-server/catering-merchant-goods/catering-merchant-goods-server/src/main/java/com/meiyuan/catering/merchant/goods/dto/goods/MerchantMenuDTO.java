package com.meiyuan.catering.merchant.goods.dto.goods;

import lombok.Data;

import java.util.List;

/**
 * @ClassName MerchantMenuDTO
 * @Description
 * @Author gz
 * @Date 2020/7/24 10:51
 * @Version 1.2.0
 */
@Data
public class MerchantMenuDTO {

    private Long menuId;

    private List<String> skuCodeList;

    private List<Long> shopIdList;

    private List<Long> goodsIdList;
}
