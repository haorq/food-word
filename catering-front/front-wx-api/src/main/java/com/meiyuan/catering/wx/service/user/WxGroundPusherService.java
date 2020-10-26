package com.meiyuan.catering.wx.service.user;

import com.google.common.collect.Lists;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.user.PusherTicketDTO;
import com.meiyuan.catering.user.fegin.user.UserPusherTicketRelationClient;
import com.meiyuan.catering.user.service.CateringUserPusherTicketRelationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName WxGroundPusherService
 * @Description
 * @Author gz
 * @Date 2020/5/7 11:39
 * @Version 1.1
 */
@Service
public class WxGroundPusherService {
    @Autowired
    private UserPusherTicketRelationClient userPusherTicketRelationClient;

    /**
     * 地推员扫码优惠券列表
     * @param groundPusherId
     * @return
     */
    public Result<List<PusherTicketDTO>> listPusherTicket(Long groundPusherId){
        List<PusherTicketDTO> dtoList = userPusherTicketRelationClient.listPusherTicket(groundPusherId).getData();
        if(CollectionUtils.isEmpty(dtoList)){
            return Result.succ(Lists.newArrayList());
        }
        return Result.succ(dtoList);
    }
}
