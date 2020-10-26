package com.meiyuan.catering.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.marketing.dto.activity.ActivityRuleDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingActivityRuleEntity;
import com.meiyuan.catering.marketing.vo.activity.ActivityRuleVO;

import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:23
 */
public interface CateringMarketingActivityRuleService extends IService<CateringMarketingActivityRuleEntity> {

    /**
     * describe: 批量新增规则
     * @author: yy
     * @date: 2020/8/8 14:56
     * @param activityId
     * @param activityRuleList
     * @param activityState
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean saveOrUpdateList(Long activityId, List<ActivityRuleDTO> activityRuleList, Integer activityState);

    /**
     * describe: 根据活动id查询规则
     * @author: yy
     * @date: 2020/8/10 16:48
     * @param activityId
     * @return: {@link List< ActivityRuleVO>}
     * @version 1.3.0
     **/
    List<ActivityRuleVO> queryListByActivityId(Long activityId);
}
