package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.dto.user.PusherTicketDTO;
import com.meiyuan.catering.user.entity.CateringUserPusherTicketRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lhm
 * @version 1.0 2020年5月6日
 */
@Repository
@Mapper
public interface CateringUserPusherTicketRelationMapper extends BaseMapper<CateringUserPusherTicketRelationEntity> {

    /**
     * 查询地推员优惠券
     * @param groundPusherId
     * @return
     */
    List<PusherTicketDTO> listPusherTicket(Long groundPusherId);
}
