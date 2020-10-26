package com.meiyuan.catering.wx.mq.receive;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.constant.UserMqConstant;
import com.meiyuan.catering.core.dto.base.UserCompanyInfo;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.activity.ActivityH5RecordDTO;
import com.meiyuan.catering.marketing.dto.ticket.TicketSelectListDTO;
import com.meiyuan.catering.marketing.enums.MarketingUsingObjectEnum;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.marketing.feign.MarketingTicketClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.fegin.user.UserClient;
import com.meiyuan.catering.user.fegin.user.UserPusherTicketRelationClient;
import com.meiyuan.catering.wx.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName UserRegisterSuccessMsgReceive
 * @Description 新注册用户发放优惠券消息监听
 * @Author gz
 * @Date 2020/3/27 13:41
 * @Version 1.1
 */
@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.TICKET_USER_REGISTER_SUCCESS)
public class WxUserRegisterSuccessTicketMsgReceive {
    @Autowired
    private MarketingTicketClient ticketClient;
    @Autowired
    private UserTicketClient userTicketClient;
    @Autowired
    private UserPusherTicketRelationClient userPusherTicketRelationClient;
    @Autowired
    private WechatUtils wechatUtils;
    @Autowired
    private UserClient userClient;
    @Autowired
    private MarketingActivityClient activityClient;

    @RabbitHandler
    public void ticketReceive(byte[] recived) {
        this.ticketPushMsg(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     * 新注册用户发放优惠券
     *
     * @param msg
     */
    @SuppressWarnings("all")
    @RabbitHandler
    public void ticketPushMsg(String msg) {
        log.info("接收到新用户注册消息msg={}", msg);
        try {
            JSONObject object = JSONObject.parseObject(msg);
            // 新用户id
            Long userId = object.getLong("userId");
            // 地推员id
            Long groundPusherId = object.getLong("groundPusherId");
            if (userId != null) {
                // 发放地推员专属优惠券
                if(groundPusherId!=null){
                    pushGroundPusherTicket(groundPusherId,userId,false);
                }
                // 发放平台【发券宝活动】优惠券
                String phone = object.getString("phone");
                if(StringUtils.isNotBlank(phone)){
                    Result<List<ActivityH5RecordDTO>> result = activityClient.listH5Record(phone);
                    if(result.success()&&CollectionUtils.isNotEmpty(result.getData())){
                        userTicketClient.userH5TicketUpdate(userId,result.getData().stream().map(ActivityH5RecordDTO::getId).collect(Collectors.toList()));
                    }
                }
            }else {
                // 老用户id 老用户扫码
                Long groundUserId = object.getLong("groundUserId");
                log.debug("老用户领取优惠券,老用户id={}",groundUserId);
                pushGroundPusherTicket(groundPusherId,groundUserId,true);
            }
        } catch (Exception e) {
            log.error("新注册用户发放优惠券异常:error={}", e);
        }
    }

    @SuppressWarnings("all")
    private void pushTicket(Long userId, Integer userType, Integer objectLimit, boolean hasReferrer) {
        Result<List<TicketSelectListDTO>> referrerResult = ticketClient.selectListForRegister(hasReferrer ? userId : null, objectLimit);
        if (referrerResult.success() && CollectionUtils.isNotEmpty(referrerResult.getData())) {
            List<TicketSelectListDTO> list = referrerResult.getData();
            List<Long> collect = list.stream().map(TicketSelectListDTO::getId).collect(Collectors.toList());
            userTicketClient.insertTicketBatch(collect, userId, userType,false);
        }
    }

    @SuppressWarnings("all")
    private Integer handlerUserType(Integer userType) {
        Integer objectLimit = MarketingUsingObjectEnum.ALL.getStatus();
        if (UserTypeEnum.PERSONAL.getStatus().equals(userType)) {
            objectLimit = MarketingUsingObjectEnum.PERSONAL.getStatus();
        } else if (UserTypeEnum.COMPANY.getStatus().equals(userType)) {
            objectLimit = MarketingUsingObjectEnum.ENTERPRISE.getStatus();
        }
        return objectLimit;
    }

    /**
     * 发放地推员专属优惠券
     * @param groundPusherId 地推员优惠券
     * @param userId 用户id
     * @param oldUser 老用户标识
     */
    private void pushGroundPusherTicket(Long groundPusherId,Long userId,boolean oldUser){
        Result<List<Long>> listResult = userPusherTicketRelationClient.listPusherTicketId(groundPusherId);
        if(listResult.failure() || CollectionUtils.isEmpty(listResult.getData())){
            return;
        }
        Result result = userTicketClient.insertTicketBatch(listResult.getData(), userId, UserTypeEnum.PERSONAL.getStatus(), oldUser);
        log.debug("pushGroundPusherTicket==>result={}",result);
        // 不存在可以领取的优惠券进行提示
        if(result.getCode()== ErrorCode.GROUND_PUSHER_NO_TICKET){
            wechatUtils.setPusherMsg(groundPusherId,userId,result.getMsg());
            log.debug("老用户提示信息放入缓存成功");
        }
    }

}
