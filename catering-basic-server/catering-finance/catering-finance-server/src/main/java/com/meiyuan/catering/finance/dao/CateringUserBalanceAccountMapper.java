package com.meiyuan.catering.finance.dao;

import com.meiyuan.catering.finance.entity.CateringUserBalanceAccountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Mapper
public interface CateringUserBalanceAccountMapper extends BaseMapper<CateringUserBalanceAccountEntity> {

    /**
     * 描述: 设置支付密码
     *
     * @param userId
     * @param password
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/3/27 10:24
     */
    Boolean updatePayPassword(@Param("userId") Long userId, @Param("password") String password);


    /**
     * 描述: 余额添加
     *
     * @param userId            用户id
     * @param balance           添加的余额
     * @param totalRealAmount   入账金额
     * @param totalCouponAmount 代金券
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/12 15:21
     */
    Boolean balanceAdd(@Param("userId") Long userId, @Param("balance") BigDecimal balance, @Param("totalRealAmount") BigDecimal totalRealAmount, @Param("totalCouponAmount") BigDecimal totalCouponAmount);

    /**
     * 描述: 余额扣减
     *
     * @param userId 用户id
     * @param amount 扣减的余额
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/12 15:25
     */
    Boolean balanceSubtract(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}
