package com.meiyuan.catering.marketing.vo.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName TicketActivityOrderVO
 * @Description
 * @Author gz
 * @Date 2020/8/5 15:43
 * @Version 1.3.0
 */
@Data
public class TicketActivityOrderVO {
    @ApiModelProperty(value = "券名称")
    private String ticketName;
    @ApiModelProperty(value = "面额")
    private BigDecimal amount;
    @ApiModelProperty(value = "有效天数")
    private Integer useDays;
    @ApiModelProperty(value = "消费限制，满多少使用")
    private BigDecimal consumeCondition;
    @ApiModelProperty(value = "使用渠道：1-外卖；2-堂食；3-全部；")
    private Integer useChannels;
    @ApiModelProperty(value = "总发行量")
    private Integer totalNum;
    @ApiModelProperty(value = "使用量")
    private Integer useNum;
    @ApiModelProperty(value = "关联订单")
    private Integer orderNum;
    @ApiModelProperty(value = "关联营业额")
    private BigDecimal turnover;
    @ApiModelProperty(value = "关联营业额")
    private BigDecimal turnoverNew;
    @ApiModelProperty(value = "领取数量")
    private Integer pullNum;
    @ApiModelProperty(value = "拉新人数")
    private Integer nowMember;

    public BigDecimal getTotalCost(){
       return amount.multiply(new BigDecimal(totalNum));
    }

    public BigDecimal getPracticalCost(){
        return amount.multiply(new BigDecimal(orderNum));
    }
}
