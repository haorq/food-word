package com.meiyuan.catering.allinpay.enums.service.member;

import lombok.Getter;

/**
 * 描述: 证件枚举
 *
 * @author zengzhangni
 * @date 2020/9/29 16:42
 * @since v1.5.0
 */
@Getter
public enum IdentityTypeEnums {
    /**
     *
     */
    ID_CARD(1L, "身份证"),
    PASSPORT(2L, "护照"),
    RESIDENCE_BOOKLET(8L, "户口簿"),
    HONGKONG_MACAO_PERMIT(9L, "港澳居民来往 内地通行证"),
    INTERIM_IDENTITY_CARD(10L, "临时身份证");
    private Long type;
    private String desc;

    IdentityTypeEnums(Long type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
