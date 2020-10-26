package com.meiyuan.catering.allinpay.model.bean.order;

import com.meiyuan.catering.core.util.BaseUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/25 09:09
 * @description 通联收款信息参数
 **/

@Data
public class AllinReciever {

    /**
     * 商户系统用户标识，商户系统中唯一编号。
     * <p>
     * 必填
     */
    private String bizUserId;
    /**
     * 金额，单位：分
     * <p>
     * 必填
     */
    private Integer amount;

    public AllinReciever(String bizUserId, Integer amount) {
        this.bizUserId = bizUserId;
        this.amount = amount;
    }

    public AllinReciever(Long bizUserId, BigDecimal amount) {
        this.bizUserId = bizUserId.toString();
        this.amount = BaseUtil.yuanToFen(amount);
    }

    public AllinReciever(String bizUserId) {
        this.bizUserId = bizUserId;
    }
}
