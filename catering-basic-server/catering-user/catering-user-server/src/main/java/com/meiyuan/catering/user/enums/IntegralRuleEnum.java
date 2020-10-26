package com.meiyuan.catering.user.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * 积分规则枚举
 *
 * @author zengzhangni
 * @date 2020/3/18
 */
@Getter
public enum IntegralRuleEnum {
    /**
     * 生日获取积分
     */
//    BIRTHDAY(Values.BIRTHDAY, "生日获取积分", 1, 0),
    /**
     * 首单获取积分
     */
    FIRST_ORDER(Values.FIRST_ORDER, "首单获取积分", 1, 0),
    /**
     * 完善信息获取积分
     */
//    PERFECT_INFO(Values.PERFECT_INFO, "完善信息获取积分", 1, 0),
    /**
     * 老系统积分 DEL
     */
    OLD_SYSTEM(Values.OLD_SYSTEM, "老系统积分", 1, 1),
    /**
     * 评价获取积分 DEL
     */
    // APPRAISE(Values.APPRAISE, "评价获取积分", 1, 0),
    /**
     * 评价晒图+文字回复
     */
    APPRAISE_PRINT_REPLY(Values.APPRAISE_PRINT_REPLY, "评价获取积分", 1, 0),
    /**
     * 评价晒图获取积分
     */
    APPRAISE_PRINT(Values.APPRAISE_PRINT, "评价获取积分", 1, 0),
    /**
     * 评价文字回复获取积分
     */
    APPRAISE_REPLY(Values.APPRAISE_REPLY, "评价获取积分", 1, 0),
    /**
     * 新人注册成功送积分
     */
    NEW_REGISTER(Values.NEW_REGISTER, "新人注册成功送积分", 1, 0),
    /**
     * 推荐新人注册成功获取积分
     */
    RECOMMEND_REGISTER(Values.RECOMMEND_REGISTER, "推荐新人注册成功获取积分", 1, 1),
    /**
     * 推荐新人首单成功获取积分
     */
    RECOMMEND_FIRST_ORDER(Values.RECOMMEND_FIRST_ORDER, "推荐新人首单成功获取积分", 1, 1);
//    /**
//     * 每消费1元，获得1积分。(平台实际收到的消费金额)
//     */
//    CONSUME(Values.CONSUME, "消费获取积分", 1, 1),
//    /**
//     * 分享菜品获取积分
//     */
//    SHARE_FOOD(Values.SHARE_FOOD, "分享菜品获取积分", 1, 0),
//    /**
//     * 退款扣积分
//     * 当发生用户退单或退款行为时，时实扣减。
//     */
//    REFUND(Values.REFUND, "退款扣积分", 2, 1),
//    /**
//     * 每三个月清零一次积分
//     * 公式：2月+3月+4月+5月-2月（三个月前的积分）
//     */
//    RESET(Values.RESET, "每三个月清零一次积分", 2, 1),
//
//    /**
//     * 积分过期
//     */
//    PAST(Values.PAST, "积分过期", 2, 1),

    /**
     * 积分编码
     */
    private String code;
    /**
     * 积分描述
     */
    private String desc;
    /**
     * 积分类型 1:增加 2:减少
     */
    private Integer type;
    /**
     * 积分规则类型 0:活动 1:系统
     * 系统类型在平台不能添加
     */
    private Integer ruleType;

    IntegralRuleEnum(String code, String desc, Integer type, Integer ruleType) {
        this.code = code;
        this.desc = desc;
        this.type = type;
        this.ruleType = ruleType;
    }

    public static IntegralRuleEnum parse(String code) {
        for (IntegralRuleEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class Values {
        private static final String BIRTHDAY = "L001";
        private static final String FIRST_ORDER = "L002";
        private static final String PERFECT_INFO = "L003";
        private static final String APPRAISE_PRINT_REPLY = "L004";
        private static final String APPRAISE_PRINT = "L005";
        private static final String APPRAISE_REPLY = "L006";
        private static final String CONSUME = "L007";
        private static final String SHARE_FOOD = "L008";
        private static final String RECOMMEND_REGISTER = "L010";
        private static final String RECOMMEND_FIRST_ORDER = "L011";
        private static final String REFUND = "L101";
        private static final String RESET = "L102";
        private static final String PAST = "L103";
        private static final String OLD_SYSTEM = "L104";
        private static final String NEW_REGISTER = "L105";
    }
}
