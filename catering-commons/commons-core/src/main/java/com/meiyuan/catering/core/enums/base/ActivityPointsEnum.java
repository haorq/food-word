package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * description：活动积分枚举
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/10 10:47
 */
@Getter
public enum ActivityPointsEnum {
    /**
     * 勾选积分
     **/
    YES(Values.YES, Boolean.TRUE,"积分"),
    /**
     * 未勾选积分
     **/
    NO(Values.NO, Boolean.FALSE,"");
    private Integer status;
    private Boolean flag;
    private String desc;
    ActivityPointsEnum(Integer status, Boolean flag, String desc){
        this.status = status;
        this.flag = flag;
        this.desc = desc;
    }

    public static ActivityPointsEnum parse(int status) {
        for (ActivityPointsEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }
    public static ActivityPointsEnum parseBool(boolean flag) {
        for (ActivityPointsEnum type : values()) {
            if (type.flag == flag) {
                return type;
            }
        }
        throw new CustomException("非法flag");
    }

    public static class  Values{
        private static final Integer NO = 0;
        private static final Integer YES = 1;
    }
}
