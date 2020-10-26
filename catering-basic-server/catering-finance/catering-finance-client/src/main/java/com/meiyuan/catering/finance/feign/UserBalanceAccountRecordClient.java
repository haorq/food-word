package com.meiyuan.catering.finance.feign;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.service.CateringUserBalanceAccountRecordService;
import com.meiyuan.catering.finance.vo.account.ConsumeRefundVO;
import com.meiyuan.catering.finance.vo.account.IncomeExpendVO;
import com.meiyuan.catering.finance.vo.account.UserAccountRecordListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/19 9:38
 * @since v1.1.0
 */
@Service
public class UserBalanceAccountRecordClient {

    @Resource
    private CateringUserBalanceAccountRecordService service;

    /**
     * 描述:总收入/总支出
     *
     * @param userId
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.finance.vo.account.IncomeExpendVO>
     * @author zengzhangni
     * @date 2020/5/19 10:16
     * @since v1.1.0
     */
    public Result<IncomeExpendVO> incomeExpendByUserId(Long userId) {
        return Result.succ(service.incomeExpendByUserId(userId));

    }

    /**
     * 描述:已退款金额/已消费金额
     *
     * @param userId
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.finance.vo.account.ConsumeRefundVO>
     * @author zengzhangni
     * @date 2020/5/19 10:16
     * @since v1.1.0
     */
    public Result<ConsumeRefundVO> consumeRefundByUserId(Long userId) {
        return Result.succ(service.consumeRefundByUserId(userId));

    }

    /**
     * 描述:查询账户消费记录列表
     *
     * @param userId
     * @param pageDTO
     * @return com.meiyuan.catering.core.util.Result<java.util.List                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               com.meiyuan.catering.finance.vo.account.UserAccountRecordListVO>>
     * @author zengzhangni
     * @date 2020/5/19 10:16
     * @since v1.1.0
     */
    public Result<List<UserAccountRecordListVO>> queryAccountRecordList(Long userId, BasePageDTO pageDTO) {

        return Result.succ(service.queryAccountRecordList(userId, pageDTO));
    }

    /**
     * 描述:添加余额账户记录
     *
     * @param userId
     * @param refundAmount
     * @param refundNumber
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 10:16
     * @since v1.1.0
     */
    public Result saveBalanceAccountRecord(Long userId, BigDecimal refundAmount, String refundNumber) {
        service.saveBalanceAccountRecord(userId, refundAmount, refundNumber);
        return Result.succ();
    }


    /**
     * 描述:老系统余额添加
     *
     * @param userId
     * @param amount
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 10:16
     * @since v1.1.0
     */
    public Result saveBalanceAccountRecord(Long userId, BigDecimal amount) {
        service.saveBalanceAccountRecord(userId, amount);
        return Result.succ();
    }

    /**
     * 描述:添加余额消费明细
     *
     * @param userId
     * @param title
     * @param receivedAmount
     * @param type
     * @param fundType
     * @param transactionId
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/20 10:07
     * @since v1.1.0
     */
    public Result<Boolean> saveBalanceAccountRecord(Long userId, String title, BigDecimal receivedAmount, Integer type, Integer fundType, String transactionId) {
        return Result.succ(service.saveBalanceAccountRecord(userId, title, receivedAmount, type, fundType, transactionId));
    }

}
