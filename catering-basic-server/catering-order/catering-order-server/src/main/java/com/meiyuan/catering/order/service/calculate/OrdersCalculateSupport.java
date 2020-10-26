/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.meiyuan.catering.order.service.calculate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.enums.base.SaleChannelsEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.NumberUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.goods.dto.goods.GoodsListDTO;
import com.meiyuan.catering.goods.feign.GoodsClient;
import com.meiyuan.catering.marketing.dto.MarketingGoodsCategoryAddDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsInfoDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsSimpleInfoDTO;
import com.meiyuan.catering.marketing.dto.ticket.UserTicketDetailsDTO;
import com.meiyuan.catering.marketing.enums.*;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.merchant.dto.merchant.MerchantsPickupAddressDTO;
import com.meiyuan.catering.merchant.dto.pickup.PickupAdressQueryDTO;
import com.meiyuan.catering.merchant.feign.MerchantPickupAdressClient;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.merchant.vo.merchant.PickupAddressListVo;
import com.meiyuan.catering.order.dto.calculate.*;
import com.meiyuan.catering.order.enums.*;
import com.meiyuan.catering.order.utils.OrderUtils;
import com.meiyuan.catering.user.dto.cart.CartShareBillUserDTO;
import com.meiyuan.catering.user.dto.cart.CartSimpleDTO;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.fegin.address.AddressClient;
import com.meiyuan.catering.user.fegin.sharebill.CartShareBillUserClient;
import com.meiyuan.catering.user.vo.address.AddressDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单结算支持类-主要用于订单结算
 *
 * @Author XiJie-Xie
 * @email 1121075903@qq.com
 * @create 2020/3/10 13:41
 */
@Slf4j
@Component
public class OrdersCalculateSupport {

    @Resource
    private MarketingGoodsClient marketingGoodsClient;
    @Resource
    private CartShareBillUserClient cartShareBillUserClient;
    @Resource
    private EsGoodsClient esGoodsClient;
    @Autowired
    private UserTicketClient userTicketClient;
    @Resource
    private OrderUtils orderUtils;
    @Resource
    private AddressClient addressClient;
    @Resource
    private MerchantPickupAdressClient merchantPickupAdressClient;
    @Resource
    private GoodsClient goodsClient;
    @Resource
    private ShopGoodsSkuClient shopGoodsSkuClient;
    @Resource
    private MerchantGoodsClient merchantGoodsClient;

    /**
     * 订单结算优惠券转换
     *
     * @param userTicketDetailsDTOList
     * @param couponsAmount            平台优惠券实际优惠金额
     * @param couponsAmountWithShop    门店优惠券实际优惠金额
     * @return
     */
    public List<OrdersCalculateDiscountDTO> buildCalculateDiscount(
            List<UserTicketDetailsDTO> userTicketDetailsDTOList,
            BigDecimal couponsAmount,
            BigDecimal couponsAmountWithShop) {
        // 1、构造订单优惠信息
        List<OrdersCalculateDiscountDTO> goodsDiscountList = new ArrayList<>();

        for (UserTicketDetailsDTO data : userTicketDetailsDTOList) {
            OrdersCalculateDiscountDTO goodsDiscountDTO = new OrdersCalculateDiscountDTO();
            BeanUtils.copyProperties(data, goodsDiscountDTO);
            goodsDiscountDTO.setDiscountId(data.getUserTicketId());
            goodsDiscountDTO.setDiscountName(data.getTicketName());
            goodsDiscountDTO.setDiscountNumber(data.getTicketCode());
            goodsDiscountDTO.setDiscountType(OrderDiscountTypeEnum.TICKET.getCode());
            // 优惠券金额取前端传过来的实际扣减金额
//            goodsDiscountDTO.setDiscountAmount(NumberUtils.setScale(data.getAmount()));

            if (UserTicketSendEnum.PLATFORM_TICKET.getStatus().equals(data.getSendTicketParty())) {

                if (couponsAmount == null) {
                    couponsAmount = data.getAmount();
                }

                // 平台优惠券（实际金额由前端传输）
                goodsDiscountDTO.setDiscountAmountActual(couponsAmount);
                goodsDiscountDTO.setDiscountAmount(couponsAmount);
            }
            if (UserTicketSendEnum.MERCHANT_TICKET.getStatus().equals(data.getSendTicketParty())) {

                if (couponsAmountWithShop == null) {
                    couponsAmountWithShop = data.getAmount();
                }

                // 门店优惠券（实际金额由前端传输）
                goodsDiscountDTO.setDiscountAmountActual(couponsAmountWithShop);
                goodsDiscountDTO.setDiscountAmount(couponsAmountWithShop);
            }

            // 2、优惠券关联商品信息copy
            List<DiscountGoodsInfoDTO> goodsList = data.getGoodsItem().stream().map(e -> {
                DiscountGoodsInfoDTO item = new DiscountGoodsInfoDTO();
                BeanUtils.copyProperties(e, item);
                return item;
            }).collect(Collectors.toList());
            goodsDiscountDTO.setGoodsItem(goodsList);
            // 3、优惠券关联商品分类信息copy
            List<DiscountGoodsCategoryDTO> goodsCategoryItem = data.getGoodsCategoryItem().stream().map(e -> {
                DiscountGoodsCategoryDTO item = new DiscountGoodsCategoryDTO();
                BeanUtils.copyProperties(e, item);
                return item;
            }).collect(Collectors.toList());
            goodsDiscountDTO.setGoodsCategoryItem(goodsCategoryItem);
            goodsDiscountList.add(goodsDiscountDTO);
        }

        return goodsDiscountList;
    }

