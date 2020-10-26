package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * @Author MeiTao
 * @Description  订单商品 收货状态枚举 : 1:已收到货 2:未收到货
 * @Date  2020/4/26 0026 14:46
 */
@Getter
public enum CargoStatusEnum {
    /**
     * 已收到货
     **/
    RECEIVED(CargoStatusEnum.Values.RECEIVED, "已收到货"),
    /**
     * 未收到货
     **/
    NOT_RECEIVED(CargoStatusEnum.Values.NOT_RECEIVED, "未收到货");

    private Integer status;
    private String desc;
    CargoStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static CargoStatusEnum parse(int status) {
        for (CargoStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer NOT_RECEIVED = 2;
        private static final Integer RECEIVED = 1;
    }
}
