package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yaoozu
 * @description 购物车拼单信息
 * @date 2020/3/2511:55
 * @since v1.0.0
 */
@Data
public class CartShareBillBaseDTO{
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /** 拼单号：发起拼单时生成 */
    private String shareBillNo;
    /** 用户表的用户ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shareUserId;
    /** 商户id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    /** 门店id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    /** 订单id*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    /** 菜单id*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    /** 创建时间 */
    private LocalDateTime shareTime;
    /** 状态：1--选购中，2--结算中,3--已结算 */
    private Integer status;
    /** 拼单类别:1--菜单，2--商品 */
    private Integer type;

}