    /**
     * 功能描述: 订单结算返回信息转换，由于结算结果信息的字段太多，
     * 一是简化返回字段
     * 二是返回当前用户可使用和不可使用的优惠券集合
     *
     * @param param        结算的请求参数
     * @param calculateDTO 结算的结果信息
     * @return com.meiyuan.catering.order.dto.calculate.OrderCalculateInfoDTO
     * @author xie-xi-jie
     * @date 2020/5/21 11:15
     * @since v 1.1.0
     */
    public OrderCalculateInfoDTO orderCalculateInfoConver(OrderCalculateParamDTO param, OrderCalculateDTO calculateDTO) {
        Long shopId = param.getShopId();
        // 构建订单结算信息DTO
        OrderCalculateInfoDTO orderCalculateInfoDTO = new OrderCalculateInfoDTO();
        BeanUtils.copyProperties(calculateDTO, orderCalculateInfoDTO);
//        if (CalculateTypeEnum.BULK.getCode().equals(calculateDTO.getCalculateType())) {
//            // 团购订单不使用优惠券
//            return orderCalculateInfoDTO;
//        }
        // 用户所有的优惠券集合
        Result<List<UserTicketDetailsDTO>> listResult = this.userTicketClient.listUserAvailableTicket(param.getUserId());
        if (listResult.failure()) {
            throw new CustomException("获取优惠券信息失败");
        }

        List<UserTicketDetailsDTO> userTicketList = listResult.getData();
        if (CollectionUtils.isEmpty(userTicketList)) {
            // 用户没有优惠券
            return orderCalculateInfoDTO;
        }
        //订单商品信息
        List<OrdersCalculateGoodsDTO> goodsList = calculateDTO.getGoodsList();
        // 过滤掉秒杀和团购商品后的商品集合（秒杀和团购商品不参与优惠券，特价也不参与优惠券）
        List<OrdersCalculateGoodsDTO> calculateGoods = goodsList.stream()
                .filter(obj -> !OrderGoodsTypeEnum.SECONDS.getValue().equals(obj.getGoodsType()))
                .filter(obj -> !OrderGoodsTypeEnum.BULK.getValue().equals(obj.getGoodsType()))
                .filter(obj -> !OrderGoodsTypeEnum.SPECIAL.getValue().equals(obj.getGoodsType()))
                .collect(Collectors.toList());

        orderCalculateInfoDTO.setTicketWithPlatform(Lists.newArrayList());
        orderCalculateInfoDTO.setTicketWithShop(Lists.newArrayList());


        // 不可使用的优惠券集合
        List<NotCanUseTicketDTO> notCanUseTicket = new ArrayList<>();
        orderCalculateInfoDTO.setNotCanUseTicket(notCanUseTicket);
        if (CollectionUtils.isEmpty(calculateGoods)) {
            // 所有优惠券都不可用
            notCanUseTicket = BaseUtil.objToObj(userTicketList, NotCanUseTicketDTO.class);
            for (NotCanUseTicketDTO dto : notCanUseTicket) {
                dto.setCanUsed(Boolean.FALSE);
                isTicketOnlyCanUseWithShop(shopId, dto);
                setTicketIsAllGoodsCanUse(dto);
                setTicketUseRemark(dto, "不可与秒杀商品、团购商品、特价商品活动优惠同时享受");
                setTicketCanNotUseRemark(dto, "不可与秒杀商品、团购商品、特价商品活动优惠同时享受");
            }
            setTicketWithPlatformAndShop(orderCalculateInfoDTO.getTicketWithPlatform(), orderCalculateInfoDTO.getTicketWithShop(),
                    BaseUtil.objToObj(notCanUseTicket, UseTicketDTO.class), calculateGoods);
            return orderCalculateInfoDTO;
        }
        // 可使用的优惠券集合
        List<CanUseTicketDTO> canUseTicket = new ArrayList<>();
        Map<Long/* ticket_id */, UserTicketDetailsDTO> userTicketMap = userTicketList
                .stream()
                .collect(Collectors.toMap(UserTicketDetailsDTO::getUserTicketId, i -> i));

        BigDecimal haveReducePriceWithTicket = BigDecimal.ZERO;
        if (param.getCouponsId() != null && userTicketMap.containsKey(param.getCouponsId())) {
            haveReducePriceWithTicket = haveReducePriceWithTicket.add(userTicketMap.get(param.getCouponsId()).getAmount());
        }
        if (param.getCouponsWithShopId() != null && userTicketMap.containsKey(param.getCouponsWithShopId())) {
            haveReducePriceWithTicket = haveReducePriceWithTicket.add(userTicketMap.get(param.getCouponsWithShopId()).getAmount());
        }

        for (UserTicketDetailsDTO ticketDetailsDTO : userTicketList) {
            // 优惠券已使用，设置不可用
            handleTicket(param, goodsList, calculateGoods, notCanUseTicket, canUseTicket, userTicketMap, haveReducePriceWithTicket, ticketDetailsDTO);
        }
        orderCalculateInfoDTO.setCanUseTicket(canUseTicket);
        BigDecimal deliveryPrice = calculateDTO.getDeliveryPrice() != null ? calculateDTO.getDeliveryPrice() : BigDecimal.ZERO;
        BigDecimal packPrice = calculateDTO.getPackPrice() != null && BigDecimal.ZERO.compareTo(calculateDTO.getPackPrice()) < 0 ? calculateDTO.getPackPrice():BigDecimal.ZERO;
        BigDecimal notOrdinaryGoodsDiscountFee = calculateDTO.getNotOrdinaryGoodsDiscountFee() != null ? calculateDTO.getNotOrdinaryGoodsDiscountFee():BigDecimal.ZERO;
        BigDecimal orderAmount = calculateDTO.getOrderAmount().subtract(deliveryPrice).subtract(packPrice).subtract(notOrdinaryGoodsDiscountFee);
        //如果使用了优惠券后，订单金额【不包含配送费的金额】为0了，将另一个平台的优惠券全部设置为不可用。
        if(BigDecimal.ZERO.compareTo(orderAmount) == 0){
            List<CanUseTicketDTO> canUseTicketTemp = orderCalculateInfoDTO.getCanUseTicket();
            if(BaseUtil.judgeList(canUseTicketTemp)){
                Map<Integer/*sendTicketParty*/, List<CanUseTicketDTO>> sendTicketPartMap = canUseTicketTemp.stream().collect(Collectors.groupingBy(CanUseTicketDTO::getSendTicketParty));
                List<CanUseTicketDTO> brandTicketDTOS = sendTicketPartMap.get(MarketingTicketSendTicketPartyEnum.BRAND.getStatus());
                List<CanUseTicketDTO> platformTicketDTOS = sendTicketPartMap.get(MarketingTicketSendTicketPartyEnum.PLATFORM.getStatus());
                if(Objects.nonNull(param.getCouponsId()) && Objects.isNull(param.getCouponsWithShopId())){
                    //平台优惠券
                    List<NotCanUseTicketDTO> notCanUseTicketTemp = setTicketNotCanUse(brandTicketDTOS);
                    orderCalculateInfoDTO.getNotCanUseTicket().addAll(notCanUseTicketTemp);
                    orderCalculateInfoDTO.setCanUseTicket(platformTicketDTOS);
                }else if(Objects.nonNull(param.getCouponsWithShopId()) && Objects.isNull(param.getCouponsId())){
                    //商家优惠券
                    List<NotCanUseTicketDTO> notCanUseTicketDTOS = setTicketNotCanUse(platformTicketDTOS);
                    orderCalculateInfoDTO.getNotCanUseTicket().addAll(notCanUseTicketDTOS);
                    orderCalculateInfoDTO.setCanUseTicket(brandTicketDTOS);
                }else {
                    //平台优惠券、商家优惠券都有
                    List<CanUseTicketDTO> canUseTickets = canUseTicketTemp.stream().filter(userTicket -> userTicket.getUserTicketId().equals(param.getCouponsWithShopId()) || userTicket.getUserTicketId().equals(param.getCouponsId())).collect(Collectors.toList());
                    List<CanUseTicketDTO> canNotUseTicketDTOList = canUseTicketTemp;
                    if(BaseUtil.judgeList(canUseTickets)){
                        canNotUseTicketDTOList = canUseTicketTemp.stream().filter(userTicket -> !canUseTickets.stream().map(CanUseTicketDTO::getUserTicketId).collect(Collectors.toList()).contains(userTicket.getUserTicketId())).collect(Collectors.toList());
                    }
                    List<NotCanUseTicketDTO> notCanUseTicketDTOS = setTicketNotCanUse(canNotUseTicketDTOList);
                    orderCalculateInfoDTO.setNotCanUseTicket(notCanUseTicketDTOS);
                    orderCalculateInfoDTO.setCanUseTicket(canUseTickets);
                }
            }
        }

        // 用户优惠券拆分到平台优惠券列表和门店优惠券列表
        setTicketWithPlatformAndShop(orderCalculateInfoDTO.getTicketWithPlatform(), orderCalculateInfoDTO.getTicketWithShop(),
                BaseUtil.objToObj(orderCalculateInfoDTO.getCanUseTicket(), UseTicketDTO.class), calculateGoods);

        setTicketWithPlatformAndShop(orderCalculateInfoDTO.getTicketWithPlatform(), orderCalculateInfoDTO.getTicketWithShop(),
                BaseUtil.objToObj(orderCalculateInfoDTO.getNotCanUseTicket(), UseTicketDTO.class), calculateGoods);
        return orderCalculateInfoDTO;
    }


    /**
     * 设置可用卡券为不可用
     *
     * @param ticketDTOList 可用卡券集合
     * @return List<NotCanUseTicketDTO>
     * @author lh
     * @date 14:41 2020/10/19
     */
    private List<NotCanUseTicketDTO> setTicketNotCanUse(List<CanUseTicketDTO> ticketDTOList){
        List<NotCanUseTicketDTO> notCanUseTicketDTOS = Lists.newArrayList();
        if(BaseUtil.judgeList(ticketDTOList)){
            for (CanUseTicketDTO canUseTicketDTO:ticketDTOList) {
                NotCanUseTicketDTO notCanUseTicketDTO = new NotCanUseTicketDTO();
                notCanUseTicketDTO = BaseUtil.objToObj(canUseTicketDTO,NotCanUseTicketDTO.class);
                notCanUseTicketDTO.setCanUsed(Boolean.FALSE);
                setTicketCanNotUseRemark(notCanUseTicketDTO,"订单金额不满足~");
                notCanUseTicketDTOS.add(notCanUseTicketDTO);
            }
        }
        return notCanUseTicketDTOS;
    }

