package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：活动进度状态枚举（需根据时间判定得到，返回前端作展示）
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 17:12
 */
@Getter
public enum ActivityStateEnum {
    /**
     *已冻结
     **/
    DOWN_SHELF(ActivityStateEnum.Values.DOWN_SHELF, "已冻结"),
    /**
     *已结束
     **/
    END_SHELF(ActivityStateEnum.Values.END_SHELF, "已结束"),
    /**
     *进行中
     **/
    UNDER_WAY(ActivityStateEnum.Values.UNDER_WAY, "进行中"),
    /**
     *待开始
     **/
    WAIT_FOR(ActivityStateEnum.Values.WAIT_FOR, "待开始");
    
    private Integer status;
    private String desc;
    ActivityStateEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ActivityStateEnum parse(int status) {
        for (ActivityStateEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer DOWN_SHELF = 4;
        private static final Integer END_SHELF = 3;
        private static final Integer UNDER_WAY = 2;
        private static final Integer WAIT_FOR = 1;
    }
}
