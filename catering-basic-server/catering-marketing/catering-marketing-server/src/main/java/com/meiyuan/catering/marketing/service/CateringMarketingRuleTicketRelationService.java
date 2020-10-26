package com.meiyuan.catering.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.marketing.dto.activity.RuleTicketRelationDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingRuleTicketRelationEntity;

import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:35
 */
public interface CateringMarketingRuleTicketRelationService extends IService<CateringMarketingRuleTicketRelationEntity> {

    /**
     * describe: 根据活动id删除关联优惠券
     * @author: yy
     * @date: 2020/8/10 17:29
     * @param activityId
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean deleteByActivityId(Long activityId);

    /**
     * describe: 根据id修改优惠券数量
     * @author: yy
     * @date: 2020/8/17 10:46
     * @param ruleTicketRelationList
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean updateListById(List<RuleTicketRelationDTO> ruleTicketRelationList);
}
