package com.meiyuan.catering.finance.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.dto.pay.ChargeOrder;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.query.recharge.RechargeRecordQueryDTO;
import com.meiyuan.catering.finance.service.CateringUserChargeRecordService;
import com.meiyuan.catering.finance.vo.recharge.RechargeRecordListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/19 9:38
 * @since v1.1.0
 */
@Service
public class UserChargeRecordClient {
    @Resource
    private CateringUserChargeRecordService service;

    /**
     * 描述:分页查询
     *
     * @param dto
     * @param userIds
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 10:19
     * @since v1.1.0
     */
    public Result<IPage<RechargeRecordListVO>> pageList(RechargeRecordQueryDTO dto, List<Long> userIds) {
        return Result.succ(service.pageList(dto, userIds));
    }

    /**
     * 描述:添加充值明细
     *
     * @param order
     * @param rechargeNo
     * @param transactionId
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/20 9:29
     * @since v1.1.0
     */
    public Result<Boolean> saveChargeRecord(ChargeOrder order, String rechargeNo, String transactionId) {
        return Result.succ(service.saveChargeRecord(order, rechargeNo, transactionId));
    }
}
