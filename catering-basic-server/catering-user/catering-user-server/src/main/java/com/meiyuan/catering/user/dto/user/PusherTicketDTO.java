package com.meiyuan.catering.user.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName PusherTicketDTO
 * @Description 地推员优惠券DTO
 * @Author gz
 * @Date 2020/5/7 11:02
 * @Version 1.1
 */
@Data
@ApiModel("地推员优惠券 Dto")
public class PusherTicketDTO {
    @ApiModelProperty(value = "优惠券id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ticketId;
    @ApiModelProperty(value = "优惠券名称")
    private String ticketName;
    @ApiModelProperty(value = "优惠券类型：1-满减券；2-代金券")
    private Integer childType;
    @ApiModelProperty(value = "面额")
    private BigDecimal amount;
    @ApiModelProperty(value = "消费限制条件")
    private BigDecimal consumeCondition;
    @ApiModelProperty(value = "触发条件：1-首单下单成功获取；2-新人注册成功获取；3-推荐人推荐新用户注册成功获取；4-推荐人推荐用户下单成功获取；5-系统无触发条件发券")
    private Integer onClick;
    /**
     * 使用有效期开始时间
     */
    @ApiModelProperty(value = "使用有效期开始时间")
    private LocalDateTime useBeginTime;
    /**
     * 使用有效期结束时间
     */
    @ApiModelProperty(value = "使用有效期结束时间")
    private LocalDateTime useEndTime;
    @ApiModelProperty(hidden = true)
    private Integer useDays;
    @ApiModelProperty(hidden = true)
    private Integer indateType;

}