    private void handleTicket(OrderCalculateParamDTO param, List<OrdersCalculateGoodsDTO> goodsList, List<OrdersCalculateGoodsDTO> calculateGoods, List<NotCanUseTicketDTO> notCanUseTicket, List<CanUseTicketDTO> canUseTicket, Map<Long, UserTicketDetailsDTO> userTicketMap, BigDecimal haveReducePriceWithTicket, UserTicketDetailsDTO ticketDetailsDTO) {
        Long shopId = param.getShopId();
        NotCanUseTicketDTO notCanUseTicketDTO = new NotCanUseTicketDTO();
        BeanUtils.copyProperties(ticketDetailsDTO, notCanUseTicketDTO);
        setTicketIsAllGoodsCanUse(notCanUseTicketDTO);

        if (isTicketOnlyCanUseWithShop(shopId, notCanUseTicketDTO)) {
            notCanUseTicket.add(notCanUseTicketDTO);
            return;
        }
        if (ticketDetailsDTO.getUsed()) {
            notCanUseTicketDTO.setCanUsed(false);
            setTicketCanNotUseRemark(notCanUseTicketDTO, "优惠券已使用，不可重复使用");
            notCanUseTicket.add(notCanUseTicketDTO);
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        // 优惠券不在有效使用范围，设置不可用
        if (!(now.isAfter(ticketDetailsDTO.getUseBeginTime()) && now.isBefore(ticketDetailsDTO.getUseEndTime()))) {
            notCanUseTicketDTO.setCanUsed(false);
            setTicketCanNotUseRemark(notCanUseTicketDTO, "未到券的使用时间");
            notCanUseTicket.add(notCanUseTicketDTO);
            return;
        }
        // 选择了平台优惠券
        if (param.getCouponsId() != null) {
            if (MarketingTicketSendTicketPartyEnum.BRAND.getStatus().compareTo(ticketDetailsDTO.getSendTicketParty()) == 0
                    && ticketDetailsDTO.getExclusion()) {
                // 门店券，并且与平台全互斥
                notCanUseTicketDTO.setCanUsed(false);
                setTicketCanNotUseRemark(notCanUseTicketDTO, "门店互斥券");
                notCanUseTicket.add(notCanUseTicketDTO);
                return;
            }
        }
        // 选择了门店优惠券
        if (param.getCouponsWithShopId() != null) {
            UserTicketDetailsDTO dto = userTicketMap.get(param.getCouponsWithShopId());
            if (MarketingTicketSendTicketPartyEnum.PLATFORM.getStatus().compareTo(ticketDetailsDTO.getSendTicketParty()) == 0
                    && dto.getExclusion()) {
                // 平台券，并且与门店券互斥
                notCanUseTicketDTO.setCanUsed(false);
                // 与门店券互斥
                setTicketCanNotUseRemark(notCanUseTicketDTO, "平台互斥券");
                notCanUseTicket.add(notCanUseTicketDTO);
                return;
            }
        }
        //（满减券是否可用判断）
        // 如果是订单优惠,并且未使用
        if (MarketingTicketUsefulConditionEnum.ORDER_TICKET.getStatus().equals(ticketDetailsDTO.getUsefulCondition())) {
            // 处理针对订单可使用的优惠券
            this.marketingOrderHandle(ticketDetailsDTO, calculateGoods, canUseTicket, notCanUseTicket, haveReducePriceWithTicket);
            return;
        }
        // 商品优惠
        if (MarketingGoodsLimitEnum.ALL.getStatus().equals(ticketDetailsDTO.getGoodsLimit())) {
            // 处理针对全部商品可使用的优惠卷
            this.marketingOrderHandle(ticketDetailsDTO, calculateGoods, canUseTicket, notCanUseTicket, haveReducePriceWithTicket);
        } else if (MarketingGoodsLimitEnum.GOODS.getStatus().equals(ticketDetailsDTO.getGoodsLimit())) {
            // 指定商品集合优惠卷
            this.marketingGoodsHandle(ticketDetailsDTO, goodsList, canUseTicket, notCanUseTicket, haveReducePriceWithTicket);
        } else {
            // 指定商品分类集合优惠卷
            this.marketingGoodsCategoryHandle(ticketDetailsDTO, calculateGoods, canUseTicket, notCanUseTicket, haveReducePriceWithTicket);
        }
    }

    /**
     * 用户优惠券拆分到平台优惠券列表和门店优惠券列表
     * lh v1.3.0
     *
     * @param ticketListWithPlatform 平台优惠券列表
     * @param ticketListWithShop     门店优惠券列表
     * @param userTicketList         用户优惠券列表
     * @param goodsList              订单非团购／秒杀商品集合（优惠券如果是指定商品／商品分类可用，需要返回订单里面可用商品ID集合）
     */
    private void setTicketWithPlatformAndShop(
            List<UseTicketDTO> ticketListWithPlatform,
            List<UseTicketDTO> ticketListWithShop,
            List<UseTicketDTO> userTicketList,
            List<OrdersCalculateGoodsDTO> goodsList) {
        if (CollectionUtils.isEmpty(userTicketList)) {
            return;
        }

        // 订单商品ID（过滤团购／秒杀／特价）集合
        List<Long> goodsIdList = goodsList.stream().map(OrdersCalculateGoodsDTO::getGoodsId).collect(Collectors.toList());

        for (UseTicketDTO dto : userTicketList) {
            if (CollectionUtils.isEmpty(dto.getGoodsIdList())) {
                dto.setGoodsIdList(Lists.newArrayList());
            }
            if (MarketingGoodsLimitEnum.ALL.getStatus().compareTo(dto.getGoodsLimit()) == 0) {
                // 所有商品可用
                dto.setGoodsIdList(Lists.newArrayList());
            } else if (MarketingGoodsLimitEnum.GOODS.getStatus().compareTo(dto.getGoodsLimit()) == 0) {
                // 指定商品可用
                List<MarketingGoodsInfoDTO> goodsItem = dto.getGoodsItem();
                if (CollectionUtils.isNotEmpty(goodsItem)) {
                    // 优惠券适用的商品ID集合
                    List<Long> goodsIdListWithTicket = goodsItem.stream().map(MarketingGoodsInfoDTO::getGoodsId).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(goodsIdListWithTicket)) {
                        // 商品ID集合同时在优惠券适用商品与订单商品
                        List<Long> goodsIdListInTicketAndOrderGoods = goodsIdList.stream().filter(i ->
                                goodsIdListWithTicket.contains(i)
                        ).collect(Collectors.toList());
                        dto.setGoodsIdList(goodsIdListInTicketAndOrderGoods);
                    }
                }
            } else if (MarketingGoodsLimitEnum.GOODS_TYPE.getStatus().compareTo(dto.getGoodsLimit()) == 0) {
                // 指定商品分类可用
                List<MarketingGoodsCategoryAddDTO> goodsCategoryItem = dto.getGoodsCategoryItem();
                if (CollectionUtils.isNotEmpty(goodsCategoryItem)) {
                    List<Long> goodsCategoryIdListWithTicket = goodsCategoryItem
                            .stream()
                            .map(MarketingGoodsCategoryAddDTO::getGoodsCategoryId)
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(goodsCategoryIdListWithTicket)) {
                        List<OrdersCalculateGoodsDTO> goodsListWithTicketAndOrderGoods = goodsList
                                .stream()
                                .filter(i -> goodsCategoryIdListWithTicket
                                        .contains(i.getCategoryId()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(goodsListWithTicketAndOrderGoods)) {
                            dto.setGoodsIdList(goodsListWithTicketAndOrderGoods
                                    .stream()
                                    .map(OrdersCalculateGoodsDTO::getGoodsId)
                                    .collect(Collectors.toList()));
                        }
                    }
                }
            }

            if (MarketingTicketSendTicketPartyEnum.PLATFORM.getStatus().equals(dto.getSendTicketParty())) {
                // 平台优惠券
                ticketListWithPlatform.add(dto);
                continue;
            }
            if (MarketingTicketSendTicketPartyEnum.BRAND.getStatus().equals(dto.getSendTicketParty())) {
                // 商家优惠券
                ticketListWithShop.add(dto);

            }
        }
    }

