package com.meiyuan.catering.finance.vo.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020-03-20
 */
@Data
@ApiModel("余额列表结果VO")
public class UserBalanceAccountQueryListVO extends IdEntity {

    @ApiModelProperty("用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("用户类型 1:企业 2:个人")
    private Integer userType;
    @ApiModelProperty("用户联系方式")
    private String userPhone;
    @ApiModelProperty("冻结金额")
    private BigDecimal frozenAmount;
    @ApiModelProperty("余额")
    private BigDecimal balance;
    @ApiModelProperty("账户入账(真实支付)总金额")
    private BigDecimal totalRealAmount;
    @ApiModelProperty("折扣总金额")
    private BigDecimal totalDiscountAmount;
    @ApiModelProperty("代金券(赠送)总金额")
    private BigDecimal totalCouponAmount;
    @ApiModelProperty("账户状态，1--正常、2--冻结")
    private Integer status;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
