package com.meiyuan.catering.core.enums.base.merchant;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺绑定设备类型
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum DeviceTypeEnum {
    /**
     * 安卓机
     **/
    ANDROID(Values.ANDROID, "安卓机"),
    /**
     * pos 机
     */
    POS(Values.POS, "pos 机"),
    /**
     * ios
     **/
    IOS(Values.IOS, "ios"),
    /**
     * 易联云打印机
     */
    YLY(Values.YLY, "易联云打印机");

    private Integer status;
    private String desc;
    DeviceTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static DeviceTypeEnum parse(int status) {
        for (DeviceTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer ANDROID = 1;
        private static final Integer POS = 2;
        private static final Integer IOS = 3;
        private static final Integer YLY = 4;
    }
}
