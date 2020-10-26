package com.meiyuan.catering.order.service.submit;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.base.TimeRangeDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.core.util.merchant.ShopUtils;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.marketing.dto.ticket.UserTicketDetailsDTO;
import com.meiyuan.catering.marketing.feign.MarketingPullNewClient;
import com.meiyuan.catering.marketing.feign.MarketingRepertoryClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.dto.calculate.OrderCalculateDTO;
import com.meiyuan.catering.order.dto.calculate.OrdersCalculateGoodsDTO;
import com.meiyuan.catering.order.dto.submit.OrderSimpleDTO;
import com.meiyuan.catering.order.dto.submit.OrdersCreateDTO;
import com.meiyuan.catering.order.dto.submit.SubmitOrderDTO;
import com.meiyuan.catering.order.entity.*;
import com.meiyuan.catering.order.enums.*;
import com.meiyuan.catering.order.mq.sender.OrderTimeOutMqSender;
import com.meiyuan.catering.order.service.*;
import com.meiyuan.catering.order.service.calculate.OrdersCalculateService;
import com.meiyuan.catering.user.dto.cart.ClearCartDTO;
import com.meiyuan.catering.user.fegin.cart.CartClient;
import com.meiyuan.catering.user.fegin.sharebill.CartShareBillClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author XiJie-Xie
 * @email 1121075903@qq.com
 * @create 2020/3/23 14:45
 */
@Service
@Slf4j
public class OrdersSubmitService {
    @Resource
    private CateringOrdersService cateringOrdersService;
    @Resource
    private CateringOrdersDeliveryService cateringOrdersDeliveryService;
    @Resource
    private CateringOrdersGoodsService cateringOrdersGoodsService;
    @Resource
    private CateringOrdersDiscountsService cateringOrdersDiscountsService;
    @Resource
    private CateringOrdersGoodsDiscountService cateringOrdersGoodsDiscountService;
    @Resource
    private CateringOrdersActivityService cateringOrdersActivityService;
    @Resource
    private OrdersCalculateService ordersCalculateService;
    @Resource
    private UserTicketClient userTicketClient;
    @Resource
    private MarketingRepertoryClient marketingRepertoryClient;
    @Resource
    private CartClient cartClient;
    @Resource
    private OrderTimeOutMqSender orderTimeOutMqSender;
    @Resource
    private CartShareBillClient cartShareBillClient;
    @Resource
    private OrdersSupport ordersSupport;
    @CreateCache
    private Cache<String, String> cache;
    @Resource
    private ShopGoodsClient shopGoodsClient;
    @Resource
    private MarketingPullNewClient pullNewClient;
    @Resource
    private ShopClient shopClient;
    @Resource
    private MerchantUtils merchantUtils;

    /**
     * 提交订单
     *
     * @param param 订单提交信息
     * @return 提交订单操作结果
     */
    public Result<OrderSimpleDTO> submit(SubmitOrderDTO param) {
        Result<OrderSimpleDTO> dto = this.submitHandle(param);
        OrderSimpleDTO data = dto.getData();
        if (dto.success() && data != null) {
            this.submittedHandle(param, data);
        }
        return dto;
    }

