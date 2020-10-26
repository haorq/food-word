package com.meiyuan.catering.merchant.vo.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/3 14:25
 */
@Data
@ApiModel("选择门店分页数据")
public class ShopChoicePageVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺编号")
    private String shopCode;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty("商户名称")
    private String merchantName;

    @ApiModelProperty("店铺联系人")
    private String primaryPersonName;

    @ApiModelProperty("门店联系电话")
    private String shopPhone;

    @ApiModelProperty("店铺状态: 1：启用，2：禁用,3：删除")
    private Integer shopStatus;
}
