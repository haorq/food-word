package com.meiyuan.catering.allinpay.enums.service.member;

import lombok.Getter;

/**
 * created on 2020/8/14 15:10
 *
 * @author yaozou
 * @since v1.0.0
 */
@Getter
public enum MemberTypeEnums {
    /**
     *
     */
    ENTERPRISE_MEMBER(2L, "企业会员"),
    PERSONAL_MEMBER(3L, "个人会员");
    private Long type;
    private String desc;

    MemberTypeEnums(Long type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