    /**
     * 订单处理
     *
     * @param param 订单提交信息
     * @return 订单处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderSimpleDTO> submitHandle(SubmitOrderDTO param) {

        ShopInfoDTO shop = null;
        if (DeliveryWayEnum.invite.getCode().equals(param.getDeliveryWay())) {
            // 自提，校验移到提交订单
            if (param.getDeliveryShopId() == null) {
                // 没有选择自提点
                throw new CustomException("请选择自提点～");
            }
            // 自提点删除判断
            shop = shopClient.getShop(param.getDeliveryShopId());
            if (shop == null || shop.getDel()) {
                throw new CustomException("自提点去流浪了～");
            }
        }else {
            // 配送门店删除判断
            shop = shopClient.getShop(param.getShopId());
            if (shop == null || shop.getDel()) {
                throw new CustomException("门店去流浪了～");
            }
        }

        if (DeliveryWayEnum.Delivery.getCode().equals(param.getDeliveryWay())) {
            // 配送，校验移到提交订单
            if (param.getDeliveryId() == null) {
                // 没有选择配送地址
                throw new CustomException("请选择配送地址～");
            }
        }
        validateTakeoutOrSelfPickTime(param.getEstimateTime(), param.getEstimateEndTime(), param.getImmediateDeliveryTime(),
                param.getShopId(), param.getDeliveryWay(),param.getMapCoordinate(),shop.getMapCoordinate());

        validateCoupon(param.getCouponsId());

        OrderSimpleDTO orderSimpleDTO = new OrderSimpleDTO();
        //加锁处理(非堵塞锁) 锁用户，防止重复提交
        cache.tryLockAndRun(CacheLockUtil.orderCommitLock(param.getUserId()), CacheLockUtil.EXPIRE, TimeUnit.SECONDS, () -> {
            // 进行订单结算，并扣减订单秒杀库存
            OrderCalculateDTO calculateDTO = this.orderCalculate(param);
            // 从【订单结算】信息中构建【订单创建】信息
            OrdersCreateDTO ordersCreate = ordersSupport.buildOrderCreateDto(calculateDTO, param);
            // 从创建信息中转换提取【订单】实体信息,生成订单编号
            CateringOrdersEntity orders = OrdersConverter.convert(ordersCreate);
            // 从创建信息中转换提取【订单配送】实体信息,生成取餐码
            CateringOrdersDeliveryEntity ordersDelivery = OrdersConverter.deliveryConvert(ordersCreate);
            // 从创建信息中转换提取【订单商品】实体信息
            List<CateringOrdersGoodsEntity> ordersGoodsList = OrdersConverter.goodsConvert(ordersCreate);
            // 从创建信息中转换提取【订单优惠】实体信息
            List<CateringOrdersDiscountsEntity> discountsList = OrdersConverter.discountConvert(ordersCreate);
            // 从创建信息中转换提取【订单商品优惠】实体信息
            List<CateringOrdersGoodsDiscountEntity> goodsDiscountList = OrdersConverter.goodsDiscountConvert(ordersCreate);
            // 从创建信息中转换提取【订单活动】实体信息
            List<CateringOrdersActivityEntity> activityList = OrdersConverter.activityConvert(ordersCreate);
            // 保存订单创建信息
            saveOrderInfo(orders, ordersDelivery, ordersGoodsList, discountsList, goodsDiscountList, activityList);
            // 如果选择了优惠券
            if (param.getCouponsId() != null
                    || param.getCouponsWithShopId() != null) {
                // 标记优惠券已使用
                List<Long> ticketIds = Lists.newArrayList();
                if (param.getCouponsId() != null) {
                    ticketIds.add(param.getCouponsId());
                }
                if (param.getCouponsWithShopId() != null) {
                    ticketIds.add(param.getCouponsWithShopId());
                }
                Result result = this.userTicketClient.useOrderTickets(ticketIds, orders.getId());
                if (result.failure()) {
                    throw new CustomException("优惠券异常，不可使用");
                }
            }

            OrdersConverter.toOrderSimpleVo(orders, orderSimpleDTO, activityList);
        });
        return Result.succ(orderSimpleDTO);
    }


    /**
     * 校验卡券是否可用
     *
     * @param ticketId
     * @return
     * @author lh
     * @date 17:39 2020/10/19
     */
    private Boolean validateCoupon(Long ticketId) {
        if(Objects.isNull(ticketId)){
            return true;
        }
        Result<UserTicketDetailsDTO> userTicketInfoByTicketResult = userTicketClient.getUserTicketInfoByUserTicketId(ticketId);
        if(userTicketInfoByTicketResult.failure()){
            throw new CustomException("卡券数据异常，请稍候再试！");
        }
        UserTicketDetailsDTO userTicketDetailsDTO = userTicketInfoByTicketResult.getData();
        LocalDateTime now = LocalDateTime.now();
        // 优惠券不在有效使用范围，设置不可用
        if (!(now.isAfter(userTicketDetailsDTO.getUseBeginTime()) && now.isBefore(userTicketDetailsDTO.getUseEndTime()))) {
            throw new CustomException("所选优惠券已失效，请重新选择~");
        }
        return true;
    }

