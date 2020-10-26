package com.meiyuan.catering.finance.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-20
 */
@Data
@ApiModel("用户账户明细")
public class UserAccountDetailVO implements Serializable {

    @ApiModelProperty("当前账户余额")
    private BigDecimal balance;
    @ApiModelProperty("收入/支出")
    private IncomeExpendVO incomeExpend;
    @ApiModelProperty("账单记录列表")
    private List<UserAccountRecordListVO> records;


}
