package com.meiyuan.catering.merchant.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhm
 * @date 2020/3/16 15:10
 **/
@ApiModel("标签详情vo")
@Data
public class ShopTagDetailVo {

    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("店铺标签")
    private String tagName;
    @ApiModelProperty("商家联系人")
    private String primaryPersonName;
    @ApiModelProperty("商家电话/门店电话")
    private String merchantPhone;
    @ApiModelProperty("1-营业 2-打样")
    private Integer businessStatus;
    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;

}