    /**
     * 外卖／自取时间校验
     *
     * @param estimateTime
     * @param estimateEndTime
     * @param immediateDeliveryTime [HH:mm]
     * @param shopId
     * @param deliveryWay
     * @param userMapCoordinate 用户收货地址经纬度
     * @param shopMapCoordinate 商家地址经纬度
     */
    private void validateTakeoutOrSelfPickTime(LocalDateTime estimateTime,LocalDateTime estimateEndTime,String immediateDeliveryTime,
            Long shopId,Integer deliveryWay,String userMapCoordinate,String shopMapCoordinate) {
        /** 自提与配送时间段校验 */

        String startTime = DateTimeUtil.getDateDisplayString(estimateTime, DateTimeUtil.H_M_PATTERN);
        String endTime = DateTimeUtil.getDateDisplayString(estimateEndTime, DateTimeUtil.H_M_PATTERN);

        ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(shopId);
        if (shopConfigInfo == null) {
            return;
        }
        List<TimeRangeDTO> deliveryTimeRanges = shopConfigInfo.getDeliveryTimeRanges();
        List<TimeRangeDTO> pickupTimeRanges = shopConfigInfo.getPickupTimeRanges();
        // 自取时间范围和配送时间范围，最后一个时间+1分钟（历史遗留原因，安卓APP给出解释）
        for (TimeRangeDTO dto : deliveryTimeRanges) {
            dto.setEndTime(timePlusOneMinute(dto.getEndTime()));
        }
        for (TimeRangeDTO dto : pickupTimeRanges) {
            dto.setEndTime(timePlusOneMinute(dto.getEndTime()));
        }

        if (DeliveryWayEnum.Delivery.getCode().equals(deliveryWay)) {
            /** 配送 */
            if (BaseUtil.judgeString(startTime) && BaseUtil.judgeString(endTime)) {
                if (!isInTimeRange(startTime, endTime, deliveryTimeRanges)) {
                    /** 【预计送达时间范围】不在配送时间段 */
                    throw new CustomException(ErrorCode.TAKEOUT_TIMEOUT_ERROR, "当前配送时间已发生变化，请重新选择配送时间~");
                }
            } else {
                if (!isInTimeRange(immediateDeliveryTime, deliveryTimeRanges)) {
                    /** 【立即送达时间】不在配送时间段 */
                    throw new CustomException(ErrorCode.TAKEOUT_TIMEOUT_ERROR, "当前配送时间已发生变化，请重新选择配送时间~");
                }
                //校验立即送达时间【后台计算出的】是否超出配送时间
                double addMin = addMin(userMapCoordinate, shopMapCoordinate);
                LocalDateTime addTime = LocalDateTime.now().plusMinutes(Math.round(addMin));
                String addImmediateDeliveryTime = DateTimeUtil.getDateTimeDisplayString(addTime, DateTimeUtil.H_M_PATTERN);
                if(!isInTimeRange(addImmediateDeliveryTime, deliveryTimeRanges)){
                    throw new CustomException("当前配送时间已发生变化，请重新选择配送时间~");
                }
                //校验立即送达时间是否和预算时间一致【允许五分钟的误差】
                addTime = addTime.minusMinutes(5);
                Boolean immediateDeliveryTimeCheckResult = DateTimeUtil.getDateTimeDisplayString(addTime, DateTimeUtil.H_M_PATTERN).compareTo(immediateDeliveryTime) <= 0;
                if(!immediateDeliveryTimeCheckResult){
                    throw new CustomException("当前配送时间已发生变化，请重新选择配送时间~");
                }
            }
        }
        if (DeliveryWayEnum.invite.getCode().equals(deliveryWay)) {
            /** 自取 */
            if (!isInTimeRange(startTime, endTime, pickupTimeRanges)) {
                // 不再自取时间段
                throw new CustomException(ErrorCode.SELF_PICKUP_TIMEOUT_ERROR, "当前自取时间已发生变化，请重新选择自取时间~");
            }
        }

    }


