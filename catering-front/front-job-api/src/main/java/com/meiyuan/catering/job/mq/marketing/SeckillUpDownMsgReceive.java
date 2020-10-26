package com.meiyuan.catering.job.mq.marketing;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillUpDownDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEntity;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingSeckillClient;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @ClassName SeckillUpDownMsgReceive
 * @Description 秒杀定时任务监听
 * @Author gz
 * @Date 2020/3/30 15:30
 * @Version 1.1
 */
@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.MARKETING_SECKILL_EXPIRE_QUEUE)
public class SeckillUpDownMsgReceive {
    
    @Autowired
    private MarketingSeckillClient seckillClient;

    @RabbitHandler
    public void seckillReceive(byte[] recived) {
        this.seckillDownMsg(new String(recived, StandardCharsets.UTF_8));
    }


    /**
     *
     * @param msg 消息-秒杀id
     */
    @SuppressWarnings("all")
    @RabbitHandler
    public void seckillDownMsg(String msg) {
        try{
            log.info("接收到秒杀定时上/下架消息msg={}",msg);
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Long id = jsonObject.getLong("id");
            Integer status = jsonObject.getInteger("upDownStatus");
            Result<MarketingSeckillDTO> one = seckillClient.findOne(id);
            if(one.failure() || one.getData() == null){
                return;
            }
            MarketingSeckillDTO data = one.getData();
            LocalDateTime now = LocalDateTime.now();
            MarketingUpDownStatusEnum statusEnum = MarketingUpDownStatusEnum.parse(status);
            switch (statusEnum){
                case UP:
                    // 上架需要判断开始时间
                    if(data.getBeginTime().isAfter(now)){
                        return;
                    }
                    break;
                case DOWN:
                    // 下架需要判断结束时间
                    if(data.getEndTime().isAfter(now)){
                        return;
                    }
                    break;
                    default:break;
            }
            MarketingSeckillUpDownDTO downDTO = new MarketingSeckillUpDownDTO();
            downDTO.setId(id);
            downDTO.setUpDownState(status);
            seckillClient.updateUpDownStatus(downDTO);
            log.info("秒杀定时上/下架消息执行成功msg={}",msg);
        }catch (Exception e){
            log.error("秒杀活动延迟任务异常.",e);
        }

    }
}
