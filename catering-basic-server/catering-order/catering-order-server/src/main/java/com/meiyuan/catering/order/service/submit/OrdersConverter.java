/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.meiyuan.catering.order.service.submit;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.order.dto.submit.*;
import com.meiyuan.catering.order.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单实体转换工具类
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/10/19 9:28
 */
public class OrdersConverter {

    /**
     * OrdersCreateDTO --> OrdersEntity
     *
     * @param dto 订单创建信息
     * @return 订单实体信息
     */
    public static CateringOrdersEntity convert(OrdersCreateDTO dto) {
        CateringOrdersEntity entity = new CateringOrdersEntity();
        dto.setId(IdWorker.getId());
        // 生成订单编号
        String orderNo = CodeGenerator.orderNo();
        dto.setOrderNumber(orderNo);
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateBy(dto.getMemberId());
        entity.setCreateName(dto.getMemberName());
        entity.setCreateTime(LocalDateTime.now());
        return entity;
    }

    /**
     * OrdersCreateDTO --> CateringOrdersDeliveryEntity
     *
     * @param dto 订单创建信息
     * @return 订单实体信息
     */
    public static CateringOrdersDeliveryEntity deliveryConvert(OrdersCreateDTO dto) {
        CateringOrdersDeliveryEntity entity = new CateringOrdersDeliveryEntity();
        OrdersDeliveryCreateDTO delivery = dto.getDelivery();
        BeanUtils.copyProperties(delivery, entity);
        // 生产取餐码
        entity.setConsigneeCode(CodeGenerator.randomCode(6));
        entity.setId(IdWorker.getId());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setOrderId(dto.getId());
        entity.setCreateBy(dto.getMemberId());
        entity.setCreateName(dto.getMemberName());
        entity.setCreateTime(LocalDateTime.now());
        return entity;
    }
    /**
     * OrdersGoodsCreateDTO --> OrdersGoodsEntity
     *
     * @param dto 订单商品创建信息
     * @return 订单商品实体信息
     */
    private static CateringOrdersGoodsEntity goodsConvert(OrdersGoodsCreateDTO dto) {
        CateringOrdersGoodsEntity entity = new CateringOrdersGoodsEntity();
        dto.setId(IdWorker.getId());
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    /**
     * OrdersGoodsCreateDTO --> OrdersGoodsEntity
     *
     * @param dto 订单创建信息
     * @return 订单商品实体信息
     */
    public static List<CateringOrdersGoodsEntity> goodsConvert(OrdersCreateDTO dto) {
        List<CateringOrdersGoodsEntity> entities = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(dto.getGoodsList())) {
            entities = dto.getGoodsList().stream()
                    .map(e ->{
                        CateringOrdersGoodsEntity entity = OrdersConverter.goodsConvert(e);
                        entity.setCreateBy(dto.getMemberId());
                        entity.setCreateName(dto.getMemberName());
                        entity.setCreateTime(LocalDateTime.now());
                        entity.setOrderId(dto.getId());
                        entity.setSkillEventId(e.getSeckillEventId());
                        entity.setOrderNumber(dto.getOrderNumber());
                        return entity;
                    }).collect(Collectors.toList());
        }
        return entities;
    }

    /**
     * OrdersDiscountsCreateDTO --> OrdersDiscountsEntity
     *
     * @param dto 订单优惠创建信息
     * @return 订单优惠实体信息
     */
    private static CateringOrdersDiscountsEntity discountConvert(OrdersDiscountsCreateDTO dto) {
        CateringOrdersDiscountsEntity entity = new CateringOrdersDiscountsEntity();
        dto.setId(IdWorker.getId());
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }


