package com.meiyuan.catering.finance.feign;

import com.meiyuan.catering.core.dto.pay.recharge.RechargeRule;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.dto.AccountListDTO;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;
import com.meiyuan.catering.finance.service.CateringRechargeRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/5/19 9:38
 * @since v1.1.0
 */
@Service
public class RechargeRuleClient {


    @Resource
    private CateringRechargeRuleService service;

    /**
     * 描述:查询活动对应的充值金额
     *
     * @param activityId
     * @return com.meiyuan.catering.core.util.Result
     * @author zengzhangni
     * @date 2020/5/19 10:02
     * @since v1.1.0
     */
    public Result<List<CateringRechargeRuleEntity>> queryByActivityId(Long activityId) {
        return Result.succ(service.queryByActivityId(activityId));

    }

    /**
     * 描述:充值规则列表
     *
     * @param id
     * @return com.meiyuan.catering.core.util.Result<java.util.List                                                               <                                                               com.meiyuan.catering.finance.dto.AccountListDTO>>
     * @author zengzhangni
     * @date 2020/5/19 16:08
     * @since v1.1.0
     */
    public Result<List<AccountListDTO>> ruleList(Long id) {
        return Result.succ(service.ruleList(id));
    }

    /**
     * 描述:查询充值规则
     *
     * @param ruleId
     * @return com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.dto.pay.recharge.RechargeRule>
     * @author zengzhangni
     * @date 2020/5/20 9:33
     * @since v1.1.0
     */
    public Result<RechargeRule> getRuleById(Long ruleId, Long activityId) {
        return Result.succ(service.getRuleById(ruleId, activityId));
    }
}
