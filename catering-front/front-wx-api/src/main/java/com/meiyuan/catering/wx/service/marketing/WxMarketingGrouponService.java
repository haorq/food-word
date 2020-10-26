package com.meiyuan.catering.wx.service.marketing;

import com.meiyuan.catering.core.dto.base.PayActivityDTO;
import com.meiyuan.catering.core.dto.base.PayGoodsDTO;
import com.meiyuan.catering.core.dto.base.PaySuccessNotifyDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.groupon.GroupMemberJoinInDTO;
import com.meiyuan.catering.marketing.dto.groupon.GroupOrderDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGroupOrderEntity;
import com.meiyuan.catering.marketing.feign.MarketingGroupOrderClient;
import com.meiyuan.catering.marketing.service.CateringMarketingGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luohuan
 * @date 2020/4/1
 **/
@Slf4j
@Service
public class WxMarketingGrouponService {
    @Autowired
    private MarketingGroupOrderClient groupOrderClient;

    @Autowired
    private CateringMarketingGoodsService marketingGoodsService;

    /**
     * 新订单支付成功后，处理团单和团单成员信息
     *
     * @param notifyDTO
     */
    public void handleNewOrder(PaySuccessNotifyDTO notifyDTO) {
        if (notifyDTO.getList() == null || notifyDTO.getList().isEmpty()) {
            throw new CustomException("无商品信息");
        }
        PayGoodsDTO payGoodsDTO = notifyDTO.getList().get(0);
        CateringMarketingGroupOrderEntity groupOrderEntity = null;
        Result<CateringMarketingGroupOrderEntity> groupOrderResult = groupOrderClient.getBymGoodsId(payGoodsDTO.getMGoodsId());
        if (groupOrderResult != null && groupOrderResult.success()) {
            groupOrderEntity = groupOrderResult.getData();
        }
        if (groupOrderEntity == null) {
            //无团单数据，说明是第一个团购订单，需要初始化团单信息
            GroupOrderDTO groupOrderDTO = this.getGroupOrderDTO(notifyDTO);
            groupOrderClient.initGroupOrder(groupOrderDTO);
        } else {
            //有团单数据，处理团单成员
            GroupMemberJoinInDTO groupMemberJoinInDTO = this.getGroupMemberJoinInDTO(notifyDTO);
            groupOrderClient.newMemberJoinIn(groupMemberJoinInDTO);
        }
    }

    /**
     * PaySuccessNotifyDTO转化为GroupOrderDTO
     *
     * @param notifyDTO
     * @return
     */
    private GroupOrderDTO getGroupOrderDTO(PaySuccessNotifyDTO notifyDTO) {
        PayGoodsDTO payGoodsDTO = notifyDTO.getList().get(0);
        PayActivityDTO activityDTO = notifyDTO.getActivityList().get(0);
        GroupOrderDTO orderDTO = new GroupOrderDTO();
        orderDTO.setOfId(activityDTO.getOfId());
        orderDTO.setOfType(activityDTO.getOfType());
        orderDTO.setMGoodsId(payGoodsDTO.getMGoodsId());
        CateringMarketingGoodsEntity goodsEntity = marketingGoodsService.getById(payGoodsDTO.getMGoodsId());
        orderDTO.setMinGrouponQuantity(goodsEntity.getMinGrouponQuantity());
        orderDTO.setGoodsNumber(payGoodsDTO.getQuantity());
        orderDTO.setUserId(notifyDTO.getUserId());
        orderDTO.setUserName(notifyDTO.getUserName());
        orderDTO.setUserNickName(notifyDTO.getUserNickName());
        orderDTO.setOrderNumber(notifyDTO.getOrderNumber());
        return orderDTO;
    }

    /**
     * PaySuccessNotifyDTO转化为GroupMemberJoinInDTO
     *
     * @param notifyDTO
     * @return
     */
    private GroupMemberJoinInDTO getGroupMemberJoinInDTO(PaySuccessNotifyDTO notifyDTO) {
        PayGoodsDTO payGoodsDTO = notifyDTO.getList().get(0);
        GroupMemberJoinInDTO joinInDTO = new GroupMemberJoinInDTO();
        joinInDTO.setMGoodsId(payGoodsDTO.getMGoodsId());
        joinInDTO.setGoodsNumber(payGoodsDTO.getQuantity());
        joinInDTO.setUserId(notifyDTO.getUserId());
        joinInDTO.setUserName(notifyDTO.getUserName());
        joinInDTO.setUserNickName(notifyDTO.getUserNickName());
        joinInDTO.setOrderNumber(notifyDTO.getOrderNumber());
        return joinInDTO;
    }
}
