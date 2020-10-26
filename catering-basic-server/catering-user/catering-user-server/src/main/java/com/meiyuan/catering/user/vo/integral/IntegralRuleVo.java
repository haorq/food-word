package com.meiyuan.catering.user.vo.integral;

import com.meiyuan.catering.marketing.vo.ticket.MarketingPlatFormActivitySelectListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengzhangni
 * @since 2020-03-18
 */
@Data
@ApiModel("微信积分信息")
public class IntegralRuleVo implements Serializable {

    @ApiModelProperty("最高获取积分")
    private Integer highIntegral;
    @ApiModelProperty("最高获的优惠券")
    private BigDecimal ticketAmount;
    @ApiModelProperty("订单完成多少天自动好评")
    private Integer autoDays;
    @ApiModelProperty("积分规则")
    List<IntegralRuleListVo> appraiseRuleList;
    @ApiModelProperty("优惠券信息集合")
    private List<MarketingPlatFormActivitySelectListVO> ticketList;

}