    /**
     * 计算收货地址经纬度和商家之间配送所需时间
     *
     * @param userMapCoordinate 用户收货地址经纬度
     * @param shopMapCoordinate 商家地址经纬度
     * @return double 配送所需时间
     * @author lh
     * @date 18:36 2020/10/12
     */
    private double addMin(String userMapCoordinate,String shopMapCoordinate){
        String bdLocation = GpsCoordinateUtils.calGCJ02toBD09(userMapCoordinate);
        double distance = EsUtil.distance(bdLocation, shopMapCoordinate);
        //获取需要添加的分钟数
        double addMin = EsUtil.calMinuteByDistance(distance);
        return addMin;
    }

    /**
     * 字符串时间加1分钟
     *
     * @param timeStr 字符串时间，如17:29
     * @return
     */
    private String timePlusOneMinute(String timeStr) {
        String zero = "23:59";
        if (zero.equals(timeStr)) {
            return timeStr;
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date time = null;
        try {
            time = df.parse(timeStr);
        } catch (ParseException e) {
            throw new CustomException("配送／自取时间转换失败");
        }
        time.setTime(time.getTime() + 60 * 1000);
        return df.format(time);
    }

    /**
     * 指定配送 立即送达时间是否在配置时间段范围
     *
     * @param immediateDeliveryTime 立即送达时间
     * @param deliveryTimeRanges
     * @return true：是。false：否
     */
    private boolean isInTimeRange(String immediateDeliveryTime,List<TimeRangeDTO> deliveryTimeRanges) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(deliveryTimeRanges)) {
            return false;
        }
        for (TimeRangeDTO range : deliveryTimeRanges) {
            if (immediateDeliveryTime.compareTo(range.getStartTime()) >= 0
                    && immediateDeliveryTime.compareTo(range.getEndTime()) <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定配送时间段是否在配置时间段范围，同时适用于自提时间段判断和外卖时间段判断
     *
     * @param startTime
     * @param endTime
     * @param deliveryTimeRanges
     * @return true：是。false：否
     */
    private boolean isInTimeRange(String startTime, String endTime, List<TimeRangeDTO> deliveryTimeRanges) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(deliveryTimeRanges)) {
            return false;
        }
        for (TimeRangeDTO range : deliveryTimeRanges) {
            if (startTime.compareTo(range.getStartTime()) >= 0
                    && endTime.compareTo(range.getEndTime()) <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * V1.3.0 下单即处理拉新
     *
     * @param orders       订单ID
     * @param activityList 活动信息集合
     * @author: GongJunZheng
     * @date: 2020/8/18 15:27
     * @return: {@link }
     * @version V1.3.0
     **/
    private void pullNew(CateringOrdersEntity orders, List<CateringOrdersActivityEntity> activityList) {
        if (BaseUtil.judgeList(activityList)) {
            // 查询是否是门店首单
            Boolean shopFirstOrder = cateringOrdersService.shopFirstOrder(orders.getMemberId(), orders.getStoreId());
            if (shopFirstOrder) {
                // 门店首单
                if (OrderTypeEnum.BULK.getStatus().equals(orders.getOrderType())) {
                    // 团购商品
                    CateringOrdersActivityEntity activityEntity = activityList.get(0);
                    pullNewClient.insertPullNew(activityEntity.getActivityId(), activityEntity.getActivityType(), orders.getMemberId(), orders.getId());
                } else {
                    if (BaseUtil.judgeList(activityList)) {
                        // 活动数据不为空，说明有其他活动（秒杀-团购不共存）
                        List<Long> seckillIdList = new ArrayList<>();
                        List<Long> specialIdList = new ArrayList<>();
                        activityList.forEach(item -> {
                            if (OrderActivityEnum.SECONDS_KILL.getCode().equals(item.getActivityType())) {
                                // 秒杀  判重，有可能一个订单选择了两个相同活动的商品
                                if (!seckillIdList.contains(item.getActivityId())) {
                                    pullNewClient.insertPullNew(item.getActivityId(), item.getActivityType(), orders.getMemberId(), orders.getId());
                                    seckillIdList.add(item.getActivityId());
                                }
                            }
                            if (OrderActivityEnum.SPECIAL.getCode().equals(item.getActivityType())) {
                                // 特价商品 判重，有可能一个订单选择了两个相同活动的特价商品
                                if (!specialIdList.contains(item.getActivityId())) {
                                    pullNewClient.insertPullNew(item.getActivityId(), item.getActivityType(), orders.getMemberId(), orders.getId());
                                    specialIdList.add(item.getActivityId());
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 功能描述: 进行订单结算，并扣减订单秒杀库存
     *
     * @param
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 14:50
     * @since v 1.1.0
     */
    private OrderCalculateDTO orderCalculate(SubmitOrderDTO param) {
        // 获取订单结算信息
        OrderCalculateDTO calculateDTO = this.ordersCalculateService.calculateStrategy(param);
        if (calculateDTO == null) {
            throw new CustomException("订单结算异常");
        }
        // 订单商品信息
        List<OrdersCalculateGoodsDTO> goodsList = calculateDTO.getGoodsList();
        if (goodsList == null || goodsList.size() == 0) {
            throw new CustomException("订单结算异常");
        }
        /**
         * 更新sku库存
         * 只需要更新非秒杀商品库存
         * 只需更新设置了库存的（库存大于0），没设置的不限购
         * 这里的goodsList来自购物车，购物车没有存储商品库存，需要额外查询
         */
        List<OrdersCalculateGoodsDTO> ordinaryGoodsList = goodsList
                .stream()
                .filter(i -> i.getGoodsType().equals(OrderGoodsTypeEnum.
                        ORDINARY.getValue()) || i.getGoodsType().equals(OrderGoodsTypeEnum.
                        SPECIAL.getValue()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(ordinaryGoodsList)) {
            ConcurrentHashMap<String/* 商品SKU编码 */, Integer/* 订单商品数量 */> skuMap = new ConcurrentHashMap<>(16);
            /* 拼单，同一个商品SKU，会有多人购买 */
            for (OrdersCalculateGoodsDTO dto : ordinaryGoodsList) {
                /*库存做减法，这里将数量乘以-1*/
                int orderGoodsCount = dto.getQuantity() * -1;
                if (skuMap.containsKey(dto.getGoodsSkuCode())) {
                    skuMap.put(dto.getGoodsSkuCode(), skuMap.get(dto.getGoodsSkuCode()) + orderGoodsCount);
                } else {
                    skuMap.put(dto.getGoodsSkuCode(), orderGoodsCount);
                }
            }
            shopGoodsClient.batchUpdateSkuStock(param.getShopId(), skuMap);
        }

        for (OrdersCalculateGoodsDTO calculateGoods : goodsList) {
            // 如果是秒杀商品
            // 扣减秒杀商品库存
            if (OrderGoodsTypeEnum.SECONDS.getValue().equals(calculateGoods.getGoodsType())) {
                this.marketingRepertoryClient.syncSeckillInventory(calculateGoods.getGoodsId(), calculateGoods.getQuantity(), true, calculateGoods.getSeckillEventId());
            }
        }
        return calculateDTO;
    }

    /**
     * 保存订单创建信息
     *
     * @param orders            订单主题信息
     * @param goodsList         订单商品列表
     * @param discountsList     订单优惠列表
     * @param goodsDiscountList 订单商品优惠列表
     */
    private void saveOrderInfo(CateringOrdersEntity orders,
                               CateringOrdersDeliveryEntity ordersDelivery,
                               List<CateringOrdersGoodsEntity> goodsList,
                               List<CateringOrdersDiscountsEntity> discountsList,
                               List<CateringOrdersGoodsDiscountEntity> goodsDiscountList,
                               List<CateringOrdersActivityEntity> activityList) {
        this.cateringOrdersService.save(orders);
        log.debug("保存订单信息：【{}】", orders);
        this.cateringOrdersDeliveryService.save(ordersDelivery);
        log.debug("保存订单配送信息：【{}】", ordersDelivery);
        this.cateringOrdersGoodsService.saveBatch(goodsList);
        log.debug("保存订单商品信息：【{}】", goodsList);
        if (!CollectionUtils.isEmpty(discountsList)) {
            this.cateringOrdersDiscountsService.saveBatch(discountsList);
        }
        if (!CollectionUtils.isEmpty(goodsDiscountList)) {
            this.cateringOrdersGoodsDiscountService.saveBatch(goodsDiscountList);
        }
        if (!CollectionUtils.isEmpty(activityList)) {
            this.cateringOrdersActivityService.saveBatch(activityList);
        }
    }

    /**
     * 功能描述: 订单提交成功后的处理,清空购物车、拼单状态变为已结算、记录订单提交记录、发送订单完成的超时延迟消息（超时自动五星好评、不能申请售后）
     *
     * @param param
     * @param data
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 14:53
     * @since v 1.1.0
     */
    public void submittedHandle(SubmitOrderDTO param, OrderSimpleDTO data) {
        // 如果不是团购订单，下单成功需要清空购物车
        if (!CalculateTypeEnum.BULK.getCode().equals(param.getCalculateType())) {
            this.clearCart(param);
        }
        // 处理拼单信息，拼单状态变为已结算
        if (CalculateTypeEnum.SHARE_BILL.getCode().equals(param.getCalculateType())) {
            Result result = cartShareBillClient.shareBillCreateOrder(data.getId(), param.getShareBillNo());
            log.debug("生成订单成功，处理拼单信息结果 result={}", result);
        }
        // 记录订单进度
        this.ordersSupport.saveOperation(data.getId(), data.getOrderNumber(), OrderOperationEnum.SUBMIT,
                data.getMerchantId(), data.getMemberName(), data.getMemberPhone(), OrderOffTypeEnum.MEMBER_CANCEL.getCode());
        // 如果配置了支付超时时间
        if (data.getPayDeadline() != null) {
            // 发送订单超时未支付MQ延迟消息
            log.debug("发送订单延迟支付自动取消任务消息：" + JsonUtil.toJson(data));
            orderTimeOutMqSender.sendTicketPushMsg(data.getId().toString(), data.getPayDeadline());
        }

        // V1.3.0 下单即处理拉新
        pullNew(data.getOrders(), data.getActivityList());

    }

    /**
     * 功能描述:  清空购物车
     *
     * @param
     * @return: void
     */
    public void clearCart(SubmitOrderDTO param) {
        ClearCartDTO clearCartDTO = new ClearCartDTO();
        BeanUtils.copyProperties(param, clearCartDTO);
        clearCartDTO.setType(param.getCalculateType());
        Result clear = this.cartClient.clearForCreateOrder(clearCartDTO);
        if (clear.failure()) {
            log.error("清空购物车失败 clear = 【{}】", clear);
            throw new CustomException("清空购物车失败");
        }
    }
}
