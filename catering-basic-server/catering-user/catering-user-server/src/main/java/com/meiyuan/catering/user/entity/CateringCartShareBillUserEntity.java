package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaoozu
 * @description 购物车拼单人信息
 * @date 2020/3/2511:55
 * @since v1.0.0
 */
@Data
@TableName("catering_cart_share_bill_user")
public class CateringCartShareBillUserEntity extends IdEntity implements Serializable {
    /** 拼单号：发起拼单时生成 */
    private String shareBillNo;
    /** 用户表的用户ID */
    private Long userId;
    /** 微信头像 */
    private String avatar;
    /** 微信昵称 */
    private String nickname;
    /** 选购状态:1--选购中，2--已点完 */
    private Integer billStatus;
}
