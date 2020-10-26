package com.meiyuan.catering.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.pay.ChargeOrder;
import com.meiyuan.catering.finance.entity.CateringUserChargeRecordEntity;
import com.meiyuan.catering.finance.query.recharge.RechargeRecordQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.RechargeRecordListVO;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
public interface CateringUserChargeRecordService extends IService<CateringUserChargeRecordEntity> {

    /**
     * zzn
     *
     * @param dto     查询条件
     * @param userIds 用户ids集合
     * @return
     */
    IPage<RechargeRecordListVO> pageList(RechargeRecordQueryDTO dto, List<Long> userIds);

    /**
     * 描述:添加充值明细
     *
     * @param order
     * @param rechargeNo
     * @param transactionId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/20 9:30
     * @since v1.1.0
     */
    Boolean saveChargeRecord(ChargeOrder order, String rechargeNo, String transactionId);
}
