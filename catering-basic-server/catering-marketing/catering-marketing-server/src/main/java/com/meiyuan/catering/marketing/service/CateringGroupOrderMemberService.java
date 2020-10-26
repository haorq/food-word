package com.meiyuan.catering.marketing.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.marketing.dto.groupon.GroupOrderMemberDTO;
import com.meiyuan.catering.marketing.entity.CateringGroupOrderMemberEntity;
import com.meiyuan.catering.marketing.vo.groupordermember.MarketingGroupOrderMemberCountVO;

import java.util.List;

/**
 * 团单成员表(CateringGroupOrderMember)服务层
 *
 * @author wxf
 * @since 2020-03-10 11:30:11
 */
public interface CateringGroupOrderMemberService extends IService<CateringGroupOrderMemberEntity> {

    /**
     * 添加团单成员
     *
     * @param memberDTO
     */
    void addMember(GroupOrderMemberDTO memberDTO);

    /**
     * 根据订单编号删除团单成员
     *
     * @param orderNumber
     */
    void removeByOrderNumber(String orderNumber);

    /**
    * 根据团单ID集合进行团购成员统计
     *
    * @param groupOrderIdList 团单ID集合
    * @author: GongJunZheng
    * @date: 2020/8/21 15:13
    * @return: {@link List<MarketingGroupOrderMemberCountVO>}
    * @version V1.3.0
    **/
    List<MarketingGroupOrderMemberCountVO> memberCount(List<Long> groupOrderIdList);

    /**
    * 根据团单ID集合进行总团购成员统计
    * @param groupOrderIdList 团单ID集合
    * @author: GongJunZheng
    * @date: 2020/8/27 21:49
    * @return: {@link Integer}
    * @version V1.3.0
    **/
    Integer totalMemberCount(List<Long> groupOrderIdList);

}