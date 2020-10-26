package com.meiyuan.catering.user.fegin.cart;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.user.entity.CateringCartShareBillEntity;
import com.meiyuan.catering.user.service.CateringCartService;
import com.meiyuan.catering.user.service.CateringCartShareBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yaoozu
 * @description 购物车定时任务
 * @date 2020/5/2516:15
 * @since v1.0.0
 */
@Service
@Slf4j
public class JobCartClient {
    @Autowired
    private CateringCartShareBillService shareBillService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CateringCartService cartService;

    /**
     * @description 自动取消商品模式拼单（清理购物车、拼单信息、拼单人信息）
     * <ul>
     *     <li>1、每日凌晨失效2天前拼单</li>
     *     <li>2、选购中、结算中（未生成订单）：删除购物车所有信息，删除拼单人信息</li>
     *     <li>3、已结算（已生成订单）：删除拼单人信息、未支付的订单系统自动取消</li>
     * </ul>
     * @author yaozou
     * @date 2020/3/31 10:44
     * @since v1.0.0
     */
    public void autoCancelForGoodsModel(){
        log.info("--------------start auto cancel share bill for goods model------------------");
        // type 拼单类别:1--菜单，2--商品
        // day 处理N天前拼单

        List<CateringCartShareBillEntity> list = shareBillService.jobAutoCancelShareBill(2,2,null);
        List<Long> orderIds = new ArrayList<>();
        list.forEach(bill -> {
            log.info("auto cancel share bill NO:{}", bill.getShareBillNo());

            shareBillService.autoCleanBillData(bill.getShareBillNo());
            cartService.delByShareBillNo(bill.getShareBillNo());
            if (bill.getOrderId() != null) {
                orderIds.add(bill.getOrderId());
            }

        });

        sendCancelOrderMsg(orderIds);
        log.info("--------------end auto cancel share bill for goods model------------------");
    }


    /**
     * @description 自动取消菜单模式拼单（清理购物车、拼单信息、拼单人信息）
     * <ul>
     *     <li>1、每日凌晨失效2天前拼单</li>
     *     <li>2、选购中、结算中（未生成订单）：删除购物车所有信息，删除拼单人信息</li>
     *     <li>3、已结算（已生成订单）：删除拼单人信息、未支付的订单系统自动取消</li>
     * </ul>
     * @author yaozou
     * @date 2020/3/31 10:44
     * @since v1.0.0
     */
    public void autoCancelFormenuModel(List<Long> menuIds){
        log.info("--------------start auto cancel share bill for menu model------------------");
        log.info("--------------end auto cancel share bill for menu model------------------");
    }

    private void sendCancelOrderMsg(List<Long> orderIds){
        if (orderIds.size() > 0){
            try {
                rabbitTemplate.convertAndSend(
                        OrderMqConstant.UNPAID_CANCEL_EXCHANGE,
                        OrderMqConstant.UNPAID_CANCEL_KEY,
                        JSONObject.toJSONString(orderIds).getBytes());
                log.info("成功发送未支付取消订单消息");
            } catch (Exception e) {
                log.error("未支付取消订单消息发送失败:orderIds={},error={}", orderIds, e);
            }
        }
    }
}
