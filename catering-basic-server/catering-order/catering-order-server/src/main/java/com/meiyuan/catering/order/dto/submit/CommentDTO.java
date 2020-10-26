package com.meiyuan.catering.order.dto.submit;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ApiModel("评价订单返回信息DTO")
public class CommentDTO {
    @ApiModelProperty("评论id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long appraiseId;
    @ApiModelProperty("获得的积分")
    private Integer integral;
    @ApiModelProperty(value = "商户ID",hidden = true)
    private Long merchantId;
    @ApiModelProperty("优惠券金额")
    private BigDecimal ticketAmount;
}
