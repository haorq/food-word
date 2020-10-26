package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.dao.CateringOrdersConfigMapper;
import com.meiyuan.catering.order.dao.CateringOrdersDiscountsMapper;
import com.meiyuan.catering.order.dao.CateringOrdersMapper;
import com.meiyuan.catering.order.dao.CateringOrdersOperationMapper;
import com.meiyuan.catering.order.dto.OrderOffDTO;
import com.meiyuan.catering.order.dto.OrdersCheckParamDTO;
import com.meiyuan.catering.order.dto.calculate.*;
import com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO;
import com.meiyuan.catering.order.dto.submit.*;
import com.meiyuan.catering.order.entity.CateringOrdersConfigEntity;
import com.meiyuan.catering.order.entity.CateringOrdersDiscountsEntity;
import com.meiyuan.catering.order.entity.CateringOrdersEntity;
import com.meiyuan.catering.order.entity.CateringOrdersOperationEntity;
import com.meiyuan.catering.order.enums.*;
import com.meiyuan.catering.order.mq.sender.OrderTimeOutMqSender;
import com.meiyuan.catering.order.utils.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单表(CateringOrders)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
@Slf4j
@Component
public class OrdersSupport {

    @Resource
    private CateringOrdersOperationMapper cateringOrdersOperationMapper;
    @Resource
    private CateringOrdersMapper cateringOrdersMapper;
    @Resource
    private CateringOrdersConfigMapper cateringOrdersConfigMapper;
    @Resource
    private OrderTimeOutMqSender orderTimeOutMqSender;
    @Resource
    private OrderUtils orderUtils;
    @Resource
    private CateringOrdersDiscountsMapper cateringOrdersDiscountsMapper;
    @Resource
    private UserTicketClient userTicketClient;
    @Resource
    private MerchantUtils merchantUtils;

    /**
     * 功能描述: 根据取餐二维码获取订单信息
     *
     * @param paramDTO 取餐二维码参数
     * @return com.meiyuan.catering.order.entity.CateringOrdersEntity
     * @date 2020/5/6 10:15
     * @version v1.0.1
     */
    public CateringOrdersEntity getOrderByCode(OrdersCheckParamDTO paramDTO) {
        List<CateringOrdersEntity> cateringOrdersList = this.cateringOrdersMapper.getOrderInfoByMerchantInfo(paramDTO);
        int size = cateringOrdersList.size();
        if (size > 1) {
            throw new CustomException("验证码[" + paramDTO.getCode() + "]在多个订单存在,请到指定订单验证");
        }
        if (size == 0) {
            throw new CustomException("验证码错误或已失效");
        }

        CateringOrdersEntity cateringOrdersEntity = cateringOrdersList.get(0);
        return cateringOrdersEntity;
    }

    public void orderTimeOutMqSender(Long orderId) {
        LocalDateTime completeConfig = this.getConfig(OrderConfigTypeEnum.COMPLETE);
        LocalDateTime appraiseConfig = this.getConfig(OrderConfigTypeEnum.COMPLETE_APPRAISE);
        // 发送订单完成超时不能申请售后
        orderTimeOutMqSender.sendOrderAfterSalesPushMsg(orderId.toString(), completeConfig);
        // 发送订单完成超时自动五星好评
        orderTimeOutMqSender.sendOrderAppraisePushMsg(orderId.toString(), appraiseConfig);
    }

