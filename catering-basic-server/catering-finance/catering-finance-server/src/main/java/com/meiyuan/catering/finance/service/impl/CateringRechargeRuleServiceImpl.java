package com.meiyuan.catering.finance.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.pay.recharge.RechargeRule;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.finance.dao.CateringRechargeRuleMapper;
import com.meiyuan.catering.finance.dto.AccountListDTO;
import com.meiyuan.catering.finance.entity.CateringRechargeActivityEntity;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;
import com.meiyuan.catering.finance.service.CateringRechargeActivityService;
import com.meiyuan.catering.finance.service.CateringRechargeRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Service
public class CateringRechargeRuleServiceImpl extends ServiceImpl<CateringRechargeRuleMapper, CateringRechargeRuleEntity> implements CateringRechargeRuleService {

    @Resource
    private CateringRechargeActivityService activityService;

    @Override
    public List<CateringRechargeRuleEntity> queryByActivityId(Long activityId) {
        LambdaQueryWrapper<CateringRechargeRuleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringRechargeRuleEntity::getActivityId, activityId);
        wrapper.orderByDesc(CateringRechargeRuleEntity::getRechargeAccount);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<AccountListDTO> ruleList(Long id) {
        LambdaQueryWrapper<CateringRechargeRuleEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(CateringRechargeRuleEntity::getActivityId, id);
        List<CateringRechargeRuleEntity> list = list(wrapper);
        return list.stream().map(rule -> {
            AccountListDTO accountListDTO = new AccountListDTO();
            accountListDTO.setRechargeAccount(rule.getRechargeAccount());
            accountListDTO.setGivenAccount(rule.getGivenAccount());
            return accountListDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public RechargeRule getRuleById(Long ruleId, Long activityId) {
        LambdaQueryWrapper<CateringRechargeActivityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringRechargeActivityEntity::getId, activityId)
                .eq(CateringRechargeActivityEntity::getStatus, 0)
                .eq(CateringRechargeActivityEntity::getUp, true);
        CateringRechargeActivityEntity activity = activityService.getOne(wrapper);
        if (activity != null) {
            return BaseUtil.objToObj(baseMapper.selectById(ruleId), RechargeRule.class);
        }
        return null;
    }
}

