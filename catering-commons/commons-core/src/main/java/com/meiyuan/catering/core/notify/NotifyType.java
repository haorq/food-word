package com.meiyuan.catering.core.notify;

/**
 * @author admin
 */

public enum NotifyType {
    /**
     * 通用验证码
     */
    VERIFY_CODE_NOTIFY("verify_code_notify"),
    /**
     * 账号创建成功通知
     */
    CREATE_ACCOUNT_SUCCESS_NOTIFY("create_account_success_notify"),
    /**
     * 打印机状态改变通知
     */
    UPDATE_PRINT_STATUS_NOTIFY("update_print_status_notify"),
    /**
     * 企业账号创建成功
     */
    CREATE_ENTERPRISE_ACCOUNT_SUCCESS_NOTIFY("create_enterprise_account_success_notify"),
    /**
     * 商户取消订单通知
     */
    MERCHANT_CANCEL_ORDER_NOTIFY("merchant_cancel_order_notify"),
    /**
     * 下单成功自提短信通知
     */
    SELF_PICKUP_NOTIFY("self_pickup_notify"),
    /**
     * 下单成功上门送餐通知
     */
    HOME_DELIVERY_NOTIFY("home_delivery_notify"),
    /**
     * 距自提时间还有1小时短信提醒通知
     */
    SELF_PICKUP_ONE_HOUR_NOTIFY("self_pickup_one_hour_notify"),
    /**
     * 拼团成功自提短信通知
     */
    GROUP_ORDER_SELF_PICKUP_NOTIFY("group_order_self_pickup_notify"),
    /**
     * 拼团成功送货上门通知
     */
    GROUP_ORDER_HOME_DELIVERY_NOTIFY("group_order_home_delivery_notify"),
    /**
     * 拼团失败通知
     */
    GROUP_ORDER_FAILURE_NOTIFY("group_order_failure_notify"),
    /**
     * 商户账号创建成功通知
     */
    CREATE_MERCHANT_SUCCESS_NOTIFY("create_merchant_success_notify"),

    /**
     * 商户修改手机号成功通知
     */
    UPDATE_ACCOUNT_SUCCESS_NOTIFY("update_account_success_notify"),

    /**
     * 商户申请被拒绝通知
     */
    MERCHANT_APPLY_REJECT_NOTIFY("merchant_apply_reject_notify");

    private String type;

    NotifyType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