    /**
     * 功能描述: 获取订单超时配置时间
     *
     * @param
     * @return: com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO
     */
    public LocalDateTime getConfig(OrderConfigTypeEnum configTypeEnum) {
        OrdersConfigDTO config = this.orderUtils.getOrderConfig(configTypeEnum.getCode());
        if (config != null) {
            LambdaQueryWrapper<CateringOrdersConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CateringOrdersConfigEntity::getConfigKey, configTypeEnum.getCode());
            // 获取订单配置信息
            CateringOrdersConfigEntity configEntity = this.cateringOrdersConfigMapper.selectOne(queryWrapper);
            if (configEntity != null) {
                config = BaseUtil.objToObj(configEntity, OrdersConfigDTO.class);
                this.orderUtils.saveOrderConfig(config.getConfigKey(), config);
                if (configEntity.getConfigValue() != null) {
                    return this.getOrdersConfigTime(configEntity);
                }
            }
        }
        return null;
    }

    public LocalDateTime getOrdersConfigTime(CateringOrdersConfigEntity configEntity) {
        if (OrderConfigUnitTypeEnum.YEAR.getCode().equals(configEntity.getConfigUnit())) {
            return LocalDateTime.now().plusYears(configEntity.getConfigValue());
        } else if (OrderConfigUnitTypeEnum.MONTH.getCode().equals(configEntity.getConfigUnit())) {
            return LocalDateTime.now().plusMonths(configEntity.getConfigValue());
        } else if (OrderConfigUnitTypeEnum.DAY.getCode().equals(configEntity.getConfigUnit())) {
            return LocalDateTime.now().plusDays(configEntity.getConfigValue());
        } else if (OrderConfigUnitTypeEnum.HOUR.getCode().equals(configEntity.getConfigUnit())) {
            return LocalDateTime.now().plusHours(configEntity.getConfigValue());
        } else if (OrderConfigUnitTypeEnum.MINUTE.getCode().equals(configEntity.getConfigUnit())) {
            return LocalDateTime.now().plusMinutes(configEntity.getConfigValue());
        } else if (OrderConfigUnitTypeEnum.SECOND.getCode().equals(configEntity.getConfigUnit())) {
            return LocalDateTime.now().plusSeconds(configEntity.getConfigValue());
        }
        return null;
    }

    /**
     * 功能描述: 取消订单，更新数据库订单状态
     *
     * @param dto        取消信息
     * @param statusEnum 当前订单状态
     * @return int
     * @author xie-xi-jie
     * @date 2020/5/19 16:56
     * @since v 1.1.0
     */
    public int cancel(OrderOffDTO dto, OrderStatusEnum statusEnum) {
        CateringOrdersEntity entity = new CateringOrdersEntity();
        entity.setId(Long.valueOf(dto.getOrderId()));
        entity.setOffReason(dto.getOffReason());
        entity.setStatus(OrderStatusEnum.CANCELED.getValue());
        entity.setUpdateBy(dto.getOffUserId());
        entity.setUpdateName(dto.getOffUserName());
        entity.setUpdateTime(LocalDateTime.now());
        QueryWrapper<CateringOrdersEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id", Long.valueOf(dto.getOrderId()));
        objectQueryWrapper.eq("status", statusEnum.getValue());
        int update = this.cateringOrdersMapper.update(entity, objectQueryWrapper);
        return update;
    }

    public int saveOperation(Long orderId, String orderNumber, OrderOperationEnum operationEnum, Long operationId, String operationName, String operationPhone, int operationType) {
        CateringOrdersOperationEntity operationEntity = new CateringOrdersOperationEntity();
        operationEntity.setId(IdWorker.getId());
        operationEntity.setOrderId(orderId);
        operationEntity.setOrderNumber(orderNumber);
        operationEntity.setOperationPhase(operationEnum.getCode());
        operationEntity.setOperationId(operationId);
        operationEntity.setOperationName(operationName);
        operationEntity.setOperationPhone(operationPhone);
        operationEntity.setOperationType(operationType);
        operationEntity.setOperationExplain(operationEnum.getDesc());
        operationEntity.setOperationTime(LocalDateTime.now());
        int save = this.cateringOrdersOperationMapper.insert(operationEntity);
        return save;
    }


    /**
     * 订单用户提交信息转订单创建信息
     *
     * @param calculateDTO 订单结算信息
     * @param param        订单提交信息
     * @return 订单创建信息
     */
    public OrdersCreateDTO buildOrderCreateDto(OrderCalculateDTO calculateDTO, SubmitOrderDTO param) {
        LocalDateTime config = this.getConfig(OrderConfigTypeEnum.NORMAL);
        OrdersCreateDTO order = new OrdersCreateDTO();
        // 订单主信息copy
        BeanUtils.copyProperties(calculateDTO, order);
        order.setMemberId(calculateDTO.getUserId());
        order.setMemberName(calculateDTO.getNickname());
        order.setMemberPhone(calculateDTO.getPhone());
        order.setMemberType(calculateDTO.getUserType());
        order.setBillingTime(LocalDateTime.now());
        order.setOrderSource(OrderSourceEnum.WX_CLIENT.getCode());
        order.setStatus(OrderStatusEnum.UNPAID.value());
        order.setPaidAmount(BigDecimal.ZERO);
        order.setPayDeadline(config);
        order.setRemarks(param.getRemarks());
        order.setCalories(param.getCalories());
        OrdersCalculateDeliveryDTO calculateDelivery = calculateDTO.getDelivery();
        OrdersDeliveryCreateDTO delivery = BaseUtil.objToObj(calculateDelivery, OrdersDeliveryCreateDTO.class);
        delivery.setDeliveryId(calculateDTO.getDeliveryId());
        delivery.setTableware(param.getTableware());
        order.setDelivery(delivery);
        // 设置门店配送方式标识（0：自配送[默认]；1：达达配送）
        ShopInfoDTO shop = merchantUtils.getShopIsNullThrowEx(param.getShopId());
        order.setShopDeliveryFlag(shop.getDeliveryType() == 2 ? 1 : 0);
        // 订单商品信息copy
        List<OrdersGoodsCreateDTO> goodsList = calculateDTO.getGoodsList().stream().map(e -> {
            OrdersGoodsCreateDTO item = new OrdersGoodsCreateDTO();
            BeanUtils.copyProperties(e, item);
            if (e.getSalesPrice() != null && e.getSalesPrice().compareTo(BigDecimal.ZERO) > 0) {
                item.setTransactionPrice(e.getSalesPrice());
            } else {
                item.setTransactionPrice(e.getStorePrice());
            }
            item.setGoodsGroupId(e.getCategoryId());
            item.setGoodsGroupName(e.getCategoryName());
            return item;
        }).collect(Collectors.toList());
        if (calculateDTO.getGiftGoodsList() != null) {
            // 订单赠送商品信息copy
            List<OrdersGoodsCreateDTO> goodsGiftList = calculateDTO.getGiftGoodsList().stream().map(e -> {
                OrdersGoodsCreateDTO item = new OrdersGoodsCreateDTO();
                BeanUtils.copyProperties(e, item);
                return item;
            }).collect(Collectors.toList());
            goodsList.addAll(goodsGiftList);
        }
        order.setGoodsList(goodsList);
        // 订单优惠信息copy
        if (!CollectionUtils.isEmpty(calculateDTO.getDiscountList())) {
            List<OrdersDiscountsCreateDTO> discountsList = calculateDTO.getDiscountList().stream().map(e -> {
                OrdersDiscountsCreateDTO item = new OrdersDiscountsCreateDTO();
                BeanUtils.copyProperties(e, item);
                List<OrdersGoodsDiscountCreateDTO> goodsDiscountList = e.getGoodsDiscountList().stream().map(obj -> {
                    OrdersGoodsDiscountCreateDTO goodsDiscount = new OrdersGoodsDiscountCreateDTO();
                    BeanUtils.copyProperties(obj, goodsDiscount);
                    return goodsDiscount;
                }).collect(Collectors.toList());
                item.setGoodsDiscountList(goodsDiscountList);
                if (e.getConsumeCondition().compareTo(BigDecimal.ZERO) >= 0) {
                    String describe = "满" + e.getConsumeCondition().setScale(0, BigDecimal.ROUND_DOWN) + "减" + e.getDiscountAmount().setScale(0, BigDecimal.ROUND_DOWN);
                    item.setDiscountDescribe(describe);
                }
                return item;
            }).collect(Collectors.toList());
            order.setDiscountsList(discountsList);
        }
        // 订单营销活动copy
        if (!CollectionUtils.isEmpty(calculateDTO.getActivityList())) {
            List<OrdersActivityCreateDTO> activityList = calculateDTO.getActivityList().stream().map(e -> {
                OrdersActivityCreateDTO item = new OrdersActivityCreateDTO();
                BeanUtils.copyProperties(e, item);
                return item;
            }).collect(Collectors.toList());
            order.setActivityList(activityList);
        }
        return order;
    }

    /**
     * 功能描述: 获取拼单用户信息，并将各自的拼单商品进行组装
     *
     * @param shareBillUserId     拼单人ID
     * @param shareBillUserName   拼单人姓名
     * @param shareBillUserAvatar 拼单人头像
     * @param goodsInfoList       拼单商品
     * @return com.meiyuan.catering.order.dto.calculate.ShareBillCalculateGoodsInfoDTO
     * @author xie-xi-jie
     * @date 2020/5/21 10:59
     * @since v 1.1.0
     */
    public ShareBillCalculateGoodsInfoDTO getWxBillUser(Long shareBillUserId, String shareBillUserName, String shareBillUserAvatar, List<OrdersCalculateGoodsInfoDTO> goodsInfoList) {
        WxBillUserDTO wxBillUserDTO = new WxBillUserDTO();
        wxBillUserDTO.setUserId(shareBillUserId);
        wxBillUserDTO.setNickname(shareBillUserName);
        wxBillUserDTO.setAvatar(shareBillUserAvatar);
        ShareBillCalculateGoodsInfoDTO shareBillGoodsInfo = new ShareBillCalculateGoodsInfoDTO();
        shareBillGoodsInfo.setUser(wxBillUserDTO);
        shareBillGoodsInfo.setGoodsInfo(goodsInfoList);
        return shareBillGoodsInfo;
    }

    /**
     * 功能描述: 订单状态转换
     *
     * @param orderStatus 数据库订单状态
     * @param afterSales  订单是否申请售后
     * @return: 订单状态描述
     */
    public MerchantOrderStatusEnum orderStatusConver(Integer orderStatus, Integer refundStatus, Boolean afterSales) {

        MerchantOrderStatusEnum statusEnum = null;
        if (OrderStatusEnum.WAIT_DELIVERY.getValue().equals(orderStatus)) {
            statusEnum = MerchantOrderStatusEnum.WAIT_DELIVERY;
        } else if (OrderStatusEnum.WAIT_TAKEN.getValue().equals(orderStatus)) {
            statusEnum = MerchantOrderStatusEnum.WAIT_TAKEN;
        } else if (OrderStatusEnum.DONE.getValue().equals(orderStatus)) {
            statusEnum = MerchantOrderStatusEnum.DONE;
            if (afterSales) {
                if (RefundStatusEnum.AWAIT_REFUND.getStatus().equals(refundStatus)) {
                    statusEnum = MerchantOrderStatusEnum.UN_REFUND;
                } else if (RefundStatusEnum.SUCCESS_REFUND.getStatus().equals(refundStatus)) {
                    statusEnum = MerchantOrderStatusEnum.REFUND;
                }
            }
        } else if (OrderStatusEnum.CANCELED.getValue().equals(orderStatus)) {
            statusEnum = MerchantOrderStatusEnum.CANCELED;
        } else if (OrderStatusEnum.OFF.getValue().equals(orderStatus)) {
            statusEnum = MerchantOrderStatusEnum.OFF;
        } else if (OrderStatusEnum.GROUP.getValue().equals(orderStatus)) {
            statusEnum = MerchantOrderStatusEnum.GROUP;
        } else {
            throw new CustomException("订单状态异常：" + orderStatus);
        }
        return statusEnum;
    }


    /**
     * 描述:退款原因字符串
     *
     * @param list
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/4/10 11:15
     */
    public String getRefundReasonStr(List<Integer> list) {
        if (list != null && list.size() > 0) {
            StringBuilder builder = new StringBuilder();
            list.forEach(i -> builder.append(RefundReasonEnum.getByValue(i).getDesc()));
            return builder.toString();
        }
        return "";
    }

    @Async
    public void sendTicketAgain(Long orderId) {
        LambdaQueryWrapper<CateringOrdersDiscountsEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CateringOrdersDiscountsEntity::getOrderId, orderId);
        List<CateringOrdersDiscountsEntity> list = this.cateringOrdersDiscountsMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (CateringOrdersDiscountsEntity entity : list) {
            // 订单完成核销后--标记优惠券核销状态
            userTicketClient.consumeTicket(entity.getDiscountId());
        }
    }
}
