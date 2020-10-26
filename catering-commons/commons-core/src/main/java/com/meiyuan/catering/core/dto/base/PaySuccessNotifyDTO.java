package com.meiyuan.catering.core.dto.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author admin
 */
@Data
public class PaySuccessNotifyDTO implements Serializable {
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 用户/下单人id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 1：普通订单 2:团购订单
     */
    private Integer orderType;
    /**
     * 取餐方式（1：外卖配送，2：到店自取）
     */
    private Integer deliveryWay;
    /**
     * true :是
     */
    private Boolean firstOrder;


    /**
     * 订单支付成功的时间
     */
    private LocalDateTime payTime;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * V1.3.0 门店ID
     */
    private Long shopId;

    /**
     * 订单商品信息
     */
    List<PayGoodsDTO> list;
    /**
     * 订单活动信息
     */
    List<PayActivityDTO> activityList;


}
