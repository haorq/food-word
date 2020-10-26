package com.meiyuan.catering.core.check;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 文本内容审核结果
 * @Date  2020/3/28 0028 10:32
 */
@Getter
public enum CheckResultEnum {
    /**
     * 合规
     */
    COMPLIANCE(Values.COMPLIANCE, "合规"),
    /**
     * 不合规
     */
    NOT_COMPLIANCE(Values.NOT_COMPLIANCE, "不合规"),
    /**
     * 疑似
     */
    SUSPECTED(Values.SUSPECTED, "疑似"),
    /**
     * 审核失败
     */
    CHECK_FAILURE(Values.CHECK_FAILURE, "审核失败");

    private Integer status;
    private String desc;
    CheckResultEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static CheckResultEnum parse(int status) {
        for (CheckResultEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer COMPLIANCE = 1;
        private static final Integer NOT_COMPLIANCE = 2;
        private static final Integer SUSPECTED = 3;
        private static final Integer CHECK_FAILURE = 4;
    }
}
