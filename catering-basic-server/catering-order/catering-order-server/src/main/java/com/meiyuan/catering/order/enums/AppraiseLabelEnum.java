package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 评论标签(1:差评 2-3:中评 4-5:好评)
 *
 * @author xxj
 * @date 2019/3/20
 */
@Getter
public enum AppraiseLabelEnum {
    /** 差评*/
    BAD(Values.BAD),
    /** 好评*/
    GOOD(Values.GOOD);

    private final Integer value;

    AppraiseLabelEnum(Integer value) {
        this.value = value;
    }
    public static class Values{
        public static final int BAD = 2;
        public static final int GOOD = 4;
    }

    public static Integer getValue(AppraiseLabelEnum orderStateEnum) {
        return orderStateEnum.value;
    }

    public static AppraiseLabelEnum getByValue(Integer status){
        for (AppraiseLabelEnum orderStateEnum : values()) {
            if (orderStateEnum.value.equals(status)) {
                return orderStateEnum;
            }
        }
        return null;
    }

    public int value() {
        return this.value;
    }
}