    /**
     * 优惠券是否仅适用于指定门店
     *
     * @param shopId
     * @param dto
     * @return
     */
    private Boolean isTicketOnlyCanUseWithShop(Long shopId, UseTicketDTO dto) {

        if (CollectionUtils.isEmpty(dto.getShopList())) {
            return Boolean.FALSE;
        }
        setTicketUseRemark(dto, "仅用于指定商家");

        if (!dto.getShopList().contains(shopId)) {
            dto.setCanUsed(Boolean.FALSE);
            // 指定商家可用，该商家不包含在内
            setTicketCanNotUseRemark(dto, "仅用于指定商家");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 设置优惠券使用备注
     * lh v1.3.0
     *
     * @param dto             优惠券对象（可用／不可用）
     * @param ticketUseRemark 使用说明
     */
    private void setTicketUseRemark(UseTicketDTO dto, String ticketUseRemark) {
        // 使用说明
        List<String> useRemarkList = dto.getUseRemarkList();
        if (CollectionUtils.isEmpty(useRemarkList)) {
            useRemarkList = Lists.newArrayList();
        }
        if (useRemarkList.contains(ticketUseRemark)) {
            return;
        }
        useRemarkList.add(ticketUseRemark);
        dto.setUseRemarkList(useRemarkList);
    }

    /**
     * 设置优惠券不可使用备注
     * lh v1.3.0
     *
     * @param dto             优惠券对象（可用／不可用）
     * @param ticketUseRemark 使用说明
     */
    private void setTicketCanNotUseRemark(UseTicketDTO dto, String ticketUseRemark) {
        // 不可用
        List<String> list = dto.getCanNotUseRemarkList();
        if (CollectionUtils.isEmpty(list)) {
            list = Lists.newArrayList();
        }
        list.add(ticketUseRemark);
        dto.setCanNotUseRemarkList(list);
    }

    /**
     * 功能描述: 计算【订单优惠卷】、【全部商品优惠卷】是否可以使用
     *
     * @param dto                       用户优惠卷信息
     * @param collect                   结算的商品信息
     * @param canUseTicket              可以使用的优惠券集合
     * @param notCanUseTicket           不可使用的优惠券集合
     * @param haveReducePriceWithTicket 优惠券已优惠金额 v1.3.0 lh 优惠券支持多张，用来处理已经使用过优惠券，剩余金额优惠券是否可用情况
     * @return: void
     */
    public void marketingOrderHandle(
            UserTicketDetailsDTO dto,
            List<OrdersCalculateGoodsDTO> collect,
            List<CanUseTicketDTO> canUseTicket,
            List<NotCanUseTicketDTO> notCanUseTicket,
            BigDecimal haveReducePriceWithTicket) {

        // 1、订单商品金额和
        BigDecimal orderGoodsTotalPrice = collect
                .stream()
                .map(OrdersCalculateGoodsDTO::getGoodsTotalPrice)
                .reduce(BigDecimal::add)
                .get();
        // 满减金额-订单商品金额之和
        BigDecimal subtract = dto.getConsumeCondition().subtract(orderGoodsTotalPrice);


        // 2、可参与的优惠的订单金额大于消费限制条件金额，则该优惠卷可使用
        String ticketUseRemark = "满" + dto.getConsumeCondition().setScale(2, BigDecimal.ROUND_HALF_UP) + "元可用";
        if (dto.getConsumeCondition().compareTo(BigDecimal.ZERO) == 0) {
            ticketUseRemark = "无门槛券";
        }
        if (subtract.compareTo(BigDecimal.ZERO) > 0) {
            // 优惠券不可用
            NotCanUseTicketDTO notCanUseTicketDTO = new NotCanUseTicketDTO();
            BeanUtils.copyProperties(dto, notCanUseTicketDTO);
            notCanUseTicketDTO.setCanUsed(false);
            setTicketUseRemark(notCanUseTicketDTO, ticketUseRemark);
            if (dto.getConsumeCondition().compareTo(BigDecimal.ZERO) > 0) {
                setTicketCanNotUseRemark(notCanUseTicketDTO,
                        "可用商品现价不满"
                                + dto.getConsumeCondition().setScale(2, BigDecimal.ROUND_HALF_UP)
                                + "元");
            }
            setTicketIsAllGoodsCanUse(notCanUseTicketDTO);
            notCanUseTicket.add(notCanUseTicketDTO);
        } else {
//            优惠券可用
            CanUseTicketDTO canUseTicketDTO = new CanUseTicketDTO();
            BeanUtils.copyProperties(dto, canUseTicketDTO);
            canUseTicketDTO.setCanUsed(true);
            setTicketUseRemark(canUseTicketDTO, ticketUseRemark);
            setTicketIsAllGoodsCanUse(canUseTicketDTO);
            canUseTicket.add(canUseTicketDTO);
        }
    }

    /**
     * 根据优惠券是否适用所有商品设置适用备注
     * lh v1.3.0
     *
     * @param useTicketDTO 优惠券对象
     */
    private void setTicketIsAllGoodsCanUse(UseTicketDTO useTicketDTO) {
        if (MarketingGoodsLimitEnum.ALL.getStatus().equals(useTicketDTO.getGoodsLimit())) {
            // 全部商品可用
            setTicketUseRemark(useTicketDTO, "全部商品可用");
        } else {
            // 全部商品可用
            setTicketUseRemark(useTicketDTO, "指定商品可用");
        }
        if (MarketingTicketSendTicketPartyEnum.BRAND.getStatus().compareTo(useTicketDTO.getSendTicketParty()) == 0
                && useTicketDTO.getExclusion()) {
            // 平台互斥券
            setTicketUseRemark(useTicketDTO, "平台互斥券");
        }
    }

    /**
     * 功能描述: 计算【指定商品优惠卷】是否可以使用
     *
     * @param detailsDTO                用户优惠卷信息
     * @param goodsList                 结算的商品信息
     * @param canUseTicket              可以使用的优惠券集合
     * @param notCanUseTicket           不可使用的优惠券集合
     * @param haveReducePriceWithTicket 已优惠金额
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 11:20
     * @since v 1.1.0
     */
    public void marketingGoodsHandle(
            UserTicketDetailsDTO detailsDTO,
            List<OrdersCalculateGoodsDTO> goodsList,
            List<CanUseTicketDTO> canUseTicket,
            List<NotCanUseTicketDTO> notCanUseTicket,
            BigDecimal haveReducePriceWithTicket) {

        // 1、优惠卷指定商品集合
        List<MarketingGoodsInfoDTO> goodsItem = detailsDTO.getGoodsItem();
        if (goodsItem == null || goodsItem.size() == 0) {
            throw new CustomException("获取优惠券关联商品信息失败");
        }

        // 2、优惠卷指定商品集合分组信息，根据商品ID分组
        Map<Long, List<MarketingGoodsInfoDTO>> goodsCollect = goodsItem.stream()
                .collect(Collectors.groupingBy(MarketingGoodsInfoDTO::getGoodsId));
        if (goodsCollect == null || goodsCollect.size() == 0) {
            throw new CustomException("获取优惠券关联商品信息失败");
        }

        // 3、结算商品分组信息，根据商品ID分组
        Map<Long, List<OrdersCalculateGoodsDTO>> calculateGoodsMap = goodsList.stream()
                .filter(obj -> !OrderGoodsTypeEnum.SECONDS.getValue().equals(obj.getGoodsType()))
                .collect(Collectors.groupingBy(OrdersCalculateGoodsDTO::getGoodsId));

        // 4、可以用优惠的所有商品金额
        BigDecimal goodsAmountTotal = BigDecimal.ZERO;
        for (Map.Entry<Long, List<OrdersCalculateGoodsDTO>> entry : calculateGoodsMap.entrySet()) {
            Long goodsId = entry.getKey();
            List<OrdersCalculateGoodsDTO> goodsCalculateList = entry.getValue();
            // 优惠卷指定商品使用
            if (goodsCollect.get(goodsId) != null) {
                // 可以用优惠的商品金额
                BigDecimal goodsAmount = goodsCalculateList.stream().map(obj -> {
                    return obj.getGoodsTotalPrice();
                }).reduce((x, y) -> x.add(y)).get();
                goodsAmountTotal = goodsAmountTotal.add(goodsAmount);
            }
        }

        // 5、消费限制条件:满多少元可使用-1：不限制

        this.canUseTicket(detailsDTO, canUseTicket, notCanUseTicket, goodsAmountTotal);
    }

    /**
     * 功能描述: 计算【指定商品分类优惠卷】是否可以使用
     *
     * @param detailsDTO                用户优惠卷信息
     * @param calculateGoods            结算的商品信息
     * @param canUseTicket              可以使用的优惠券集合
     * @param notCanUseTicket           不可使用的优惠券集合
     * @param haveReducePriceWithTicket 已优惠金额
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 11:20
     * @since v 1.1.0
     */
    public void marketingGoodsCategoryHandle(
            UserTicketDetailsDTO detailsDTO,
            List<OrdersCalculateGoodsDTO> calculateGoods,
            List<CanUseTicketDTO> canUseTicket,
            List<NotCanUseTicketDTO> notCanUseTicket,
            BigDecimal haveReducePriceWithTicket) {

        List<MarketingGoodsCategoryAddDTO> goodsCategoryItem = detailsDTO.getGoodsCategoryItem();

        // 1、优惠卷指定商品分类使用
        if (CollectionUtils.isEmpty(goodsCategoryItem)) {
            throw new CustomException("获取优惠券关联商品分类信息失败");
        }

        // 2、优惠卷指定商品分类集合分组信息，根据商品分类ID分组
        Map<Long/* 优惠券适用商品所属分类ID */, List<MarketingGoodsCategoryAddDTO>/* 优惠券适用商品所属分类集合 */> goodsCategoryCollect = goodsCategoryItem.stream()
                .collect(Collectors.groupingBy(MarketingGoodsCategoryAddDTO::getGoodsCategoryId));
        if (goodsCategoryCollect == null || goodsCategoryCollect.size() == 0) {
            throw new CustomException("获取优惠券关联商品分类信息失败");
        }


        //        根据平台分类批量查询分类下平台商品goodsId
        List<Long> marketCategoryIdList = goodsCategoryItem.stream().map(MarketingGoodsCategoryAddDTO::getGoodsCategoryId).collect(Collectors.toList());
        Result<List<GoodsListDTO>> goodsListResult = goodsClient.listByCategoryIdList(marketCategoryIdList);
//        可使用优惠券商品集合
        List<GoodsListDTO> goodsWithTicketList = goodsListResult.getData();
        if (CollectionUtils.isEmpty(goodsWithTicketList)) {
            return;
        }
//        可用优惠券商品ID集合
        List<Long> goodsIdWithTicketList = goodsWithTicketList.stream().map(GoodsListDTO::getGoodsId).collect(Collectors.toList());

        // 5、可以用优惠的所有商品金额
        BigDecimal goodsAmountTotal = BigDecimal.ZERO;

        for (OrdersCalculateGoodsDTO orderGoods : calculateGoods) {
            if (!goodsIdWithTicketList.contains(orderGoods.getGoodsId())) {
                continue;
            }
            // 消费限制条件:满多少元可使用 -1：不限制
            if (detailsDTO.getConsumeCondition().compareTo(BigDecimal.ZERO) >= 0) {
                goodsAmountTotal = goodsAmountTotal.add(orderGoods.getGoodsTotalPrice());
            }
        }
        // 6、消费限制条件:满多少元可使用 -1：不限制


        this.canUseTicket(detailsDTO, canUseTicket, notCanUseTicket, goodsAmountTotal);
    }

    /**
     * 功能描述: 判断商品金额是否满足使用优惠卷
     *
     * @param detailsDTO       用户优惠卷信息
     * @param canUseTicket     可以使用的优惠券集合
     * @param notCanUseTicket  不可使用的优惠券集合
     * @param goodsAmountTotal 可使用优惠的分类下所有商品总金额
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 11:20
     * @since v 1.1.0
     */
    private void canUseTicket(
            UserTicketDetailsDTO detailsDTO,
            List<CanUseTicketDTO> canUseTicket,
            List<NotCanUseTicketDTO> notCanUseTicket,
            BigDecimal goodsAmountTotal) {

        Boolean canUsed = true;
        NotCanUseTicketDTO notCanUseTicketDTO = new NotCanUseTicketDTO();
        CanUseTicketDTO canUseTicketDTO = new CanUseTicketDTO();

        if (goodsAmountTotal.equals(BigDecimal.ZERO)) {
            // 可优惠金额为0，则不可优惠
            canUsed = Boolean.FALSE;
        } else {
            // 消费限制条件:满多少元可使用 -1：不限制
            if (detailsDTO.getConsumeCondition().compareTo(BigDecimal.ZERO) > 0) {
                if (goodsAmountTotal.compareTo(detailsDTO.getConsumeCondition()) < 0) {
                    canUsed = false;
                }
            }
        }
        String ticketUseRemark = "满" + detailsDTO.getConsumeCondition().setScale(2, BigDecimal.ROUND_HALF_UP) + "元可用";
        if (detailsDTO.getConsumeCondition().compareTo(BigDecimal.ZERO) == 0) {
            ticketUseRemark = "无门槛券";
        }
        if (canUsed) {
            BeanUtils.copyProperties(detailsDTO, canUseTicketDTO);
            setTicketUseRemark(canUseTicketDTO, "指定商品可用");
            canUseTicketDTO.setCanUsed(canUsed);
            setTicketUseRemark(canUseTicketDTO, ticketUseRemark);
            setTicketIsAllGoodsCanUse(canUseTicketDTO);
            canUseTicket.add(canUseTicketDTO);
        } else {
            BeanUtils.copyProperties(detailsDTO, notCanUseTicketDTO);
            setTicketUseRemark(notCanUseTicketDTO, "指定商品可用");
            setTicketCanNotUseRemark(notCanUseTicketDTO, "指定商品可用");
            notCanUseTicketDTO.setCanUsed(canUsed);
            setTicketIsAllGoodsCanUse(notCanUseTicketDTO);
            setTicketUseRemark(canUseTicketDTO, ticketUseRemark);
            if (detailsDTO.getConsumeCondition().compareTo(BigDecimal.ZERO) > 0) {
                setTicketCanNotUseRemark(notCanUseTicketDTO,
                        "可用商品现价不满" + detailsDTO.getConsumeCondition().setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
            }
            notCanUseTicket.add(notCanUseTicketDTO);
        }
    }

    /**
     * 功能描述: [选中优惠券结算的时候]计算当前商品是否可使用【订单优惠卷】、【全部商品优惠卷】
     *
     * @param ordersCalculateDiscountDTO 选择的优惠券
     * @param goodsList                  结算的商品信息
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 11:20
     * @since v 1.1.0
     */
    public void orderDiscount(OrdersCalculateDiscountDTO ordersCalculateDiscountDTO, List<OrdersCalculateGoodsDTO> goodsList) {
        // 普通商品
        List<OrdersCalculateGoodsDTO> ordinary = goodsList.stream().filter(obj ->
                !OrderGoodsTypeEnum.SECONDS.getValue().equals(obj.getGoodsType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ordinary)) {
            throw new CustomException("没有可用优惠券的商品");
        }
        // 消费限制条件:满多少元可使用-1：不限制
        if (ordersCalculateDiscountDTO.getConsumeCondition().compareTo(BigDecimal.ZERO) < 0) {
            // 没有使用门槛
            return;
        }
        // 可以用优惠的商品金额（这里用total_price不能用数量乘以单价，存在限多少份优惠的情况）
        BigDecimal orderGoodsTotalPrice = ordinary
                .stream()
                .map(OrdersCalculateGoodsDTO::getGoodsTotalPrice)
                .reduce(BigDecimal::add)
                .get();

        BigDecimal subtract = ordersCalculateDiscountDTO.getConsumeCondition().subtract(orderGoodsTotalPrice);
        if (subtract.compareTo(BigDecimal.ZERO) > 0) {
            throw new CustomException("还差 " + subtract.abs().setScale(2) + " 元优惠券才可使用（不包含秒杀商品）");
        }
        // 优惠金额大于商品金额，则优惠金额取商品金额
        // 两张优惠券后，该逻辑更新为实际优惠金额由前端传输
//        if (ordersCalculateDiscountDTO.getDiscountAmount().compareTo(orderGoodsTotalPrice) > 0) {
//            ordersCalculateDiscountDTO.setDiscountAmountActual(orderGoodsTotalPrice);
//        }
    }

    /**
     * 功能描述: 优惠卷指定部分商品使用，验证当前商品是否可使用该优惠卷
     *
     * @param ordersCalculateDiscountDTO
     * @param goodsList
     * @return: void
     */
    public void partGoods(OrdersCalculateDiscountDTO ordersCalculateDiscountDTO, List<OrdersCalculateGoodsDTO> goodsList) {

        List<DiscountGoodsInfoDTO> goodsItem = ordersCalculateDiscountDTO.getGoodsItem();
        // 1、优惠卷指定商品使用
        if (CollectionUtils.isEmpty(goodsItem)) {
            throw new CustomException("获取优惠券关联商品信息失败");
        }

        // 2、优惠卷指定商品集合分组信息，根据商品ID分组
        Map<Long, List<DiscountGoodsInfoDTO>> goodsCollect = goodsItem.stream()
                .collect(Collectors.groupingBy(DiscountGoodsInfoDTO::getGoodsId));
        if (goodsCollect == null || goodsCollect.size() == 0) {
            throw new CustomException("获取优惠券关联商品信息失败");
        }

        // 3、结算商品分组信息，根据商品ID分组
        Map<Long, List<OrdersCalculateGoodsDTO>> calculateGoodsMap = goodsList.stream()
                .filter(obj -> !OrderGoodsTypeEnum.SECONDS.getValue().equals(obj.getGoodsType()))
                .collect(Collectors.groupingBy(OrdersCalculateGoodsDTO::getGoodsId));

        // 4、可以用优惠的所有商品金额
        BigDecimal goodsAmountTotal = BigDecimal.ZERO;
        for (Map.Entry<Long, List<OrdersCalculateGoodsDTO>> entry : calculateGoodsMap.entrySet()) {
            Long goodsId = entry.getKey();
            List<OrdersCalculateGoodsDTO> goodsCalculateList = entry.getValue();
            // 优惠卷指定商品使用
            if (goodsCollect.get(goodsId) != null) {
                // 可以用优惠的单个商品金额
                BigDecimal goodsAmount = goodsAmountCalculate(goodsCalculateList);
                goodsAmountTotal = goodsAmountTotal.add(goodsAmount);
            }
        }

        // 5、消费限制条件:满多少元可使用-1：不限制
        if (ordersCalculateDiscountDTO.getConsumeCondition().compareTo(BigDecimal.ZERO) >= 0) {
            // 优惠卷条件金额 - 指定商品合计金额
            BigDecimal subtract = NumberUtils.setScale(ordersCalculateDiscountDTO.getConsumeCondition().subtract(goodsAmountTotal));
            if (subtract.compareTo(BigDecimal.ZERO) > 0) {
                throw new CustomException("还差 " + subtract + " 元才可使用优惠券");
            }
        }
        // 优惠金额大于商品金额，则优惠金额取商品金额
        // 两张优惠券后，该逻辑更新为实际优惠金额由前端传输
//        if (ordersCalculateDiscountDTO.getDiscountAmount().compareTo(goodsAmountTotal) > 0) {
//            ordersCalculateDiscountDTO.setDiscountAmountActual(goodsAmountTotal);
//        }
    }

    /**
     * 功能描述: 优惠卷指定商品分类集合，验证当前商品是否可使用该优惠卷
     *
     * @param ordersCalculateDiscountDTO
     * @param goodsList
     */
    public void goodsCategory(OrdersCalculateDiscountDTO ordersCalculateDiscountDTO, List<OrdersCalculateGoodsDTO> goodsList) {
        List<DiscountGoodsCategoryDTO> goodsCategoryItem = ordersCalculateDiscountDTO.getGoodsCategoryItem();
        // 1、优惠卷指定商品分类使用
        if (goodsCategoryItem == null || goodsCategoryItem.size() == 0) {
            log.error("获取优惠券指定商品分类信息失败 goodsCategoryItem = 【{}】", goodsCategoryItem);
            throw new CustomException("获取优惠券关联商品分类信息失败");
        }

        List<Long> categoryIdWithTicketList = goodsCategoryItem.stream().map(DiscountGoodsCategoryDTO::getGoodsCategoryId).collect(Collectors.toList());
        Result<List<GoodsListDTO>> goodsWithTicketListRet = goodsClient.listByCategoryIdList(categoryIdWithTicketList);
        List<GoodsListDTO> goodsWithTicketList = goodsWithTicketListRet.getData();
        if (CollectionUtils.isEmpty(goodsWithTicketList)) {
            throw new CustomException("获取优惠券指定分类下的商品失败");
        }
        List<Long> goodsIdWithTicketList = goodsWithTicketList.stream().map(GoodsListDTO::getGoodsId).collect(Collectors.toList());

        // 可以用优惠的所有商品金额
        BigDecimal goodsAmountTotal = BigDecimal.ZERO;

        for (OrdersCalculateGoodsDTO orderGoods : goodsList) {
            if (!goodsIdWithTicketList.contains(orderGoods.getGoodsId())) {
                continue;
            }
            BigDecimal orderGoodsPrice = orderGoods.getSalesPrice();
            if (orderGoodsPrice.compareTo(BigDecimal.ZERO) <= 0) {
                orderGoodsPrice = orderGoods.getStorePrice();
            }
            goodsAmountTotal = goodsAmountTotal.add(orderGoodsPrice.multiply(BigDecimal.valueOf(orderGoods.getQuantity())));
        }
//        再次做校验，防止商品金额被修改后不满足优惠券满减条件
        if (goodsAmountTotal.compareTo(ordersCalculateDiscountDTO.getConsumeCondition()) < 0) {
            throw new CustomException("还差 " + ordersCalculateDiscountDTO.getConsumeCondition().subtract(goodsAmountTotal).setScale(2, BigDecimal.ROUND_HALF_UP) + " 元才可使用优惠券");
        }

        // 优惠金额大于商品金额，则优惠金额取商品金额
        // 两张优惠券后，该逻辑更新为实际优惠金额由前端传输
//        if (ordersCalculateDiscountDTO.getDiscountAmount().compareTo(goodsAmountTotal) > 0) {
//            ordersCalculateDiscountDTO.setDiscountAmountActual(goodsAmountTotal);
//        }
    }

    /**
     * 功能描述: 需要进行优惠得到商品金额计算
     *
     * @param goodsCalculateList
     * @return: void
     */
    private BigDecimal goodsAmountCalculate(List<OrdersCalculateGoodsDTO> goodsCalculateList) {
        // 可以用优惠的单个商品金额（这里用total_price，不能用数量乘以单价，存在限多少分优惠的情况）
        if (CollectionUtils.isEmpty(goodsCalculateList)) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalPrice = goodsCalculateList
                .stream()
                .map(OrdersCalculateGoodsDTO::getGoodsTotalPrice)
                .reduce(BigDecimal::add)
                .get();

        return totalPrice;
    }

    /**
     * 功能描述: 获取拼单人信息失败
     *
     * @param shareBillNo 拼单号
     * @return java.util.Map<java.lang.Long                                                               ,                                                                                                                               com.meiyuan.catering.user.dto.cart.CartShareBillUserDTO>
     * @author xie-xi-jie
     * @date 2020/5/21 10:21
     * @since v 1.1.0
     */
    public Map<Long, CartShareBillUserDTO> getShareBillUserMap(String shareBillNo) {
        Result<Map<Long, CartShareBillUserDTO>> mapResult = cartShareBillUserClient.shareBillUserMap(shareBillNo);
        if (mapResult.failure()) {
            log.error("获取拼单人信息失败 shareBillNo = 【{}】 ，mapResult = 【{}】", shareBillNo, mapResult);
            throw new CustomException("获取拼单人信息失败");
        }
        Map<Long, CartShareBillUserDTO> shareBillUserMap = mapResult.getData();
        if (shareBillUserMap == null) {
            log.error("获取拼单人信息失败 shareBillNo = 【{}】 ，shareBillUserMap = 【null】", shareBillNo);
            throw new CustomException("获取拼单人信息失败");
        }
        return shareBillUserMap;
    }


    /**
     * 秒杀和团购才会调用
     *
     * @param goodsId  秒杀和团购订单，订单商品表里面到goods_id存到是m_goods_id
     * @param typeEnum
     * @return
     */
    public MarketingGoodsSimpleInfoDTO getSimpleInfo(Long goodsId, MarketingOfTypeEnum typeEnum, BigDecimal pack) {
        // 获取活动信息
        EsMarketingDTO marketingDTO = ClientUtil.getDate(esGoodsClient.getMarketingGoodsById(goodsId));
//        Result<MarketingGoodsSimpleInfoDTO> result = this.marketingGoodsClient.getSimpleInfoById(goodsId);
//
//
//        if (result.failure()) {
//            // 模块调用失败
//            throw new CustomException(result.getCode(), result.getMsg());
//        }

//        Marketi-ngGoodsSimpleInfoDTO simpleInfo = result.getData();
        if (marketingDTO == null) {
            // 没有查询到商品信息，从数据库查询商品名称
            Result<String> ret = merchantGoodsClient.getGoodsNameFromDbByMgoodsId(goodsId);
            if (ret.failure()) {
                // 模块调用失败
                throw new CustomException(ret.getCode(), ret.getMsg());
            }
            throw new CustomException(ErrorCode.GROUPON_DOWN, ret.getData() + "商品已下架，请重新下单");
        }

        if (marketingDTO.getDel()) {
            throw new CustomException(ErrorCode.GROUPON_DOWN, marketingDTO.getGoodsName() + "商品已下架，请重新下单");
        }

        //团购活动已下架 / 已结束
        if (
                !LocalDateTime.now().isBefore(marketingDTO.getEndTime()) ||
                        marketingDTO.getUpDownState().compareTo(MarketingUpDownStatusEnum.DOWN.getStatus()) == 0
        ) {

            if (typeEnum.getStatus().equals(MarketingOfTypeEnum.GROUPON.getStatus())) {
                throw new CustomException(ErrorCode.GROUPON_DOWN, ErrorCode.GROUPON_DOWN_MSG);
            }

            if (typeEnum.getStatus().equals(MarketingOfTypeEnum.SECKILL.getStatus())) {
                throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, ErrorCode.SECKILL_DOWN_ERROR_MSG);
            }
        }

        if (!LocalDateTime.now().isBefore(marketingDTO.getEndTime())) {
            throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, "活动已结束");
        }

        //活动商品下架
        if (Objects.equals(marketingDTO.getGoodsUpDownState(), GoodsStatusEnum.LOWER_SHELF.getStatus())) {
            throw new CustomException(ErrorCode.GROUPON_DOWN, marketingDTO.getGoodsName() + "商品已下架，请重新下单");
        }

        // 销售渠道为堂食
        if (SaleChannelsEnum.TS.getStatus().equals(marketingDTO.getGoodsSalesChannels())) {
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, marketingDTO.getGoodsName() + "商品已下架，请重新下单");
        }

        if (pack != null) {
            BigDecimal packPrice = BaseUtil.isNullOrNegativeOne(marketingDTO.getPackPrice()) ? BigDecimal.ZERO : marketingDTO.getPackPrice();
            if (!BaseUtil.priceEquals(pack, packPrice)) {
                log.error("订单商品餐盒费发生变更，不能完成支付 marketingDTO =【{}】", marketingDTO);
                throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, marketingDTO.getGoodsName() + ",餐盒费发生变更");
            }
        }


