package com.meiyuan.catering.marketing.vo.repertory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/11 14:08
 * @description 查询指定秒杀活动的指定场次的商品销售情况VO
 **/

@Data
@ApiModel(value = "查询指定秒杀活动的指定场次的商品销售情况VO")
public class MarketingRepertoryEventSoldVo {

    @ApiModelProperty(value = "营销商品ID")
    private Long mGoodsId;
    @ApiModelProperty(value = "售出数量")
    private Integer sold;

}
