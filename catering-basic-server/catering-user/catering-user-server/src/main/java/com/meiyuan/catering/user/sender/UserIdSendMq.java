package com.meiyuan.catering.user.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.constant.UserMqConstant;
import com.meiyuan.catering.user.dto.user.UserIdDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lhm
 * @date 2020/3/26 17:55
 **/
@Slf4j
@Component
public class UserIdSendMq {
    @Resource
    AmqpTemplate rabbitTemplate;


    /**
     * 发送用户信息
     * @param dto
     */
    public void sendUserMq(UserIdDTO dto) {
        log.info("-----------------------------开始 发送新用户信息--------------------------------");
        log.debug("新用户信息UserIdDTO={}",dto);
        String sendMsg = JSON.toJSONString(dto);
        rabbitTemplate.convertAndSend(UserMqConstant.USER_ADD_FANOUT_EXCHANGE, null,
                sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 发送新用户信息--------------------------------");
    }



}


