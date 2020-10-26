package com.meiyuan.catering.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.finance.entity.CateringUserBalanceAccountRecordEntity;
import com.meiyuan.catering.finance.vo.account.ConsumeRefundVO;
import com.meiyuan.catering.finance.vo.account.IncomeExpendVO;
import com.meiyuan.catering.finance.vo.account.UserAccountRecordListVO;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author zengzhangni
 * @date 2020-03-16
 */
public interface CateringUserBalanceAccountRecordService extends IService<CateringUserBalanceAccountRecordEntity> {


    /**
     * 描述: 总收入/总支出
     *
     * @param userId 用户id
     * @return com.meiyuan.catering.finance.vo.account.IncomeExpendVO
     * @author zengzhangni
     * @date 2020/6/23 10:48
     * @since v1.1.1
     */
    IncomeExpendVO incomeExpendByUserId(Long userId);

    /**
     * 描述: 已退款金额/已消费金额
     *
     * @param userId 用户id
     * @return com.meiyuan.catering.finance.vo.account.ConsumeRefundVO
     * @author zengzhangni
     * @date 2020/6/23 10:48
     * @since v1.1.1
     */
    ConsumeRefundVO consumeRefundByUserId(Long userId);

    /**
     * 描述:查询账户消费记录列表
     *
     * @param userId  用户id
     * @param pageDTO 查询条件
     * @return java.util.List<com.meiyuan.catering.finance.vo.account.UserAccountRecordListVO>
     * @author zengzhangni
     * @date 2020/6/23 10:47
     * @since v1.1.1
     */
    List<UserAccountRecordListVO> queryAccountRecordList(Long userId, BasePageDTO pageDTO);

    /**
     * 描述: 添加余额账户记录
     *
     * @param userId       用户id
     * @param refundAmount refundAmount
     * @param refundNumber refundNumber
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 16:59
     */
    void saveBalanceAccountRecord(Long userId, BigDecimal refundAmount, String refundNumber);


    /**
     * 描述:老系统余额添加
     *
     * @param userId 用户id
     * @param amount 余额
     * @return void
     * @author zengzhangni
     * @date 2020/4/3 10:35
     */
    void saveBalanceAccountRecord(Long userId, BigDecimal amount);

    /**
     * 描述:添加余额消费明细
     *
     * @param userId         用户id
     * @param title          标题
     * @param receivedAmount 金额
     * @param type           类型
     * @param fundType       类型
     * @param transactionId  流水
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 10:06
     * @since v1.1.0
     */
    Boolean saveBalanceAccountRecord(Long userId, String title, BigDecimal receivedAmount, Integer type, Integer fundType, String transactionId);
}
