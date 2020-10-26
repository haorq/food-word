package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * @author yaoozu
 * @description 购物车拼单人信息
 * @date 2020/3/2511:55
 * @since v1.0.0
 */
@Data
public class CartShareBillUserDTO extends IdEntity {
    /** 拼单号：发起拼单时生成 */
    private String shareBillNo;
    /** 用户表的用户ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    /** 微信头像 */
    private String avatar;
    /** 微信昵称 */
    private String nickname;
    /** 选购状态:1--选购中，2--已点完 */
    private Integer billStatus;
}
