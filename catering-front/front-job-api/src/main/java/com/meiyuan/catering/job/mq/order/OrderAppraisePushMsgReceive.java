package com.meiyuan.catering.job.mq.order;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.util.CacheLockUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.job.utils.JobUtils;
import com.meiyuan.catering.order.dto.submit.CommentDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:  订单完成超时自动五星好评——消费消息
 *
 * @author: XiJie Xie
 * @date: 2020/3/24 16:42
 */
@Slf4j
@Component
@RabbitListener(queues = OrderMqConstant.ORDER_APPRAISE_EXPIRE_QUEUE)
public class OrderAppraisePushMsgReceive {
    @Autowired
    private OrderClient orderClient;
    @Resource
    JobUtils jobUtils;
    @CreateCache
    private Cache<String, String> cache;

    @RabbitHandler
    public void ticketReceive(byte[] recived) {
        this.orderTimeOutPushMsg(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     * @param msg 订单完成超时自动五星好评消息-订单id
     */
    @RabbitHandler
    public void orderTimeOutPushMsg(String msg) {
        try {
            //和订单评论同一把锁
            cache.tryLockAndRun(CacheLockUtil.orderCommentLock(msg), CacheLockUtil.EXPIRE, TimeUnit.SECONDS, () -> {
                Result<CommentDTO> commentDtoResult = orderClient.autoCommentOrder(Long.valueOf(msg));
                if (commentDtoResult.success() && commentDtoResult.getData() != null) {
                    log.info("订单自动评价成功，处理结果 commentDTOResult：{}",commentDtoResult);
                } else {
                    log.info("订单自动评价失败，跳过超时处理，orderID：{}",msg);
                }
            });
        } catch (Exception e) {
            log.error("订单完成超时自动五星好评延迟任务处理异常:msg={},error={}", msg, e);
        }
    }

}
