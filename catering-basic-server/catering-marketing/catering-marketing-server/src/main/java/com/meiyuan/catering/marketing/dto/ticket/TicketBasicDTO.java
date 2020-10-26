package com.meiyuan.catering.marketing.dto.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName TicketBasicDTO
 * @Description
 * @Author gz
 * @Date 2020/8/5 14:14
 * @Version 1.3.0
 */
@Data
public class TicketBasicDTO {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String ticketName;
    /**
     * 金额/折数
     */
    @ApiModelProperty(value = "面额")
    private BigDecimal amount;
    @ApiModelProperty(value = "有效期天数")
    private Integer useDays;
    @ApiModelProperty(value = "每日库存")
    private Integer stock;
    /**
     * 消费限制条件:满多少元可使用； -1：不限制
     */
    @ApiModelProperty(value = "消费限制条件:满多少元可使用")
    private BigDecimal consumeCondition;
    @ApiModelProperty(value = "是否与平台券互斥")
    private Boolean exclusion;
    @ApiModelProperty(value = "使用渠道：1-外卖；2-堂食；3-全部；")
    private Integer useChannels;


}