        MarketingGoodsSimpleInfoDTO simpleInfoDTO = new MarketingGoodsSimpleInfoDTO();
        simpleInfoDTO.setActivityId(marketingDTO.getId());
        simpleInfoDTO.setActivityName(marketingDTO.getName());
        simpleInfoDTO.setMGoodsId(marketingDTO.getMGoodsId());
        simpleInfoDTO.setMinQuantity(marketingDTO.getMinQuantity());
        simpleInfoDTO.setActivityPrice(marketingDTO.getActivityPrice());
        simpleInfoDTO.setGoodsId(marketingDTO.getGoodsId());
        simpleInfoDTO.setGoodsName(marketingDTO.getGoodsName());
        simpleInfoDTO.setSku(marketingDTO.getSku());
        simpleInfoDTO.setActivityBeginTime(marketingDTO.getBeginTime());
        simpleInfoDTO.setActivityEndTime(marketingDTO.getEndTime());
        simpleInfoDTO.setUpDown(marketingDTO.getUpDownState());
        simpleInfoDTO.setGoodsStatus(marketingDTO.getGoodsUpDownState());
        simpleInfoDTO.setBusinessSupport(marketingDTO.getBusinessSupport());
        simpleInfoDTO.setStorePrice(marketingDTO.getStorePrice());
        simpleInfoDTO.setSkuValue(marketingDTO.getSkuValue());
        simpleInfoDTO.setInfoPicture(marketingDTO.getGoodsPicture());
        simpleInfoDTO.setPackPrice(marketingDTO.getPackPrice());
        simpleInfoDTO.setGoodsSpecType(marketingDTO.getGoodsSpecType());
        return simpleInfoDTO;
    }


    /**
     * 功能描述: 团购、秒杀商品校验
     *
     * @param simpleInfo 团购、秒杀活动信息
     * @param price      下单价格
     * @param typeEnum   活动类型
     * @return: void
     */
    protected EsGoodsDTO activityGoodsCheck(String shopId, MarketingGoodsSimpleInfoDTO simpleInfo, BigDecimal price, MarketingOfTypeEnum typeEnum) {

        // 已下验证 在get  simpleInfo 时已判断

        //        LocalDateTime now = LocalDateTime.now();
//        if (!now.isAfter(simpleInfo.getActivityBeginTime())) {
//            log.error("{}【MGoodsId：{}】活动未开始", typeEnum.getDesc(), simpleInfo.getMGoodsId());
//            throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, "活动未开始");
//        }
//
//        if (!now.isBefore(simpleInfo.getActivityEndTime())) {
//            log.error("{}【MGoodsId：{}】活动已结束，当前时间【{}】，活动结束时间【{}】", typeEnum.getDesc(),
//                    simpleInfo.getMGoodsId(), DateTimeUtil.getDateTimeDisplayString(now), DateTimeUtil.getDateTimeDisplayString(simpleInfo.getActivityEndTime()));
//            throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, "活动已结束");
//        }
//        if (MarketingUpDownStatusEnum.DOWN.getStatus().equals(simpleInfo.getUpDown())) {
//            log.error("{}【MGoodsId：{}】活动已下架", typeEnum.getDesc(), simpleInfo.getMGoodsId());
//            throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, "活动已下架");
//        }
//
//        if (!now.isBefore(simpleInfo.getActivityEndTime())) {
//            log.error("{}【MGoodsId：{}】活动已结束，当前时间【{}】，活动结束时间【{}】", typeEnum.getDesc(),
//                    simpleInfo.getMGoodsId(), DateTimeUtil.getDateTimeDisplayString(now), DateTimeUtil.getDateTimeDisplayString(simpleInfo.getActivityEndTime()));
//            throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, "活动已结束");
//        }
//        if (simpleInfo.getActivityPrice().compareTo(price) != 0) {
//            log.error("{}活动价格被修改，MGoodsId【{}】", typeEnum.getDesc(), simpleInfo.getMGoodsId());
//            throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, "活动价格被修改，不能进行结算");
//        }
//
////        从活动商品ES查询商品信息
//        Result<EsMarketingDTO> esMarketingDtoResult = this.esGoodsClient.getMarketingGoodsById(simpleInfo.getMGoodsId());
//        if (esMarketingDtoResult.failure()) {
//            throw new CustomException(ErrorCode.ORDER_MARKETING_GOODS_NOT_EXIST, ErrorCode.ORDER_MARKETING_GOODS_NOT_EXIST_MSG);
//        }
//        EsMarketingDTO data = esMarketingDtoResult.getData();
//        if (data == null) {
//            throw new CustomException(ErrorCode.ORDER_MARKETING_GOODS_NOT_EXIST, ErrorCode.ORDER_MARKETING_GOODS_NOT_EXIST_MSG);
//        }
//        //菜单移除商品
//        if (data.getDel()) {
//            throw new CustomException(ErrorCode.ORDER_MARKETING_GOODS_NOT_EXIST, data.getGoodsName() + "商品已下架，请重新下单");
//        }
        EsGoodsDTO esGoods = new EsGoodsDTO();
        // 商品活动信息
        esGoods.setMarketPrice(simpleInfo.getStorePrice());
        // 售价取活动加
        esGoods.setSalesPrice(simpleInfo.getActivityPrice());
        // 商品基础信息
        esGoods.setSkuCode(simpleInfo.getSku());
        esGoods.setSalesChannels(simpleInfo.getBusinessSupport());
        esGoods.setLowestBuy(simpleInfo.getMinQuantity());
        esGoods.setHighestBuy(simpleInfo.getLimitQuantity());
        esGoods.setGoodsId(String.valueOf(simpleInfo.getGoodsId()));
        esGoods.setGoodsName(simpleInfo.getGoodsName());
        esGoods.setShopId(simpleInfo.getShopId());
        esGoods.setShopName(simpleInfo.getShopName());
        esGoods.setInfoPicture(simpleInfo.getInfoPicture());
        esGoods.setGoodsLabel(simpleInfo.getGoodsLabel());
        esGoods.setSkuValue(simpleInfo.getSkuValue());
        esGoods.setPackPrice(simpleInfo.getPackPrice());
        esGoods.setGoodsSpecType(simpleInfo.getGoodsSpecType());
        return esGoods;
    }

    /**
     * 功能描述: 普通商品校验
     *
     * @param
     * @return: void
     */
    protected EsGoodsDTO ordinaryGoodsCheck(CartSimpleDTO goods) {
        Result<EsGoodsDTO> esGoodsDtoResult = this.esGoodsClient.getBySkuCode(goods.getShopId().toString(), goods.getGoodsId().toString(), goods.getSkuCode());
        if (esGoodsDtoResult.failure()) {
            throw new CustomException(esGoodsDtoResult.getCode(), esGoodsDtoResult.getMsg());
        }
        EsGoodsDTO esGoods = esGoodsDtoResult.getData();
        if (esGoods == null) {

            Result<ShopSkuDTO> result = shopGoodsSkuClient.queryBySkuAndShopId(goods.getSkuCode(), goods.getShopId());
            if (result.failure()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
            ShopSkuDTO dto = result.getData();
            if (dto == null) {
                throw new CustomException(result.getMsg());
            }
            throw new CustomException(ErrorCode.GROUPON_DOWN, dto.getGoodsName() + "商品已下架，请重新下单");
        }
        List<EsGoodsSkuDTO> skuList = esGoods.getSkuList();
        if (CollectionUtils.isNotEmpty(skuList)) {
            Map<String, EsGoodsSkuDTO> skuMap = skuList.stream().collect(Collectors.toMap(EsGoodsSkuDTO::getSkuCode, Function.identity(), (key1, key2) -> key2));
            EsGoodsSkuDTO esGoodsSkuDTO = skuMap.get(goods.getSkuCode());
            esGoods.setPackPrice(esGoodsSkuDTO.getPackPrice());
        }
        /** 商品售卖渠道校验 ，只能售卖外卖商品 **/
        // TODO: 2020-07-13 lh

        // 商品是否在上架中
        if (!GoodsStatusEnum.UPPER_SHELF.getStatus().equals(esGoods.getGoodsStatus())) {
            throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, esGoods.getGoodsName() + "商品已下架，请重新提交订单！");
        }
        if (GoodsStatusEnum.LOWER_SHELF.getStatus().equals(esGoods.getMerchantGoodsStatus())) {
            throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, esGoods.getGoodsName() + "商品已下架，请重新提交订单！");
        }

        //是否是企业用户
        boolean isCompanyUser = Objects.equals(goods.getUserType(), UserTypeEnum.COMPANY.getStatus());
        Integer lowestBuy = esGoods.getLowestBuy(isCompanyUser);
        if (lowestBuy != null) {
            // 商品最低、最高购买限制
            if (goods.getNumber() < lowestBuy) {
                throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, esGoods.getGoodsName() + " 最低限购 " + esGoods.getLowestBuy());
            }
        }
        if (esGoods.getHighestBuy() != null && esGoods.getHighestBuy() != -1) {
            if (goods.getNumber() > esGoods.getHighestBuy()) {
                log.error("商品【ID：{}】最高限购【{}】", esGoods.getId(), esGoods.getHighestBuy());
                throw new CustomException(ErrorCode.ORDER_GOODS_DOWN, esGoods.getGoodsName() + " 最高限购 " + esGoods.getHighestBuy());
            }
        }
        return esGoods;
    }


    /**
     * 功能描述:  商品类型转换
     *
     * @param esGoods        ES商品信息
     * @param calculateGoods 计算商品信息
     */
    protected void goodsConver(EsGoodsDTO esGoods, OrdersCalculateGoodsDTO calculateGoods) {
        calculateGoods.setGoodsName(esGoods.getGoodsName());
        calculateGoods.setGoodsSpuCode(esGoods.getSpuCode());
        calculateGoods.setGoodsSkuCode(esGoods.getSkuCode());
        calculateGoods.setGifts(false);
        /* 多个图片逗号分割 */
        String infoPicture = esGoods.getInfoPicture();
        if (StringUtils.isNotBlank(infoPicture)) {
            calculateGoods.setGoodsPicture(infoPicture.split(",")[0]);
        }
        if (!StringUtils.isEmpty(esGoods.getCategoryId())) {
            calculateGoods.setCategoryId(Long.valueOf(esGoods.getCategoryId()));
        }
        calculateGoods.setCategoryName(esGoods.getCategoryName());

        if (!StringUtils.isEmpty(esGoods.getGoodsLabel())) {
            List<String> labelList = Arrays.asList(esGoods.getGoodsLabel());
            if (!CollectionUtils.isEmpty(labelList)) {
                calculateGoods.setGoodsLabelName(labelList.get(0));
            }
        }


        calculateGoods.setGoodsSpecificationDesc(!StringUtils.isEmpty(esGoods.getPropertyValue()) ? esGoods.getPropertyValue() : esGoods.getSkuValue());
    }

    /**
     * 订单商品原价总和
     *
     * @param goodsList
     * @return java.math.BigDecimal
     * @author lh
     * @since v 1.3.0
     */
    protected BigDecimal orderGoodsStoreTotalPrice(List<OrdersCalculateGoodsDTO> goodsList) {
        return goodsList
                .stream()
                .map(i -> {
                    BigDecimal price = i.getStorePrice() == null || i.getStorePrice().compareTo(BigDecimal.ZERO) == 0 ? i.getSalesPrice() : i.getStorePrice();
                    BigDecimal multiply = price.multiply(BigDecimal.valueOf(i.getQuantity()));
                    return multiply;
                })
                .reduce(BigDecimal::add).get();
    }

    /**
     * 订单商品售价总和
     *
     * @param goodsList 订单商品集合
     * @return java.math.BigDecimal
     * @author lh
     * @since v 1.3.0
     */
    protected BigDecimal orderGoodsSalesTotalPrice(List<OrdersCalculateGoodsDTO> goodsList) {
        return goodsList
                .stream()
                .map(i -> {
                    BigDecimal price = i.getSalesPrice() == null || i.getSalesPrice().compareTo(BigDecimal.ZERO) == 0 ? i.getStorePrice() : i.getSalesPrice();
                    BigDecimal multiply = price.multiply(BigDecimal.valueOf(i.getQuantity()));
                    return multiply;
                })
                .reduce(BigDecimal::add).get();
    }

    /**
     * 功能描述: 获取用户配送信息
     *
     * @param
     * @return com.meiyuan.catering.order.dto.calculate.OrdersCalculateDeliveryDTO
     * @author xie-xi-jie
     * @date 2020/6/23 14:12
     * @since v 1.1.0
     */
    protected OrdersCalculateDeliveryDTO getDeliveryInfo(OrderCalculateDTO param) {
        // 从缓存获取商户配置信息
        ShopConfigInfoDTO shopConfigInfo = this.orderUtils.getShopConfigInfo(param.getStoreId());
        if (shopConfigInfo == null) {
            log.error("获取商家配置信息失败 shopConfigInfo= null");
            throw new CustomException("获取商家信息失败");
        }
        // 商家设置的配送金额
        BigDecimal deliveryAmount = shopConfigInfo.getDeliveryPrice();
        // 商户配置了配送费
        if (deliveryAmount != null) {

            param.setDeliveryPriceOriginal(deliveryAmount);
            param.setDeliveryPriceFree(shopConfigInfo.getFreeDeliveryPrice());

            // 获取结算商品的原价合计，用户计算配送费的起送金额、免配送费等
            BigDecimal goodsStoreTotalPrice = this.orderGoodsStoreTotalPrice(param.getGoodsList());
            // 如果商品配置了起送金额，判断是否达到配送的要求
            if (shopConfigInfo.getLeastDeliveryPrice() != null) {
                if (goodsStoreTotalPrice.compareTo(shopConfigInfo.getLeastDeliveryPrice()) < 0) {
                    BigDecimal subtract = shopConfigInfo.getLeastDeliveryPrice().subtract(goodsStoreTotalPrice);
                    throw new CustomException(ErrorCode.CART_CHECK_ERROR,"还差 " + subtract.abs().setScale(2) + " 元才能配送");
                }
            }

            // 满减用优惠后的金额来计算
            // 如果商品配置了满单免配送金额，判断是否达到免配送要求
            BigDecimal goodsSalesTotalPrice = this.orderGoodsSalesTotalPrice(param.getGoodsList());
            if (shopConfigInfo.getFreeDeliveryPrice() != null) {
                if (goodsSalesTotalPrice.compareTo(shopConfigInfo.getFreeDeliveryPrice()) >= 0) {
                    // 省配送费，配送费计入优惠金额
                    deliveryAmount = BigDecimal.ZERO;
                }
            }
            param.setDeliveryPrice(deliveryAmount);
        }
        // 获取用户收货地址信息
        Result<AddressDetailVo> addressDetailVoResult = this.addressClient.userAddressDetail(param.getDeliveryId());
        if (addressDetailVoResult.failure()) {
            log.error("获取用户收货地址信息失败【Msg：{}】", addressDetailVoResult.getMsg());
            throw new CustomException("获取收货地址信息失败");
        }

        AddressDetailVo data = addressDetailVoResult.getData();
        if (data == null) {
            log.error("获取用户收货地址信息失败【data：null】");
            throw new CustomException("获取收货地址信息失败");
        }
        log.debug("获取用户配送信息 {}", data);
        PickupAdressQueryDTO dto = new PickupAdressQueryDTO();
        dto.setMapCoordinate(param.getMapCoordinate());
        dto.setMerchantId(param.getMerchantId());
        dto.setShopId(param.getShopId());


        // 1、 构建订单取餐信息
        OrdersCalculateDeliveryDTO delivery = new OrdersCalculateDeliveryDTO();
        delivery.setConsigneeName(data.getName());
        delivery.setConsigneeSex(data.getGender());
        delivery.setConsigneePhone(data.getPhone());
        delivery.setConsigneeArea(data.getAddressShort());
        delivery.setConsigneeAddress(data.getAddressDetail());


        if (DeliveryWayEnum.invite.getCode().compareTo(param.getDeliveryWay()) == 0) {
            //自提点，才需要查询自提点信息
            // 获取配送自提点信息，默认最近的自提点配送
            Result<IPage<PickupAddressListVo>> iPageResult = this.merchantPickupAdressClient.pickupAddressList(dto);
            if (iPageResult.failure()) {
                throw new CustomException(iPageResult.getCode(), iPageResult.getMsg());
            }
            List<PickupAddressListVo> records = iPageResult.getData().getRecords();
            if (records == null || records.size() == 0) {
                throw new CustomException("该商户未绑定自提点");
            }
            PickupAddressListVo pickupAddressListVo = records.get(0);
            delivery.setStoreId(pickupAddressListVo.getId());
            delivery.setStoreName(pickupAddressListVo.getName());
        }


        return delivery;
    }

    /**
     * 功能描述: 获取用户自取信息
     *
     * @param
     * @return com.meiyuan.catering.order.dto.calculate.OrdersCalculateDeliveryDTO
     * @author xie-xi-jie
     * @date 2020/6/23 14:12
     * @since v 1.1.0
     */
    protected OrdersCalculateDeliveryDTO getInviteInfo(OrderCalculateDTO param) {
        if (param.getDeliveryShopId() == null) {
            // 结算还没选择自提点，也需要返回商品信息
            return null;
        }
        // 自取方式选择的自提点信息
        Result<MerchantsPickupAddressDTO> merchantPickupAddress = this.merchantPickupAdressClient.getMerchantPickupAddress(param.getDeliveryShopId());
        if (merchantPickupAddress.failure()) {
            log.error("获取自取门店失败【Msg：{}】", merchantPickupAddress.getMsg());
            throw new CustomException("获取自取门店失败");
        }
        MerchantsPickupAddressDTO data = merchantPickupAddress.getData();
        if (data == null) {
            log.error("获取自取门店失败【data：null】");
            throw new CustomException("获取自取门店失败");
        }
        // 1、 构建订单取餐信息
        OrdersCalculateDeliveryDTO delivery = new OrdersCalculateDeliveryDTO();
        delivery.setConsigneeArea(data.getAddressDetail());
        //自取单获取的是门店地址加门牌号
        delivery.setConsigneeAddress(data.getDoorNumber());
        delivery.setStoreId(param.getDeliveryShopId());
        delivery.setStoreName(param.getDeliveryShopName());
        delivery.setStoreManager(data.getPrimaryPersonName());
        delivery.setStorePhone(data.getRegisterPhone());
        delivery.setConsigneeName(param.getNickname());
        delivery.setConsigneePhone(param.getDeliveryPhone());
        delivery.setConsigneeSex(param.getGender());
        //自取设置配送费为-1
        param.setDeliveryPrice(BaseUtil.PRICE);
        return delivery;
    }

    /**
     * 功能描述: 构建商品优惠信息，用于结算后的下单数据写入
     *
     * @param goodsDiscountBeforeFee     商品优惠前金额
     * @param goodsDiscountLaterFee      商品优惠后金额
     * @param goodsDTO                   结算商品信息
     * @param ordersCalculateDiscountDTO 订单优惠信息（这里是优惠券信息）
     * @return void
     * @author xie-xi-jie
     * @date 2020/6/23 17:39
     * @since v 1.1.0
     */
    protected OrdersCalculateGoodsDiscountDTO goodsDiscountBuild(BigDecimal goodsDiscountBeforeFee,
                                                                 BigDecimal goodsDiscountLaterFee,
                                                                 OrdersCalculateGoodsDTO goodsDTO,
                                                                 OrdersCalculateDiscountDTO ordersCalculateDiscountDTO) {
        // 商品优惠金额
        BigDecimal discountGoodsFee = goodsDiscountBeforeFee.subtract(goodsDiscountLaterFee);
        goodsDTO.setDiscountFee(discountGoodsFee);
        OrdersCalculateGoodsDiscountDTO goodsDiscountDTO = new OrdersCalculateGoodsDiscountDTO();
        goodsDiscountDTO.setGoodsId(goodsDTO.getGoodsId());
        goodsDiscountDTO.setOrderDiscountsId(ordersCalculateDiscountDTO.getDiscountId());
        goodsDiscountDTO.setDiscountAmount(discountGoodsFee);
        goodsDiscountDTO.setSalesPrice(goodsDTO.getSalesPrice());
        goodsDiscountDTO.setTransactionPrice(goodsDTO.getSalesPrice());
        goodsDiscountDTO.setDiscountBeforeFee(goodsDiscountBeforeFee);
        goodsDiscountDTO.setDiscountLaterFee(goodsDiscountLaterFee);
        goodsDiscountDTO.setLimitLevel(ordersCalculateDiscountDTO.getGoodsLimit());
        return goodsDiscountDTO;
    }

    /**
     * 功能描述: 构建秒杀活动信息，用于结算后的下单数据写入
     *
     * @param simpleInfo 秒杀商品信息
     * @return com.meiyuan.catering.order.dto.calculate.OrdersCalculateActivityDTO
     * @author xie-xi-jie
     * @date 2020/6/23 17:52
     * @since v 1.1.0
     */
    protected List<OrdersCalculateActivityDTO> activityBuild(MarketingGoodsSimpleInfoDTO simpleInfo) {
        // 构建订单活动信息
        List<OrdersCalculateActivityDTO> activityList = new ArrayList<>();
        // 构建订单秒杀活动信息
        OrdersCalculateActivityDTO activityDTO = new OrdersCalculateActivityDTO();
        activityDTO.setRelationDimension(OrderRelationEnum.GOODS.getCode());
        activityDTO.setActivityType(OrderActivityEnum.SECONDS_KILL.getCode());
        BeanUtils.copyProperties(simpleInfo, activityDTO);
        activityList.add(activityDTO);
        return activityList;
    }

    protected OrdersCalculateActivityDTO newActivityBuild(MarketingGoodsSimpleInfoDTO simpleInfo) {
        // 构建订单秒杀活动信息
        OrdersCalculateActivityDTO activityDTO = new OrdersCalculateActivityDTO();
        activityDTO.setRelationDimension(OrderRelationEnum.GOODS.getCode());
        activityDTO.setActivityType(OrderActivityEnum.SECONDS_KILL.getCode());
        BeanUtils.copyProperties(simpleInfo, activityDTO);
        return activityDTO;
    }


    public static void main(String[] args) {
        System.out.println(BigDecimal.ZERO.equals(BigDecimal.ZERO));
    }
}
