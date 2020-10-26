package com.meiyuan.catering.marketing.vo.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName PlatFormTicketOrderInfoVO
 * @Description
 * @Author gz
 * @Date 2020/8/10 15:08
 * @Version 1.3.0
 */
@Data
public class PlatFormTicketOrderInfoVO extends PlatFormTicketBaseInfoVO {

    @ApiModelProperty(value = "使用量")
    private Integer useNum;
    @ApiModelProperty(value = "关联订单")
    private Integer orderNum;
    @ApiModelProperty(value = "关联营业额")
    private BigDecimal turnover;
    @ApiModelProperty(value = "拉新人数")
    private Integer newMemberNum;


    public BigDecimal getActivityCost(){
        return super.amount.multiply(new BigDecimal(orderNum));
    }
}
