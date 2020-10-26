package com.meiyuan.catering.finance.vo.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.finance.vo.account.ConsumeRefundVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020-03-20
 */
@Data
@ApiModel("余额详情VO")
public class UserBalanceAccountQueryDetailVO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("用户联系方式")
    private String userPhone;
    @ApiModelProperty("冻结金额")
    private BigDecimal frozenAmount;
    @ApiModelProperty("收入/支出")
    private ConsumeRefundVO expendVO;
    @ApiModelProperty("余额")
    private BigDecimal balance;
    @ApiModelProperty("账户入账(真实支付)总金额")
    private BigDecimal totalRealAmount;
    @ApiModelProperty("折扣总金额")
    private BigDecimal totalDiscountAmount;
    @ApiModelProperty("代金券(赠送)总金额")
    private BigDecimal totalCouponAmount;

}
