package com.meiyuan.catering.es.enums.marketing;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author gz
 * @date 2020/3/10 10:59
 * @description 营销活动对象枚举
 **/
@Getter
public enum MarketingUsingObjectEnum {
    /**
     * 全部
     **/
    ALL(Values.ALL, "全部"),
    /**
     * 个人
     **/
    PERSONAL(Values.PERSONAL, "个人"),
    /**
     * 企业
     **/
    ENTERPRISE(Values.ENTERPRISE, "企业");
    private Integer status;
    private String desc;
    MarketingUsingObjectEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingUsingObjectEnum parse(int status) {
        for (MarketingUsingObjectEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer ALL = 0;
        private static final Integer PERSONAL = 1;
        private static final Integer ENTERPRISE = 2;
    }
}
