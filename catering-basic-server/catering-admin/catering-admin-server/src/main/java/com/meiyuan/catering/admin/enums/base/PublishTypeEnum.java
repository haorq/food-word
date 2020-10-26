package com.meiyuan.catering.admin.enums.base;

import lombok.Getter;

/**
 * @Author zzn
 * @Date 2020/6/23 0023 18:01
 * @Description 简单描述 : 广告发布类型
 * @Since version-1.0.0
 */
@Getter
public enum PublishTypeEnum {
    /**
     * 1:立即发布
     **/
    PROMPTLY(1, "立即发布"),
    /**
     * 2:预约发布
     **/
    SUBSCRIBE(2, "预约发布");
    private Integer status;
    private String desc;

    PublishTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }


}
