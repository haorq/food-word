package com.meiyuan.catering.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yaoozu
 * @description 购物车拼单人信息
 * @date 2020/3/2511:55
 * @since v1.0.0
 */
@Data
@TableName("catering_cart_share_bill")
public class CateringCartShareBillEntity extends IdEntity implements Serializable {
    /**
     * 拼单号：发起拼单时生成
     */
    private String shareBillNo;
    /**
     * 用户表的用户ID
     */
    private Long shareUserId;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 店铺id
     *
     * @since v1.2.0
     */
    private Long shopId;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 菜单id
     */
    private Long menuId;
    /**
     * 创建时间
     */
    private LocalDateTime shareTime;
    /**
     * 状态：1--选购中，2--结算中,3--已结算
     */
    private Integer status;
    /**
     * 拼单类别:1--菜单，2--商品
     */
    private Integer type;

}
