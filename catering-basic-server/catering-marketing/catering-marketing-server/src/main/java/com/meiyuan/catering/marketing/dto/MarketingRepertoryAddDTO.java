package com.meiyuan.catering.marketing.dto;

import lombok.Data;

/**
 * @ClassName MarketingRepertoryAddDTO
 * @Description 库存AddDTO
 * @Author gz
 * @Date 2020/3/18 13:59
 * @Version 1.1
 */
@Data
public class MarketingRepertoryAddDTO {
    /**
     * 营销商品表主键id
     */
    private Long mGoodsId;
    /**
     * 总库存
     */
    private Integer totalInventory;


}
