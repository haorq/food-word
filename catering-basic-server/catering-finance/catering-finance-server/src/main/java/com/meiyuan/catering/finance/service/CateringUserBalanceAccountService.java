package com.meiyuan.catering.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.pay.account.BalanceAccountInfo;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.finance.entity.CateringUserBalanceAccountEntity;
import com.meiyuan.catering.finance.query.recharge.UserBalanceAccountQueryDTO;
import com.meiyuan.catering.finance.vo.account.UserAccountDetailVO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryDetailVO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryListVO;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
public interface CateringUserBalanceAccountService extends IService<CateringUserBalanceAccountEntity> {

    /**
     * 描述: 分页列表
     *
     * @param dto 查询条件
     * @return com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryListVO>
     * @author zengzhangni
     * @date 2020/6/23 10:49
     * @since v1.1.1
     */
    PageData<UserBalanceAccountQueryListVO> pageList(UserBalanceAccountQueryDTO dto);

    /**
     * 描述:通过userid 查询用户账户信息
     *
     * @param userId
     * @return com.meiyuan.catering.core.dto.pay.account.BalanceAccountInfo
     * @author zengzhangni
     * @date 2020/5/19 17:51
     * @since v1.1.0
     */
    BalanceAccountInfo userAccountInfo(Long userId);


    /**
     * 描述: 创建账户
     *
     * @param userId   用户id
     * @param userType 用户类型
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/23 10:49
     * @since v1.1.1
     */
    Boolean createAccount(Long userId, Integer userType);

    /**
     * 描述: 修改用户支付密码
     *
     * @param userId   用户id
     * @param password 密码
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/23 10:49
     * @since v1.1.1
     */
    Boolean updatePayPassword(Long userId, String password);

    /**
     * 描述: 退款余额添加
     *
     * @param userId       用户id
     * @param refundAmount 退款金额
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 16:58
     */
    Boolean balanceAdd(Long userId, BigDecimal refundAmount);

    /**
     * 描述:充值余额添加
     *
     * @param userId            用户id
     * @param amount            金额
     * @param totalRealAmount   totalRealAmount
     * @param totalCouponAmount totalCouponAmount
     * @return void
     * @author zengzhangni
     * @date 2020/5/12 15:38
     */
    Boolean balanceAdd(Long userId, BigDecimal amount, BigDecimal totalRealAmount, BigDecimal totalCouponAmount);


    /**
     * 描述: 余额减少
     *
     * @param userId    用户id
     * @param payAmount 支付金额
     * @return void
     * @author zengzhangni
     * @date 2020/4/2 11:12
     */
    Boolean balanceSubtract(Long userId, BigDecimal payAmount);

    /**
     * 描述: 老系统余额添加
     *
     * @param userId 用户id
     * @param amount 金额
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 16:58
     */
    Boolean systemBalanceAdd(Long userId, BigDecimal amount);

    /**
     * 描述: 获得用户余额
     *
     * @param userId 用户ID
     * @return java.math.BigDecimal
     * @author yaozou
     * @date 2020/4/16 10:25
     * @since v1.0.0
     */
    BigDecimal balance(Long userId);

    /**
     * 描述:用户账户详情
     *
     * @param id id
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 14:50
     * @since v1.1.0
     */
    UserBalanceAccountQueryDetailVO getBalanceAccountDetailById(Long id);

    /**
     * 描述:账号明细
     *
     * @param userId  用户id
     * @param pageDTO 查询条件
     * @return com.meiyuan.catering.finance.vo.account.UserAccountDetailVO
     * @author zengzhangni
     * @date 2020/5/21 17:26
     * @since v1.1.0
     */
    UserAccountDetailVO accountDetail(Long userId, BasePageDTO pageDTO);
}
