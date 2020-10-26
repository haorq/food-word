package com.meiyuan.catering.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额账户明细
 *
 * @author zengzhangni
 * @date 2020-03-16
 */
@Data
@TableName("catering_user_balance_account_record")
public class CateringUserBalanceAccountRecordEntity extends IdEntity {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 款项标题
     */
    private String title;
    /**
     * 金额
     */
    private BigDecimal account;
    /**
     * 资金类别：1--收入，2--支出
     */
    private Integer type;
    /**
     * 款项类别，1--充值，2--订单支付，3--订单退款，4--余额退款（充值退款）5:初始化余额/老系统余额
     */
    private Integer fundType;
    /**
     * 款项流水号，fund_type为1--充值交易流水号，2--订单支付交易流水号，3--订单退款流水号，4--余额退款（充值退款）流水号 5:无
     */
    private String fundNo;
    /**
     * 状态，1--有效，2--无效
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
