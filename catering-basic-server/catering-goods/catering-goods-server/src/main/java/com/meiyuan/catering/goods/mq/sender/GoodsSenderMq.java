package com.meiyuan.catering.goods.mq.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.GoodsMqConstant;
import com.meiyuan.catering.core.constant.RabbitMqConstant;
import com.meiyuan.catering.core.dto.goods.GoodsExtToEsDTO;
import com.meiyuan.catering.goods.dto.es.CategoryLabelDelToEsDTO;
import com.meiyuan.catering.goods.dto.es.GoodsEsDeteleDTO;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.es.GoodsMerchantMenuGoodsDTO;
import com.meiyuan.catering.goods.dto.mq.MerchantGoodsUpDownFanoutDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/23 14:50
 * @description 发送消息
 **/
@Slf4j
@Component
public class GoodsSenderMq {
    @Resource
    AmqpTemplate rabbitTemplate;
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 发送修改商品信息的广播
     *
     * @author: wxf
     * @date: 2020/3/23 14:57
     * @param dto 发送的数据
     **/
    public void goodsAddUpdateFanout(GoodsEsGoodsDTO dto) {
        log.info("-----------------------------开始 发送商品新增修改信息的广播--------------------------------");
        String sendMsg = JSON.toJSONString(dto);
        rabbitTemplate.convertAndSend(GoodsMqConstant.GOODS_ADD_UPDATE_FANOUT_EXCHANGE, null, sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 发送商品新增修改信息的广播--------------------------------");
    }

    /**
     *  商品/菜单 推送
     *
     * @author: wxf
     * @date: 2020/3/23 14:57
     * @param dtoList 发送的数据
     **/
    public void goodsMenuPush(List<GoodsMerchantMenuGoodsDTO> dtoList) {
        log.info("-----------------------------开始 发送商品/菜单推送ES信息--------------------------------");
        String sendMsg = JSON.toJSONString(dtoList);
        rabbitTemplate.convertAndSend(GoodsMqConstant.GOODS_EXCHANGE, GoodsMqConstant.GOODS_MENU_PUSH_QUEUE_NAME,
                sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 送商品/菜单推送ES信息--------------------------------");
    }

    /**
     *  分类/标签删除同步ES
     *
     * @author: wxf
     * @date: 2020/3/23 14:57
     * @param dto 发送的数据
     **/
    public void categoryLabelDel(CategoryLabelDelToEsDTO dto) {
        log.info("-----------------------------开始 发送分类/标签删除信息--------------------------------");
        String sendMsg = JSON.toJSONString(dto);
        rabbitTemplate.convertAndSend(GoodsMqConstant.GOODS_EXCHANGE, GoodsMqConstant.CATEGORY_LABEL_DEL_QUEUE_NAME,
                sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 发送分类/标签删除信息--------------------------------");
    }

    /**
     * 发送商户商品上下架广播信息
     *
     * @author: wxf
     * @date: 2020/3/23 14:57
     * @param dto 发送的数据
     **/
    public void merchantGoodsUpDownFanout(MerchantGoodsUpDownFanoutDTO dto) {
        log.info("-----------------------------开始 发送商户商品上下架广播信息--------------------------------");
        String sendMsg = JSON.toJSONString(dto);
        rabbitTemplate.convertAndSend(GoodsMqConstant.MERCHANT_GOODS_UP_DOWN_FANOUT_EXCHANGE, null, sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 发送商户商品上下架广播信息--------------------------------");
    }

    /**
     * 发送商品删除
     *
     * @author: wxf
     * @date: 2020/3/23 14:57
     * @param dto 发送的数据
     **/
    public void goodsDeleteFanout(GoodsEsDeteleDTO dto ) {
        log.info("-----------------------------开始 发送商品删除/取消授权信息--------------------------------");
        String sendMsg = JSON.toJSONString(dto);
        rabbitTemplate.convertAndSend(GoodsMqConstant.GOODS_DELETE_FANOUT_EXCHANGE, null, sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 发送商品删除/取消授权信息--------------------------------");
    }


    /**
     * 方法描述: 同步ES--平台编辑<br>
     *
     * @author: gz
     * @date: 2020/7/13 11:37
     * @param dto
     * @return: {@link }
     * @version 1.2.0
     **/
    public void sendPlatformGoodsUpdateMsg(GoodsExtToEsDTO dto){
        log.info("-----------------------------开始 发送平台商品修改信息消息--------------------------------");
        String sendMsg = JSON.toJSONString(dto);
        rabbitTemplate.convertAndSend(GoodsMqConstant.GOODS_EXCHANGE, GoodsMqConstant.PLATFORM_OR_MERCHANT_GOODS_UPDATE_QUEUE, sendMsg.getBytes(StandardCharsets.UTF_8));
        log.info("-----------------------------结束 平台商品修改信息消息--------------------------------");
    }



    /**
     * 获取延迟消息时限
     *
     * @param target 目标时间
     * @return 延迟消息时限
     */
    private Long getTtl(LocalDateTime target) {
        Long ttl = Duration.between(LocalDateTime.now(), target).toMillis();
        if (ttl > RabbitMqConstant.MAX_X_DELAY_TTL) {
            log.info("目标时间[{}]超过延迟最大限制，延迟时间采用最大延迟限制，实际业务由队列监听器进行重发处理",
                    FORMATTER.format(target));
            ttl = RabbitMqConstant.MAX_X_DELAY_TTL;
        }
        return ttl < 0L ? 0L : ttl;
    }
}
