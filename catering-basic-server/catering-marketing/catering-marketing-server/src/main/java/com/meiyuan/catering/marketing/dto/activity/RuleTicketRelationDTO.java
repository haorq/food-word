package com.meiyuan.catering.marketing.dto.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description：规则关联优惠券
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 13:17
 */
@Data
public class RuleTicketRelationDTO {

    @ApiModelProperty(value = "关联优惠券表 id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ruleTicketId;

    @ApiModelProperty(value = "优惠券 id")
    @NotNull(message = "优惠券不能为空！")
    private Long ticketId;

    @ApiModelProperty(value = "发放数量（张）")
    @NotNull(message = "发放数量不能为空！")
    private Integer quantity;

    @ApiModelProperty(value = "被分享人优惠券 id")
    private Long passiveTicketId;

    @ApiModelProperty(value = "被分享人发放数量")
    private Integer passiveAmount;


}
