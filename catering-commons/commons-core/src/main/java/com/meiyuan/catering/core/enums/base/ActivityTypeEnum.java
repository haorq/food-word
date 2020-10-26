package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：活动类型枚举
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/10 17:32
 */
@Getter
public enum ActivityTypeEnum {
    /**
     *发券宝
     **/
    SECURITIES(Values.SECURITIES, "发券宝"),
    /**
     *平台补贴
     **/
    SUBSIDY(Values.SUBSIDY, "平台补贴"),
    /**
     *评价赠礼
     **/
    EVALUATE(Values.EVALUATE, "评价赠礼"),
    /**
     *首单奖励
     **/
    FIRST_ORDER(Values.FIRST_ORDER, "首单奖励"),
    /**
     *推荐有奖
     **/
    RECOMMEND(Values.RECOMMEND, "推荐有奖"),
    /**
     *新用户注册
     **/
    REGISTER(Values.REGISTER, "新用户注册");

    private Integer status;
    private String desc;
    ActivityTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ActivityTypeEnum parse(int status) {
        for (ActivityTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer SECURITIES = 6;
        private static final Integer SUBSIDY = 5;
        private static final Integer EVALUATE = 4;
        private static final Integer FIRST_ORDER = 3;
        private static final Integer RECOMMEND = 2;
        private static final Integer REGISTER = 1;
    }
}
