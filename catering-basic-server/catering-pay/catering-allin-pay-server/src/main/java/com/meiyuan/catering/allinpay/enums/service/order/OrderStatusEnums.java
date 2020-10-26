package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * created on 2020/8/18 17:44
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum OrderStatusEnums {
    /**  */
    NON_PAYMENT(1L,"未支付"),
    TRANSACTION_FAIL(3L,"交易失败 交易过程中出现错误"),
    TRANSACTION_SUCC(4L,"交易成功"),
    TRANSACTION_SUCC_REFUND(5L,"交易成功-发生退款 交易成功，但是发生了退款"),
    CLOSE(6L,"关闭 未支付的订单，每天日终（00:30）批量关闭已创建未支付， 且创建时间大于 24 小时的订单。 进行中的订单，8 天日终（00:30）批量关闭进行中的订单。"),
    PANDING(99L,"进行中")
    ;
    private Long status;
    private String name;
    OrderStatusEnums(Long status,String name){
        this.status = status;
        this.name = name;
    }

    public Long getStatus() {
        return status;
    }
}
