package com.meiyuan.catering.job.service.order;

import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.marketing.dto.ticket.TicketDataRecordDTO;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.order.dto.submit.OrderTicketUsedRecordDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yaoozu
 * @description 定时订单服务
 * @date 2020/4/1311:14
 * @since v1.0.0
 */
@Service
@Slf4j
public class JobOrderService {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private MarketingTicketActivityClient activityClient;
    @Autowired
    private UserTicketClient userTicketClient;

    /**
     * @param
     * @return
     * @description 关闭未取餐的订单
     * <ul>
     * <li>自动关闭时间是预计取餐第二天的0点</li>
     * </ul>
     * @author yaozou
     * @date 2020/4/13 11:17
     * @since v1.0.0
     */
    @Transactional(rollbackFor = Exception.class)
    public void closeWaitingTokeOrder() {
        List<OrderTicketUsedRecordDTO> dtos = orderClient.closeOrder();
        if (CollectionUtils.isNotEmpty(dtos)) {
            try {
                dtos = dtos.stream().filter(i->CollectionUtils.isNotEmpty(i.getTicketIds())).collect(Collectors.toList());
                // 记录优惠券信息
                activityClient.saveTicketDataRecordBatch(ConvertUtils.sourceToTarget(dtos, TicketDataRecordDTO.class));
                dtos.forEach(e -> {
                    e.getTicketIds().forEach(i -> {
                        // 自动核销优惠券状态
                        userTicketClient.consumeTicket(i);
                        // 再次发券
                        userTicketClient.sendTicketAgain(i);
                    });
                });
            } catch (Exception e) {
                log.error("关闭未取餐订单处理优惠券业务异常:{}",e);
            }
        }
    }

    /**
     * 批量发单到达达（针对与订单的场景，指定配送时间超过1小时，下单时不会立即下发到达达）
     */
    public void batchAddOrderToDada(){
        orderClient.batchAddOrderToDada();
    }
}
