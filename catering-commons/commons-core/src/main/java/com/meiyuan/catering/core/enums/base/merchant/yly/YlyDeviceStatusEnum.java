package com.meiyuan.catering.core.enums.base.merchant.yly;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 易联云打印机状态枚举
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum YlyDeviceStatusEnum {
    /**
     * 离线
     **/
    OUT_LINE(Values.OUT_LINE, "离线"),
    /**
     * 在线
     */
    ONE_LINE(Values.ONE_LINE, "在线"),
    /**
     * 缺纸
     **/
    OUT_PAPER(Values.OUT_PAPER, "缺纸");

    private Integer status;
    private String desc;
    YlyDeviceStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static YlyDeviceStatusEnum parse(int status) {
        for (YlyDeviceStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer OUT_LINE = 0;
        private static final Integer ONE_LINE = 1;
        private static final Integer OUT_PAPER = 2;
    }
}
