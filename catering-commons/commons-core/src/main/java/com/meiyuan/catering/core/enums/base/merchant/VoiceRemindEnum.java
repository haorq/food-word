package com.meiyuan.catering.core.enums.base.merchant;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺经营状态枚举
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum VoiceRemindEnum {
    /**
     * 营业
     **/
    OPEN(Values.OPEN, "营业"),
    /**
     * 打烊
     **/
    CLOSE(Values.CLOSE, "打烊");
    private Integer status;
    private String desc;
    VoiceRemindEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static VoiceRemindEnum parse(int status) {
        for (VoiceRemindEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer OPEN = 1;
        private static final Integer CLOSE = 2;
    }
}
