package com.meiyuan.catering.finance.feign;

import com.meiyuan.catering.core.dto.pay.account.BalanceAccountInfo;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.query.recharge.UserBalanceAccountQueryDTO;
import com.meiyuan.catering.finance.service.CateringUserBalanceAccountService;
import com.meiyuan.catering.finance.vo.account.UserAccountDetailVO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryDetailVO;
import com.meiyuan.catering.finance.vo.recharge.UserBalanceAccountQueryListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/5/19 9:38
 * @since v1.1.0
 */
@Service
public class UserBalanceAccountClient {

    @Resource
    private CateringUserBalanceAccountService service;

    /**
     * 描述:分页列表
     *
     * @param dto
     * @return
     * @author zengzhangni
     * @date 2020/5/19 10:11
     * @since v1.1.0
     */
    public Result<PageData<UserBalanceAccountQueryListVO>> pageList(UserBalanceAccountQueryDTO dto) {
        return Result.succ(service.pageList(dto));
    }


    /**
     * 描述:用户账户信息
     *
     * @param userId
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 10:12
     * @since v1.1.0
     */
    public Result<BalanceAccountInfo> userAccountInfo(Long userId) {
        return Result.succ(service.userAccountInfo(userId));
    }


    /**
     * 描述:创建账户
     *
     * @param userId
     * @param userType
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 10:12
     * @since v1.1.0
     */
    public Result<Boolean> createAccount(Long userId, Integer userType) {
        return Result.succ(service.createAccount(userId, userType));
    }


    /**
     * 描述:修改用户支付密码
     *
     * @param userId
     * @param password
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 10:12
     * @since v1.1.0
     */
    public Result<Boolean> updatePayPassword(Long userId, String password) {
        return Result.succ(service.updatePayPassword(userId, password));
    }

    /**
     * 描述:退款余额添加
     *
     * @param userId
     * @param refundAmount
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 10:12
     * @since v1.1.0
     */
    public Result<Boolean> balanceAdd(Long userId, BigDecimal refundAmount) {
        return Result.succ(service.balanceAdd(userId, refundAmount));
    }

    /**
     * 描述:充值余额添加
     *
     * @param userId
     * @param amount
     * @param totalRealAmount
     * @param totalCouponAmount
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 10:12
     * @since v1.1.0
     */
    public Result<Boolean> balanceAdd(Long userId, BigDecimal amount, BigDecimal totalRealAmount, BigDecimal totalCouponAmount) {
        return Result.succ(service.balanceAdd(userId, amount, totalRealAmount, totalCouponAmount));
    }


    /**
     * 描述:余额减少
     *
     * @param userId
     * @param payAmount
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 10:12
     * @since v1.1.0
     */
    public Result<Boolean> balanceSubtract(Long userId, BigDecimal payAmount) {
        return Result.succ(service.balanceSubtract(userId, payAmount));
    }

    /**
     * 描述:老系统余额添加
     *
     * @param userId
     * @param amount
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 10:12
     * @since v1.1.0
     */
    public Result<Boolean> systemBalanceAdd(Long userId, BigDecimal amount) {
        return Result.succ(service.systemBalanceAdd(userId, amount));
    }

    /**
     * 描述:获得用户余额
     *
     * @param userId
     * @return com.meiyuan.catering.core.util.Result<java.math.BigDecimal>
     * @author zengzhangni
     * @date 2020/5/19 10:12
     * @since v1.1.0
     */
    public Result<BigDecimal> balance(Long userId) {
        return Result.succ(service.balance(userId));
    }

    /**
     * 描述:用户账户详情
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 14:50
     * @since v1.1.0
     */
    public Result<UserBalanceAccountQueryDetailVO> getBalanceAccountDetailById(Long id) {
        return Result.succ(service.getBalanceAccountDetailById(id));
    }

    /**
     * 描述:账号明细
     *
     * @param userId
     * @param pageDTO
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/21 17:26
     * @since v1.1.0
     */
    public Result<UserAccountDetailVO> accountDetail(Long userId, BasePageDTO pageDTO) {
        return Result.succ(service.accountDetail(userId, pageDTO));
    }
}