    /**
     * OrdersDiscountsCreateDTO --> OrdersDiscountsEntity
     *
     * @param dto 订单创建信息
     * @return 订单优惠实体信息
     */
    public static List<CateringOrdersDiscountsEntity> discountConvert(OrdersCreateDTO dto) {
        List<CateringOrdersDiscountsEntity> entities = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(dto.getDiscountsList())) {
            entities = dto.getDiscountsList().stream()
                    .map(e ->{
                        CateringOrdersDiscountsEntity entity = OrdersConverter.discountConvert(e);
                        entity.setCreateBy(dto.getMemberId());
                        entity.setCreateName(dto.getMemberName());
                        entity.setCreateTime(LocalDateTime.now());
                        entity.setMerchantId(dto.getMerchantId());
                        entity.setOrderId(dto.getId());
                        entity.setOrderNumber(dto.getOrderNumber());
                        entity.setDiscountTotal(1);
                        return entity;
                    }).collect(Collectors.toList());
        }
        return entities;
    }

    /**
     * OrdersDiscountsCreateDTO --> OrdersGoodsDiscountEntity
     *
     * @param dto 订单创建信息
     * @return 订单商品优惠信息
     */
    public static List<CateringOrdersGoodsDiscountEntity> goodsDiscountConvert(OrdersCreateDTO dto) {
        List<CateringOrdersGoodsDiscountEntity> goodsDiscounts = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(dto.getDiscountsList())) {
            dto.getDiscountsList()
                    .stream().filter(discount -> !CollectionUtils.isEmpty(discount.getGoodsDiscountList()))
                    .forEach(discount ->
                            goodsDiscounts.addAll(discount.getGoodsDiscountList().stream().map(item -> {
                                List<Long> collect = dto.getGoodsList().stream().filter(e -> item.getGoodsId().equals(e.getGoodsId())).map(OrdersGoodsCreateDTO::getId).collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(collect)){
                                    item.setOrderGoodsId(collect.get(0));
                                }
                                item.setId(IdWorker.getId());
                                item.setOrderId(dto.getId());
                                item.setOrderNumber(dto.getOrderNumber());
                                item.setOrderDiscountsId(discount.getDiscountId());
                                CateringOrdersGoodsDiscountEntity entity = new CateringOrdersGoodsDiscountEntity();
                                BeanUtils.copyProperties(item, entity);
                                return entity;
                            }).collect(Collectors.toList())));
        }
        return goodsDiscounts;
    }

    /**
     * OrdersGoodsCreateDTO --> OrdersGoodsEntity
     *
     * @param dto 订单创建信息
     * @return 订单商品实体信息
     */
    public static List<CateringOrdersActivityEntity> activityConvert(OrdersCreateDTO dto) {
        List<CateringOrdersActivityEntity> entities = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(dto.getActivityList())) {
            entities = dto.getActivityList().stream()
                    .map(e ->{
                        CateringOrdersActivityEntity entity = OrdersConverter.activityConvert(e);
                        entity.setCreateBy(dto.getMemberId());
                        entity.setCreateName(dto.getMemberName());
                        entity.setCreateTime(LocalDateTime.now());
                        entity.setRelationId(dto.getId());
                        return entity;
                    }).collect(Collectors.toList());
        }
        return entities;
    }
    /**
     * OrdersGoodsCreateDTO --> OrdersGoodsEntity
     *
     * @param dto 订单商品创建信息
     * @return 订单商品实体信息
     */
    private static CateringOrdersActivityEntity activityConvert(OrdersActivityCreateDTO dto) {
        CateringOrdersActivityEntity entity = new CateringOrdersActivityEntity();
        dto.setId(IdWorker.getId());
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    /**
     * CateringOrdersEntity --> toOrderSimpleVo
     *
     * @param entity 订单实体信息
     * @return 订单简要信息Vo
     */
    public static OrderSimpleDTO toOrderSimpleVo(CateringOrdersEntity entity, OrderSimpleDTO orderSimpleDTO, List<CateringOrdersActivityEntity> activityList) {
        BeanUtils.copyProperties(entity, orderSimpleDTO);
        orderSimpleDTO.setOrders(entity);
        orderSimpleDTO.setActivityList(activityList);
        return orderSimpleDTO;
    }
}
