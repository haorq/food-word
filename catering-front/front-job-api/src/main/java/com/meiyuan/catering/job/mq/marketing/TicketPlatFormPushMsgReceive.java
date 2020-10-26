package com.meiyuan.catering.job.mq.marketing;

import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.ticket.PushTicketToUserDTO;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName userTicketMsgReceive
 * @Description
 * @Author gz
 * @Date 2020/3/23 17:31
 * @Version 1.1
 */
@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.TICKET_PLATFORM_PUSH_EXPIRE_QUEUE)
public class TicketPlatFormPushMsgReceive {
    @Autowired
    private MarketingActivityClient marketingActivityClient;
    @Autowired
    private UserTicketClient userTicketClient;
    @RabbitHandler
    public void ticketReceive(byte[] recived) {
        this.ticketPushMsg(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     *
     * @param msg 消息-优惠券id
     */
    @SuppressWarnings("all")
    @RabbitHandler
    public void ticketPushMsg(String msg) {
        log.debug("接收到平台发券消息msg={}",msg);
        try{
            Long activityId = Long.valueOf(msg);
            Result<List<PushTicketToUserDTO>> result = marketingActivityClient.listTicketPushMsgForActivityId(activityId);
            log.debug("活动结果集result={}",result);
            if(result.failure() || CollectionUtils.isEmpty(result.getData())){
                log.error("发送平台优惠券异常,没有获取到平台优惠券信息");
            }
            List<PushTicketToUserDTO> data = result.getData();
            PushTicketToUserDTO ticket = data.stream().findFirst().get();
            if(ticket.getBeginTime().isAfter(LocalDateTime.now()) || ticket.getEndTime().isBefore(LocalDateTime.now())){
                log.info("优惠券的发放日期数据存在修改，不做发放处理");
                return;
            }
            if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(ticket.getUpDownStatus())){
                log.info("当前活动已冻结,不做发券处理");
                return;
            }
            userTicketClient.pushPlatFormTicketToUser(data);
        }catch (Exception e){
            log.error("优惠券发放延迟任务处理异常:msg={},error={}",msg,e);
        }
    }
}
