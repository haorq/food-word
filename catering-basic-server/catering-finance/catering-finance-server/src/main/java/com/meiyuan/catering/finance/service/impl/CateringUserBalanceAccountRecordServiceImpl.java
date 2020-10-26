package com.meiyuan.catering.finance.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.constant.AccountRecordConstant;
import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.finance.dao.CateringUserBalanceAccountRecordMapper;
import com.meiyuan.catering.finance.entity.CateringUserBalanceAccountRecordEntity;
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
 * @date 2020-03-16
 */
@Service
public class CateringUserBalanceAccountRecordServiceImpl extends ServiceImpl<CateringUserBalanceAccountRecordMapper, CateringUserBalanceAccountRecordEntity> implements CateringUserBalanceAccountRecordService {
    @Resource
    private CateringUserBalanceAccountRecordMapper cateringUserBalanceAccountRecordMapper;


    @Override
    public IncomeExpendVO incomeExpendByUserId(Long userId) {
        return baseMapper.incomeExpendByUserId(userId);
    }

    @Override
    public ConsumeRefundVO consumeRefundByUserId(Long userId) {
        return baseMapper.consumeRefundByUserId(userId);
    }

    @Override
    public List<UserAccountRecordListVO> queryAccountRecordList(Long userId, BasePageDTO pageDTO) {
        return baseMapper.queryAccountRecordList(pageDTO.getPage(), userId);
    }

    @Override
    public void saveBalanceAccountRecord(Long userId, BigDecimal refundAmount, String refundNumber) {
        CateringUserBalanceAccountRecordEntity recordEntity = new CateringUserBalanceAccountRecordEntity();
        recordEntity.setUserId(userId);
        recordEntity.setTitle(AccountRecordConstant.Title.ORDER_REFUND);
        recordEntity.setAccount(refundAmount);
        recordEntity.setType(AccountRecordConstant.Type.INCOME);
        recordEntity.setFundType(AccountRecordConstant.FundType.ORDER_REFUND);
        recordEntity.setFundNo(refundNumber);
        recordEntity.setStatus(AccountRecordConstant.Status.VALID);
        baseMapper.insert(recordEntity);
    }

    @Override
    public void saveBalanceAccountRecord(Long userId, BigDecimal amount) {
        CateringUserBalanceAccountRecordEntity recordEntity = new CateringUserBalanceAccountRecordEntity();
        recordEntity.setUserId(userId);
        recordEntity.setTitle(AccountRecordConstant.Title.SYSTEM_BALANCE);
        recordEntity.setAccount(amount);
        recordEntity.setType(AccountRecordConstant.Type.INCOME);
        recordEntity.setFundType(AccountRecordConstant.FundType.SYSTEM_BALANCE);
        recordEntity.setFundNo("");
        recordEntity.setStatus(AccountRecordConstant.Status.VALID);
        baseMapper.insert(recordEntity);
    }

    @Override
    public Boolean saveBalanceAccountRecord(Long userId, String title, BigDecimal receivedAmount, Integer type, Integer fundType, String transactionId) {
        CateringUserBalanceAccountRecordEntity accountRecordEntity = new CateringUserBalanceAccountRecordEntity();
        accountRecordEntity.setUserId(userId);
        accountRecordEntity.setTitle(title);
        accountRecordEntity.setAccount(receivedAmount);
        accountRecordEntity.setType(type);
        accountRecordEntity.setFundType(fundType);
        accountRecordEntity.setFundNo(transactionId);
        return save(accountRecordEntity);
    }
}

