package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 商户店铺列表查询VO
 * @Date  2020/3/12 0012 10:32
 */
@Data
@ApiModel("商户店铺列表查询VO")
public class CateringMerchantPageVo {
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("店铺id")
    private Long shopId;
    @ApiModelProperty("店铺编号")
    private String shopCode;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("店铺联系人")
    private String primaryPersonName;
    @ApiModelProperty("注册电话/商家联系人电话")
    private String registerPhone;
    @ApiModelProperty("完整地址")
    private String addressFull;
    @ApiModelProperty("店铺类型：1-店铺 2-店铺兼自提点 3-自提点")
    private Integer shopType;
    @ApiModelProperty("店铺状态:1：启用，2：禁用")
    private Integer shopStatus;
    @ApiModelProperty("门店身份/类型：1--商家，2--自提点，3--商家兼自提点[1.1.0]")
    private Integer type;


    @ApiModelProperty("门店来源：1- 商户申请 2-平台添加")
    private Integer shopSource;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间/删除时间")
    private LocalDateTime updateTime;

}
