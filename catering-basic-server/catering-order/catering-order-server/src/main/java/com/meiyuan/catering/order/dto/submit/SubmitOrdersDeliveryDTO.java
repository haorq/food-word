package com.meiyuan.catering.order.dto.submit;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 功能描述: 订单提交配送信息DTO
 * @author: XiJie Xie
 * @date: 2020/3/10 10:45
 * @version v 1.0
 */
@Data
@TableName("catering_orders_delivery")
public class SubmitOrdersDeliveryDTO implements Serializable {
    private static final long serialVersionUID = -15126619452822257L;

    /** 取餐人姓名 */
    private String consigneeName;
    /** 取餐人性别（1：男；2-女；3-其他） */
    private Integer consigneeSex;
    /** 取餐人电话 */
    private String consigneePhone;
    /** 餐具数量（单位：份） */
    private Integer tableware;
    /** 收货地区 */
    private String consigneeArea;
    /** 详细地址 */
    private String consigneeAddress;
    /** 预计送达、取餐时间 */
    private LocalDateTime estimateTime;
    /** 预计送达、取餐截止时间 */
    private LocalDateTime estimateEndTime;
}
