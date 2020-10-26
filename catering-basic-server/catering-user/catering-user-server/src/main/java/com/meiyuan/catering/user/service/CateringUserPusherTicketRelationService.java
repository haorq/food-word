package com.meiyuan.catering.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.user.dto.user.PusherTicketDTO;
import com.meiyuan.catering.user.entity.CateringUserPusherTicketRelationEntity;

import java.util.List;

/**
 * @author lhm
 * @version 1.0 2020年5月6日
 */
public interface CateringUserPusherTicketRelationService extends IService<CateringUserPusherTicketRelationEntity> {
    /**
     * 描述：通过地推员id获取优惠券IDS
     * @author lhm
     * @date 2020/6/23
     * @param groundPusherId
     * @return {@link List< Long>}
     * @version 1.1.1
     **/
    List<Long> listPusherTicketId(Long groundPusherId);


    /**
     * 描述：地推员扫码优惠券列表
     * @author lhm
     * @date 2020/6/23
     * @param groundPusherId
     * @return {@link List< PusherTicketDTO>}
     * @version 1.1.1
     **/
    List<PusherTicketDTO> listPusherTicket(Long groundPusherId);
}
