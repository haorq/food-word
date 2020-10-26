package com.meiyuan.catering.es.dto.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/8/3 0003 15:31
 * @Description 简单描述 : 店铺活动查询参数dto
 * @Since version-1.3.0
 */
@Data
@ApiModel(value = "店铺活动查询参数dto")
public class EsMarketingShopDTO{
    @ApiModelProperty("购物车选中数量")
    private List<Long> shopIds;

    @ApiModelProperty(value = "活动类型:1-秒杀；2-拼团；3-团购；4-优惠券；")
    private Integer ofType;

    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户",hidden = true)
    private Integer userType;
}
