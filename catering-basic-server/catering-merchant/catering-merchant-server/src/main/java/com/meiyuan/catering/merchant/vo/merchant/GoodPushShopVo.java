package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description 商品推送-店铺下拉列表查询结果VO
 * @Date 2020/3/12 0012 10:32
 */
@Data
@ApiModel("商品推送-店铺下拉列表查询结果VO")
public class GoodPushShopVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("店铺编码")
    private String shopCode;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("门店联系人")
    private String primaryPersonName;
    @ApiModelProperty("门店联系人电话")
    private String registerPhone;
    @ApiModelProperty("完整地址")
    private String addressFull;
}
