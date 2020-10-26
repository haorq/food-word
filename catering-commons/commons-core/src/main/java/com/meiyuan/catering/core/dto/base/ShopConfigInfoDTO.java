package com.meiyuan.catering.core.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DiscountInfo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Description 商户店铺配置信息
 * @Date 2020/3/24 0024 14:03
 */
@Data
public class ShopConfigInfoDTO implements Serializable {
    private static final long serialVersionUID = 723719346349430137L;
    /**
     * 店铺id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    /**
     * 1-企业 2-个人 3-全部
     */
    private Integer deliveryObject;
    /**
     * 配送价格
     */
    private BigDecimal deliveryPrice;
    /**
     * 满单免配送金额
     */
    private BigDecimal freeDeliveryPrice;
    /**
     * 订单起送金额
     */
    private BigDecimal leastDeliveryPrice;
    /**
     * 配送范围
     */
    private Integer deliveryRange;
    /**
     * 配送范围规则:1-距离 2-直线
     */
    private Integer deliveryRule;

    /**
     * 业务支持：1：仅配送，2：仅自提，3：全部
     */
    private Integer businessSupport;


    /**
     * 自动接单:1-自动 2-不自动
     */
    private Integer autoReceipt;

    /**
     * 店铺配送时间范围
     */
    private List<TimeRangeDTO> deliveryTimeRanges;

    /**
     * 店铺自提时间范围
     */
    private List<TimeRangeDTO> pickupTimeRanges;


    public String getDispatching() {
        //业务支持：1：仅配送，2：仅自提，3：全部
        Integer pickup = 2;
        if (Objects.equals(businessSupport, pickup)) {
            return null;
        }
        //配送费
        if (BaseUtil.isZeroOrNull(deliveryPrice)) {
            return null;
        }
        //满单免配送金额
        if (BaseUtil.isZeroOrNull(freeDeliveryPrice)) {
            return null;
        }
        return DiscountInfo.dispatchingDiscountMsg(freeDeliveryPrice);

    }

}
