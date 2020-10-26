package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingPictureTypeEnum
 * @Description 营销图片枚举
 * @Author gz
 * @Date 2020/3/18 13:32
 * @Version 1.1
 */
@Getter
public enum MarketingPictureTypeEnum {
    /**
     * 封面图
     */
    FACE(MarketingPictureTypeEnum.Values.FACE,"封面图"),
    /**
     * 详情图
     */
    DETAILS(MarketingPictureTypeEnum.Values.DETAILS,"详情图"),
    /**
     * 分享图
     */
    SHARE(MarketingPictureTypeEnum.Values.SHARE,"分享图");
    private Integer status;
    private String desc;
    MarketingPictureTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingPictureTypeEnum parse(int status) {
        for (MarketingPictureTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer FACE = 1;
        private static final Integer DETAILS = 2;
        private static final Integer SHARE = 3;
    }
}
