package com.meiyuan.catering.job.mq.marketing;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CacheLockUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.yly.YlyUtils;
import com.meiyuan.catering.marketing.dto.groupon.MarketingGrouponUpDownDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGroupOrderEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.enums.MarketingGroupOrderStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingGroupOrderService;
import com.meiyuan.catering.marketing.service.CateringMarketingGrouponService;
import com.meiyuan.catering.marketing.vo.groupon.MarketingGrouponEndVO;
import com.meiyuan.catering.merchant.dto.shop.config.YlyDeviceInfoVo;
import com.meiyuan.catering.merchant.feign.ShopPrintingConfigClient;
import com.meiyuan.catering.order.dto.query.merchant.OrdersDetailMerchantDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.utils.Prient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author luohuan
 * @date 2020/4/2
 * 团购定时上/下架任务监听
 **/
@Slf4j
@Component
public class GrouponUpDownMsgReceive {

    @Autowired
    private CateringMarketingGrouponService grouponService;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private ShopPrintingConfigClient shopPrintingConfigClient;
    @CreateCache
    private Cache<String, String> cache;
    @Autowired
    private CateringMarketingGroupOrderService groupOrderService;
    @Resource
    YlyUtils ylyUtils;

    @RabbitListener(queues = MarketingMqConstant.MARKETING_GROUPON_EXPIRE_QUEUE)
    @RabbitHandler
    public void grouponUpDownMsg(byte[] receive) {
        String msg = new String(receive, StandardCharsets.UTF_8);

        // V1.3.0 为团购结束消息
        log.info("接收到团购定时结束消息，msg={}", msg);
        try {
            MarketingGrouponUpDownDTO dto = JSONObject.parseObject(msg, MarketingGrouponUpDownDTO.class);
            // 团购活动ID
            Long id = dto.getId();
            // 结束时间
            LocalDateTime endTime = dto.getEndTime();
            // 有可能收到两条一摸一样的团购结束消息，加锁进行处理
            // 出现的情况举例：商户创建时间范围2020-10-19 23:00:00至2020-10-20 10:00:00的团购活动，由于在创建的时候，判断结束
            //              时间距离当前小于24小时，就会发送一条延迟结束团购消息；在配置中，每天晚上23:30:00会进行第二天结束的团购活动
            //              信息查询任务，进行发送延迟结束团购消息，这样一来，在定时任务中，就会查询出来当前这个团购，再发送一
            //              条延迟结束团购消息，同一个团购活动就会出现两条消息，但是我们只能消费一条，所以加锁
            cache.tryLockAndRun(CacheLockUtil.grouponEnd(id), CacheLockUtil.EXPIRE, TimeUnit.SECONDS, () -> {
                CateringMarketingGrouponEntity grouponEntity = grouponService.getById(id);
                boolean isFail = grouponEntity == null || grouponEntity.getDel()
                        || MarketingUpDownStatusEnum.DOWN.getStatus().equals(grouponEntity.getUpDown())
                        || !endTime.isEqual(grouponEntity.getEndTime());
                if (isFail) {
                    // 为空  被删除  被冻结  结束时间不匹配都不需要继续执行
                    return;
                }
                // 查询团购订单数据信息
                List<CateringMarketingGroupOrderEntity> groupOrderList = groupOrderService.listByOfId(id);
                if(!BaseUtil.judgeList(groupOrderList)) {
                    // 没有团购订单，不需要处理
                    return;
                }
                boolean makeFlag = true;
                for (CateringMarketingGroupOrderEntity groupOrder : groupOrderList) {
                    // 只要有一个团购订单状态不是拼团中，说明已经接收到了消息，或者被冻结，就不需要处理了
                    if(MarketingGroupOrderStatusEnum.SUCCESS.getStatus().equals(groupOrder.getStatus())
                            || MarketingGroupOrderStatusEnum.FAILURE.getStatus().equals(groupOrder.getStatus())) {
                        makeFlag = false;
                        break;
                    }
                }
                if(makeFlag) {
                    MarketingGrouponEndVO returnVO = groupOrderService.endGroup(id, false);
                    if(BaseUtil.judgeList(returnVO.getOrderNumbers())) {
                        // 订单编号不为空，说明有成功的团购订单，查询对应的订单ID集合
                        Result<List<Long>> grouponOrderIdsResult = orderClient.listOrderIdByOrderNumber(returnVO.getOrderNumbers());
                        // 团购订单ID集合
                        List<Long> grouponOrderIds = grouponOrderIdsResult.getData();
                        if(BaseUtil.judgeList(grouponOrderIds)) {
                            // 店铺ID
                            Long shopId = returnVO.getShopId();
                            //订单语音通知
                            shopPrintingConfigClient.saveShopOrderNotice(shopId, grouponOrderIds);
                            grouponOrderIds.forEach(orderId->{
                                //订单打印小票
                                Result<OrdersDetailMerchantDTO> result = this.orderClient.orderDetailQueryMerchant(orderId);
                                OrdersDetailMerchantDTO ordersDetail = result.getData();
                                if (ObjectUtils.isEmpty(ordersDetail)) {
                                    log.error("门店订单查询失败，订单号 ： " + orderId);
                                }else {
                                    //打印小票
                                    Result<List<YlyDeviceInfoVo>> listResult = shopPrintingConfigClient.ylyDevicePage(shopId);
                                    if (BaseUtil.judgeResultList(listResult)){
                                        List<YlyDeviceInfoVo> ylyDeviceInfoList = listResult.getData().stream().filter(distinctByKey(YlyDeviceInfoVo::getDeviceNumber)).collect(Collectors.toList());
                                        this.printOrderInfo(ylyDeviceInfoList,ordersDetail);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.error("团购活动延迟任务异常.", e);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    private void printOrderInfo(List<YlyDeviceInfoVo> ylyDeviceInfoList, OrdersDetailMerchantDTO ordersDetail) {
        ylyDeviceInfoList.forEach(ylyDeviceInfoVo -> {
            if (ylyDeviceInfoVo.getPrintingTimes()>0){
                for (int i=0;i<ylyDeviceInfoVo.getPrintingTimes();i++){
                    ylyUtils.printIndex(ylyDeviceInfoVo.getDeviceNumber(),
                            Prient.getTakeOutTemplate(ordersDetail, new StringBuilder()).toString(),
                            String.valueOf(ordersDetail.getOrderId()));
                }
            }
            if (ylyDeviceInfoVo.getCookTimes()>0){
                for (int i=0;i<ylyDeviceInfoVo.getCookTimes();i++){
                    ylyUtils.printIndex(ylyDeviceInfoVo.getDeviceNumber(),
                            Prient.getKitchenTemplate(ordersDetail, new StringBuilder()),
                            String.valueOf(ordersDetail.getOrderId()));
                }
            }
        });
    }
}
