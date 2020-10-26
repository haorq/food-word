package com.meiyuan.catering.core.dto.pay.account;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/5/19 17:50
 * @since v1.1.0
 */
@Data
public class BalanceAccountInfo extends IdEntity {
    /**
     * 用户id 个人用户用户id/企业用户公司id
     */
    private Long userId;
    /**
     * 密码
     */
    private String password;
    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;
    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 账户入账总金额
     */
    private BigDecimal totalRealAmount;
    /**
     * 折扣总金额
     */
    private BigDecimal totalDiscountAmount;
    /**
     * 代金券总金额
     */
    private BigDecimal totalCouponAmount;
    /**
     * 1：企业用户，2：个人用户
     */
    private Integer userType;
    /**
     * 账户状态，1--正常、2--冻结
     */
    private Integer status;
    /**
     * 账户备注
     */
    private String remark;
    /**
     * 密码安全级别
     */
    private Integer passwordLevel;
}
