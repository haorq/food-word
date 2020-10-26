package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingActivityRuleEntity;
import com.meiyuan.catering.marketing.vo.activity.ActivityRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:25
 */
@Mapper
public interface CateringMarketingActivityRuleMapper extends BaseMapper<CateringMarketingActivityRuleEntity> {

    /**
     * describe: 根据活动id查询规则
     * @author: yy
     * @date: 2020/8/10 16:52
     * @param activityId
     * @return: {@link List< ActivityRuleVO>}
     * @version 1.3.0
     **/
    List<ActivityRuleVO> queryListByActivityId(@Param("activityId") Long activityId);
}
