package com.meiyuan.catering.allinpay.enums.service;

/**
 * @author zengzhangni
 * @description 通联支付调用接口地址枚举类
 */

public enum ApiEnums {
    /**
     * memberService
     */
    CREATE_MEMBER("allinpay.yunst.memberService.createMember", "1.0"),
    SEND_VERIFICATION_CODE("allinpay.yunst.memberService.sendVerificationCode", "1.0"),
    BIND_PHONE("allinpay.yunst.memberService.bindPhone", "1.0"),
    SIGN_CONTRACT("allinpay.yunst.memberService.signContract", "1.0"),
    SIGN_CONTRACT_QUERY("allinpay.yunst.memberService.signContractQuery", "1.0"),
    SET_REAL_NAME("allinpay.yunst.memberService.setRealName", "1.0"),
    SET_COMPANY_INFO("allinpay.yunst.memberService.setCompanyInfo", "1.0"),
    GET_MEMBER_INFO("allinpay.yunst.memberService.getMemberInfo", "1.0"),
    GET_BANK_CARD_BIN("allinpay.yunst.memberService.getBankCardBin", "1.0"),

    APPLY_BIND_BANK_CARD("allinpay.yunst.memberService.applyBindBankCard", "1.0"),
    BIND_BANK_CARD("allinpay.yunst.memberService.bindBankCard", "1.0"),
    SET_SAFE_CARD("allinpay.yunst.memberService. setSafeCard", "1.0"),
    QUERY_BANK_CARD("allinpay.yunst.memberService.queryBankCard", "1.0"),
    UNBIND_BANK_CARD("allinpay.yunst.memberService.unbindBankCard", "1.0"),

    LOCK_MEMBER("allinpay.yunst.memberService.lockMember", "1.0"),
    UNLOCK_MEMBER("allinpay.yunst.memberService.unlockMember", "1.0"),

    SET_PAY_PWD("allinpay.yunst.memberService.setPayPwd", "1.0"),
    UPDATE_PAY_PWD("allinpay.yunst.memberService.updatePayPwd", "1.0"),
    RESET_PAY_PWD("allinpay.yunst.memberService.resetPayPwd", "1.0"),

    APPLY_BIND_ACCT("allinpay.yunst.memberService.applyBindAcct", "1.0"),

    UNBIND_PHONE("allinpay.yunst.memberService.unbindPhone", "1.0"),
    BANK_CARD_CHANGE_BIND_PHONE("allinpay.yunst.memberService.bankCardChangeBindPhone", "1.0"),
    VERIFY_BANK_CARD_CHANGE_BIND_PHONE("allinpay.yunst.memberService.verifyBankCardChangeBindPhone", "1.0"),
    VSP_TERMID_SERVICE("allinpay.yunst.memberService.vspTermidService", "1.0"),

    /**
     * orderService
     */
    WITHDRAW_APPLY("allinpay.yunst.orderService.withdrawApply", "1.0"),
    AGENT_COLLECT_APPLY("allinpay.yunst.orderService.agentCollectApply", "1.0"),
    SIGNAL_AGENT_PAY("allinpay.yunst.orderService.signalAgentPay", "1.0"),
    BATCH_AGENT_PAY("allinpay.yunst.orderService.batchAgentPay", "1.0"),
    PAY_BY_PWD("allinpay.yunst.orderService.payByPwd", "1.0"),
    PAY_BY_BACK_SMS("allinpay.yunst.orderService.payByBackSMS", "1.0"),
    PAY_BY_SMS("allinpay.yunst.orderService.payBySMS", "1.0"),
    REFUND("allinpay.yunst.orderService.refund", "1.0"),
    GET_ORDER_DETAIL("allinpay.yunst.orderService.getOrderDetail", "1.0"),
    DEPOSIT_APPLY("allinpay.yunst.orderService.depositApply", "1.0"),
    RESEND_PAY_SMS("allinpay.yunst.orderService.resendPaySMS", "1.0"),

    QUERY_INEXP_DETAIL("allinpay.yunst.orderService.queryInExpDetail", "1.0"),
    GET_ORDER_SPLIT_RULE_LIST_DETAIL("allinpay.yunst.orderService.getOrderSplitRuleListDetail", "1.0"),

    QUERY_BALANCE("allinpay.yunst.orderService.queryBalance", "1.0"),
    APPLICATION_TRANSFER("allinpay.yunst.orderService.applicationTransfer", "1.0"),


    /**
     * merchantService
     */
    QUERY_MERCHANT_BALANCE("allinpay.yunst.merchantService.queryMerchantBalance", "1.0"),
    ;
    private String method;
    private String version;

    ApiEnums(String method, String version) {
        this.method = method;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public String getVersion() {
        return version;
    }
}
