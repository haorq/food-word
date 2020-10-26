package com.meiyuan.catering.marketing.vo.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName TicketAppInfoVO
 * @Description
 * @Author gz
 * @Date 2020/8/7 13:41
 * @Version 1.3.0
 */
@Data
public class TicketAppInfoVO extends TicketBasicVO {
    @ApiModelProperty(value = "使用量")
    private Integer useNum;
    @ApiModelProperty(value = "关联订单")
    private Integer orderNum;
    @ApiModelProperty(value = "关联营业额")
    private BigDecimal turnover;
    @ApiModelProperty(value = "实际拉新人数")
    private Integer nowMember;
    @ApiModelProperty(value = "使用有效期开始时间")
    private LocalDateTime useBeginTime;
    @ApiModelProperty(value = "使用有效期结束时间")
    private LocalDateTime useEndTime;
    @ApiModelProperty(value = "有效期类型v1.1.0：1-具体时间；2-有效期天数")
    private Integer indateType;

    public BigDecimal getActivityCost(){
        return super.amount.multiply(new BigDecimal(orderNum));
    }

}
