package com.meiyuan.catering.job.mq.marketing;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.enums.base.SaleChannelsEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.ticket.TicketBasicInfoDTO;
import com.meiyuan.catering.marketing.enums.MarketingTicketIndateTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingUsingObjectEnum;
import com.meiyuan.catering.marketing.feign.MarketingTicketClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName userTicketMsgReceive
 * @Description
 * @Author gz
 * @Date 2020/3/23 17:31
 * @Version 1.1
 */
@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.TICKET_PUSH_EXPIRE_QUEUE)
public class TicketPushMsgReceive {
    @Autowired
    private MarketingTicketClient ticketClient;
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
        try{
            Map<String,Object> map = JSONObject.parseObject(msg, Map.class);
            List<Long> ids = (List<Long>)map.get("ticketIdList");
            Long activityId = Long.valueOf(map.get("activityId").toString());
            Result<List<TicketBasicInfoDTO>> info = ticketClient.selectBasicInfo(ids);
            if(info.failure() || CollectionUtils.isEmpty(info.getData())){
                throw new CustomException("没有获取到优惠券信息");
            }
            info.getData().forEach(infoData->{
                if(infoData.getBeginTime().isAfter(LocalDateTime.now()) || infoData.getEndTime().isBefore(LocalDateTime.now())){
                    log.info("优惠券的发放日期数据存在修改，不做发放处理");
                    return;
                }
                if(infoData.getUseChannels().equals(SaleChannelsEnum.TS.getStatus())){
                    return;
                }
                Integer quantity = infoData.getPublishQuantity();
                // 库存不足
                if(quantity==0){
                    return;
                }
                Integer objectLimit = MarketingUsingObjectEnum.ALL.getStatus();
                if( MarketingUsingObjectEnum.ENTERPRISE.getStatus().equals(infoData.getObjectLimit())){
                    // 企业
                    objectLimit = MarketingUsingObjectEnum.PERSONAL.getStatus();
                }else if(MarketingUsingObjectEnum.PERSONAL.getStatus().equals(infoData.getObjectLimit())){
                    // 个人
                    objectLimit = MarketingUsingObjectEnum.ENTERPRISE.getStatus();
                }
                LocalDateTime useEndTime = infoData.getUseEndTime();
                if(MarketingTicketIndateTypeEnum.DAYS.getStatus().equals(infoData.getIndateType())){
                    useEndTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23,59,59)).plusDays(infoData.getUseDays());
                }
                // 发送优惠券操作
                userTicketClient.pushTicketToUser(infoData.getId(),infoData.getTicketName(),quantity,objectLimit,useEndTime,activityId);
            });
        }catch (Exception e){
            log.error("优惠券发放延迟任务处理异常:msg={},error={}",msg,e);
        }
    }
}
