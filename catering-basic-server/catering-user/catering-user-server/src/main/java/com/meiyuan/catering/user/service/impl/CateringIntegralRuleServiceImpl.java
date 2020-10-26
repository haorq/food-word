package com.meiyuan.catering.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.user.dao.CateringIntegralRuleMapper;
import com.meiyuan.catering.user.entity.CateringIntegralRuleEntity;
import com.meiyuan.catering.user.service.CateringIntegralActivityService;
import com.meiyuan.catering.user.service.CateringIntegralRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 积分规则表(CateringIntegralRule)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-09 18:42:07
 */
@Service("cateringIntegralRuleService")
public class CateringIntegralRuleServiceImpl extends ServiceImpl<CateringIntegralRuleMapper, CateringIntegralRuleEntity> implements CateringIntegralRuleService {
    @Resource
    private CateringIntegralActivityService activityService;

    @Override
    public List<CateringIntegralRuleEntity> listAll() {
        return baseMapper.listAll();
    }

    @Override
    public List<CateringIntegralRuleEntity> all() {
        //获取所有已存在的积分活动的规则编码
        List<String> activityList = activityService.queryNotDeleteList();
        List<CateringIntegralRuleEntity> list;
        if (activityList != null && activityList.size() > 0) {
            LambdaQueryWrapper<CateringIntegralRuleEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.notIn(CateringIntegralRuleEntity::getIntegralNo, activityList);
            list = list(wrapper);
        } else {
            list = list();
        }
        return list;
    }
}
