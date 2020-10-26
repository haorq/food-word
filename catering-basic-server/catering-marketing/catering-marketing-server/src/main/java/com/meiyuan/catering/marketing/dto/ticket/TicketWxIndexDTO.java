package com.meiyuan.catering.marketing.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName TicketWxIndexDTO
 * @Description 微信优惠券弹窗DTO
 * @Author gz
 * @Date 2020/3/25 13:52
 * @Version 1.1
 */
@Data
public class TicketWxIndexDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String ticketName;
    /**
     * 子类型：1-满减券；2-代金券
     */
    @ApiModelProperty(value = "子类型：1-满减券；2-代金券")
    private Integer childType;
    /**
     * 金额/折数
     */
    @ApiModelProperty(value = "面额")
    private BigDecimal amount;
    /**
     * 消费限制条件:满多少元可使用； -1：不限制
     */
    @ApiModelProperty(value = "消费限制条件:满多少元可使用")
    private BigDecimal consumeCondition;
}
