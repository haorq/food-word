package com.meiyuan.catering.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.pay.recharge.RechargeRule;
import com.meiyuan.catering.finance.dto.AccountListDTO;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
public interface CateringRechargeRuleService extends IService<CateringRechargeRuleEntity> {

    /**
     * 描述:查询活动对应的充值金额
     *
     * @param activityId 活动表id
     * @return java.util.List<com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity>
     * @author zengzhangni
     * @date 2020/6/23 10:46
     * @since v1.1.1
     */
    List<CateringRechargeRuleEntity> queryByActivityId(Long activityId);

    /**
     * 描述:充值活动规则列表
     *
     * @param id
     * @return java.util.List<com.meiyuan.catering.finance.dto.AccountListDTO>
     * @author zengzhangni
     * @date 2020/5/19 16:09
     * @since v1.1.0
     */
    List<AccountListDTO> ruleList(Long id);

    /**
     * 描述:查询充值规则
     *
     * @param ruleId     规则id
     * @param activityId 活动id
     * @return com.meiyuan.catering.core.dto.pay.recharge.RechargeRule
     * @author zengzhangni
     * @date 2020/5/20 9:33
     * @since v1.1.0
     */
    RechargeRule getRuleById(Long ruleId, Long activityId);
}
