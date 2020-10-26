package com.meiyuan.catering.marketing.vo.marketing;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author fql
 */
@Setter
@Getter
public class MarketingSubsidyVo {

    @ApiModelProperty(value = "订单主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "使用渠道")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long useChannels;

    @ApiModelProperty(value = "优惠卷面额")
    private BigDecimal amount;

    @ApiModelProperty(value = "平台成本合计（元）")
    private BigDecimal manageAmount;

    @ApiModelProperty(value = "商家成本合计（元）")
    private BigDecimal merchantCost;

    @ApiModelProperty(value = "是否限制具体的商品使用：1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer goodsLimit;

    @ApiModelProperty(value = "发券方：1-行膳平台；2-地推员专属；3-品牌")
    private Integer sendTicketParty;

    @ApiModelProperty(value = "对象类型 1:用户 2 品牌")
    private Integer targetType;

    @ApiModelProperty(value = "对象类型 1:用户 2 品牌")
    private BigDecimal bearDuty;

    @ApiModelProperty(value = "活动id")
    private Long ticketActivtiyId;
}
