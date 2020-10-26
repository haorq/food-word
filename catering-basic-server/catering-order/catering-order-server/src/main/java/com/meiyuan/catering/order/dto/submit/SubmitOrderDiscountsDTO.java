/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.meiyuan.catering.order.dto.submit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单优惠信息创建Dto
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ApiModel
public class SubmitOrderDiscountsDTO implements Serializable {

    private static final long serialVersionUID = -7229755222149945518L;

    @ApiModelProperty("优惠类型（1：券；2：促销；3：会员卡；4：积分；5：钱包；6：会员卡升级优惠）")
    private Integer discountType;

    @ApiModelProperty("优惠子类型（根据discount_type确定，【discount_type=1，11：优惠券；12：折扣券】；【discount_type=2，21：商品自身多件促销】；【discount_type=3，31：会员商品折扣；32：会员服务折扣；33：会员储值卡；34：会员提货券；35：会员服务券】；【discount_type=4，41：积分抵扣】；【discount_type=5，51：钱包抵扣】；【discount_type=6，61：会员卡分享升级优惠；62：会员卡购买升级优惠】）")
    private Integer discountChildType;

    @ApiModelProperty("优惠源ID（根据类型不同区分）")
    private String discountId;

    @ApiModelProperty(value = "优惠源编号", hidden = true)
    private String discountNumber;

    @ApiModelProperty("优惠源详细ID（当前为会员卡下指定权益ID）")
    private String discountDetailId;

    @ApiModelProperty(value = "优惠源名称", hidden = true)
    private String discountName;

    @ApiModelProperty(value = "优惠折扣比例（折扣详细数值，如85折为0.85）", hidden = true)
    private BigDecimal discountRate;

    @ApiModelProperty(value = "优惠使用积分", hidden = true)
    private Integer discountScore;

    @ApiModelProperty(value = "优惠金额", hidden = true)
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "优惠是否参与计算", hidden = true)
    private Boolean calculated;

}
