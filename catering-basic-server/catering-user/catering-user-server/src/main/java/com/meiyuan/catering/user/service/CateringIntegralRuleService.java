package com.meiyuan.catering.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.user.entity.CateringIntegralRuleEntity;

import java.util.List;

/**
 * 积分规则表(CateringIntegralRule)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-09 18:41:20
 */
public interface CateringIntegralRuleService extends IService<CateringIntegralRuleEntity> {

    /**
     * 描述: 所有规则(不过滤)
     *
     * @param
     * @return java.util.List<com.meiyuan.catering.user.entity.CateringIntegralRuleEntity>
     * @author zengzhangni
     * @date 2020/6/23 10:42
     * @since v1.1.1
     */
    List<CateringIntegralRuleEntity> listAll();

    /**
     * 描述:所有规则(过滤)
     *
     * @param
     * @return java.util.List<com.meiyuan.catering.user.entity.CateringIntegralRuleEntity>
     * @author zengzhangni
     * @date 2020/6/23 10:42
     * @since v1.1.1
     */
    List<CateringIntegralRuleEntity> all();
}
