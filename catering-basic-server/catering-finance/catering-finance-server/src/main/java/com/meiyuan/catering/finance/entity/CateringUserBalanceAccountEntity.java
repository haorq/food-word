package com.meiyuan.catering.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额账户
 *
 * @author zengzhangni
 * @date 2020-03-16
 */
@Data
@TableName("catering_user_balance_account")
public class CateringUserBalanceAccountEntity extends IdEntity {

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
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 账户备注
     */
    private String remark;
    /**
     * 密码安全级别
     */
    private Integer passwordLevel;

}
