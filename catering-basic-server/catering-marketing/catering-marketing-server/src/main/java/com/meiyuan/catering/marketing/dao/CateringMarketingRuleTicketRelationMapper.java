package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingRuleTicketRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:34
 */
@Mapper
public interface CateringMarketingRuleTicketRelationMapper extends BaseMapper<CateringMarketingRuleTicketRelationEntity> {
    /**
     * describe: 根据活动id删除关联优惠券
     * @author: yy
     * @date: 2020/8/10 17:30
     * @param activityId
     * @return: {@link Boolean}
     * @version 1.3.0
     **/
    Boolean deleteByActivityId(@Param("activityId") Long activityId);
}
