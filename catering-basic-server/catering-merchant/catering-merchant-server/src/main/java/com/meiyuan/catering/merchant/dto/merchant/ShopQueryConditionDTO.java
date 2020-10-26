package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Description 店铺下拉查询条件
 * @Date  2020/3/27 0027 0:49
 */
@Data
@ApiModel("店铺下拉查询条件")
public class ShopQueryConditionDTO {
    @ApiModelProperty("省编码")
    private String addressProvinceCode;

    @ApiModelProperty("市编码")
    private String addressCityCode;

    @ApiModelProperty("区编码")
    private String addressAreaCode;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("关键字：联系人，联系电话")
    private String keyword;

    @ApiModelProperty(value = "merchantIds",notes = "去除该id对应的店铺")
    private List<Long> merchantIds;

    @ApiModelProperty(value = "shopIds",notes = "去除该id对应的店铺")
    private List<Long> shopIds;

}
