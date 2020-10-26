package com.meiyuan.catering.marketing.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.marketing.entity.CateringGroupOrderMemberEntity;
import com.meiyuan.catering.marketing.vo.groupordermember.MarketingGroupOrderMemberCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 团单成员表(CateringGroupOrderMember)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 11:24:40
 */
@Mapper
public interface CateringGroupOrderMemberMapper extends BaseMapper<CateringGroupOrderMemberEntity>{

    /**
     * 根据团单ID集合进行团购成员统计
     *
     * @param groupOrderIdList 团单ID集合
     * @author: GongJunZheng
     * @date: 2020/8/21 15:13
     * @return: {@link List<MarketingGroupOrderMemberCountVO>}
     * @version V1.3.0
     **/
    List<MarketingGroupOrderMemberCountVO> memberCount(@Param("groupOrderIdList") List<Long> groupOrderIdList);

    /**
    * 根据团单ID集合进行总团购成员统计
    * @param groupOrderIdList 团单ID集合
    * @author: GongJunZheng
    * @date: 2020/8/27 21:50
    * @return: {@link Integer}
    * @version V1.3.0
    **/
    Integer totalMemberCount(@Param("groupOrderIdList") List<Long> groupOrderIdList);
}