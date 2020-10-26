package com.meiyuan.catering.user.enums;

import lombok.Getter;

/**
 * @Author lh
 * @Date 2020/6/24 0024 9:37
 * @Description 简单描述 : 广告上下架Enum
 * @Since version-1.0.0
 */
@Getter
public enum AdvertisingPositionEnum {
    /**
     * 手动下架
     **/
    TOP(1, "顶部"),
    /**
     * 自动下架
     **/
    CENTER(2, "中部");
    private Integer status;
    private String desc;

    AdvertisingPositionEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }


}
