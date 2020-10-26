package com.meiyuan.catering.core.constant;

/**
 * 账户记录常量
 *
 * @author zengzhangni
 * @date 2020/3/30
 */
public class AccountRecordConstant {

    /**
     * 资金类别 1--收入，2--支出
     */
    public interface Type {
        Integer INCOME = 1;
        Integer EXPEND = 2;
    }

    /**
     * 状态，1--有效，2--无效
     */
    public interface Status {
        Integer VALID = 1;
        Integer INVALID = 2;
    }

    /**
     * 款项类别，1--充值，2--订单支付，3--订单退款，4--余额退款（充值退款）5:初始化余额/老系统余额
     */
    public interface FundType {
        Integer RECHARGE_APY = 1;
        Integer ORDER_PAY = 2;
        Integer ORDER_REFUND = 3;
        Integer BALANCE_REFUND = 4;
        Integer SYSTEM_BALANCE = 5;
    }

    /**
     * 款项标题
     */
    public interface Title {
        String RECHARGE_APY = "充值";
        String ORDER_PAY = "订单支付";
        String ORDER_REFUND = "订单退款";
        String SYSTEM_BALANCE = "老系统余额";
    }
}
