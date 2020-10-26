package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * @author GongJunZheng
 * @date 2020/10/09 14:10
 * @description 标准账户集枚举
 **/

public enum SourceAccountEnums {

    STANDARD_BALANCE_ACCOUNT("标准余额账户集", "100001", Boolean.TRUE, Boolean.TRUE, "用于平台管理自有资金，一般为经营收入、手续费收入"),
    STANDARD_CAUTION_MONEY_ACCOUNT("标准保证金账户集", "100002", Boolean.TRUE, Boolean.FALSE, "用于平台向平台用户收取、计提保证金"),
    RESERVED_AMOUNT_ACCOUNT("准备金额度账户集", "100003", Boolean.TRUE, Boolean.FALSE, "用于通商云向平台收取交易手续费、保证金等费用。注：平台预充值给通商云。"),
    MIDDLE_ACCOUNT_A("中间账户集A", "100004", Boolean.FALSE, Boolean.FALSE, "用于正向交易资金的中间账户"),
    MIDDLE_ACCOUNT_B("中间账户集B", "100005", Boolean.FALSE, Boolean.FALSE, "用于逆向交易资金的中间账户"),
    STANDARD_MARKETING_ACCOUNT("标准营销账户集", "2000000", Boolean.TRUE, Boolean.TRUE, "用于平台管理营销活动相关的资金"),
    PREPAID_CARD_ACCOUNT("预付卡账户集", "100006", Boolean.TRUE, Boolean.TRUE, "用于平台管理预付卡业务资金注：默认不创建，发生预付卡交易时，系统自动创建。");
    /**
     * 账户集名称
     */
    private String name;
    /**
     * 账户集编号
     */
    private String number;
    /**
     * 是否支持接口充值
     */
    private Boolean deposit;
    /**
     * 是否支持接口提现
     */
    private Boolean withdraw;
    /**
     * 账户用途说明
     */
    private String desc;

    SourceAccountEnums(String name, String number, Boolean deposit, Boolean withdraw, String desc) {
        this.name = name;
        this.number = number;
        this.deposit = deposit;
        this.withdraw = withdraw;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Boolean getDeposit() {
        return deposit;
    }

    public Boolean getWithdraw() {
        return withdraw;
    }

    public String getDesc() {
        return desc;
    }
}
