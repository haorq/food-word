package com.meiyuan.catering.marketing.vo.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/09/03 20:09
 * @description 商户APP端营销特价商品详情基本信息VO
 **/

@Data
@ApiModel(value = "商户APP端营销特价商品详情基本信息VO")
public class MerchantSpecialDetailBaseVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "活动ID")
    private Long id;
    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty(value = "活动类型 （5：特价商品  固定为5）")
    private Integer marketingType;
    @ApiModelProperty(value = "活动状态（1：未进行 2：进行中 3：已结束 4：已冻结）")
    private Integer status;
    @ApiModelProperty("活动状态")
    private String statusStr;
    @ApiModelProperty("数据来源：1-平台；2-商家；3-店铺")
    private Integer source;
    @ApiModelProperty("数据来源")
    private String sourceStr;
    @ApiModelProperty(value = "开始日期")
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "活动目的")
    private String activityTarget;
    @ApiModelProperty(value = "拉新目标")
    private Integer userTarget;
    @ApiModelProperty(value = "营业目标")
    private BigDecimal businessTarget;
    @ApiModelProperty(value = "活动描述")
    private String description;
    @ApiModelProperty(value = "定价方式 1-统一折扣 2-折扣 3-固定价")
    private Integer fixType;
    @ApiModelProperty(value = "定价方式")
    private String fixTypeStr;
    @ApiModelProperty(value = "统一折扣(当定价方式为'统一折扣'时有值)")
    private BigDecimal unifySpecialNumber;

}
