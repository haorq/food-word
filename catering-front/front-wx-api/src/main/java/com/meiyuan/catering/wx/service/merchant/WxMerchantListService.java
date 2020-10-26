package com.meiyuan.catering.wx.service.merchant;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopConfigInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.enums.base.DiscountTypeEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.DiscountInfo;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.merchant.DiscountQuery;
import com.meiyuan.catering.es.dto.merchant.EsMerchantListParamDTO;
import com.meiyuan.catering.es.dto.wx.EsWxMerchantDTO;
import com.meiyuan.catering.es.entity.EsMarketingEntity;
import com.meiyuan.catering.es.enums.marketing.MarketingUsingObjectEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.es.fegin.EsMarketingClient;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.es.vo.ShopDiscountInfoVo;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketActivityTypeEnum;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.marketing.vo.ticket.WxMerchantIndexTicketInfoVO;
import com.meiyuan.catering.merchant.enums.BusinessSupportEnum;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopDiscountGoodsDTO;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.wx.dto.DiscountDataMap;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述: 商户列表
 *
 * @author zengzhangni
 * @date 2020/9/3 11:21
 * @since v1.4.0
 */
@Service
@Slf4j
public class WxMerchantListService {

    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private EsMerchantClient esMerchantClient;
    @Autowired
    private EsGoodsClient esGoodsClient;
    @Resource
    private EsMarketingClient esMarketingClient;
    @Resource
    private MarketingTicketActivityClient marketingTicketActivityClient;
    @Resource
    private ShopGoodsSkuClient shopGoodsSkuClient;


    public Result<PageData<EsWxMerchantDTO>> wxMerchantLimit(UserTokenDTO token, EsMerchantListParamDTO dto) {
        //查询wx小程序有外卖商品的店铺
        ShopDiscountGoodsDTO discountGoodsDto = new ShopDiscountGoodsDTO();
        if (BaseUtil.judgeList(dto.getShopIdList())){
            List<Long> shopIds = dto.getShopIdList().stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
            discountGoodsDto.setShopIds(shopIds);
        }
        Result<List<Long>> listResult = shopGoodsSkuClient.listShopHaveGoods(discountGoodsDto);
        if(!BaseUtil.judgeResultList(listResult)){
            return Result.succ(new PageData<>());
        }
        List<Long> shopIds = listResult.getData();
        List<String> shopIdsStr = shopIds.stream().map(s -> String.valueOf(s)).collect(Collectors.toList());
        dto.setShopIdList(shopIdsStr);

        Long pageNo = dto.getPageNo();
        Long pageSize = dto.getPageSize();
        //处理分页大小
        disposePageSize(dto);

        //先查询一次结果
        Result<PageData<EsWxMerchantDTO>> result = esMerchantClient.listLimit(dto);
        PageData<EsWxMerchantDTO> page = ClientUtil.getDate(result);
        if (page.getTotal() <= 0) {
            return result;
        }

        //通过过滤条件去匹配shopId
        Set<String> matchShopIds = queryByCondition(dto, token, result);

        if (matchShopIds != null) {
            //如果过滤的shopId 大于0 在通过匹配的shopId 再查询一次
            if (matchShopIds.size() > 0) {
                //list 分页
                listPage(result.getData(), pageNo, pageSize, matchShopIds);

            } else {
                //没有匹配的门店直接返回 []
                return Result.succ(new PageData<>());
            }
        }

        //使用结果门店id
        matchShopIds = result.getData().getList().stream().map(EsWxMerchantDTO::getShopId).collect(Collectors.toSet());


        //查询对应门店的优惠信息
        DiscountDataMap dataMap = getDataMap(matchShopIds, token);
        //组装结果数据
        setResultData(dto, result, dataMap);
        return result;
    }

    public void setResultData(EsMerchantListParamDTO dto, Result<PageData<EsWxMerchantDTO>> result, DiscountDataMap dataMap) {

        result.getData().getList().forEach(
                i -> {
                    Long merchantId = Long.valueOf(i.getMerchantId());
                    Long shopId = Long.valueOf(i.getShopId());
                    MerchantInfoDTO merchant = merchantUtils.getMerchant(merchantId);
                    ShopInfoDTO shop = merchantUtils.getShop(shopId);
                    if (shop != null) {
                        // 门头图
                        i.setDoorHeadPicture(shop.getDoorHeadPicture());
                        //设置门店是否营业
                        i.setBusinessStatus(shop.getBusinessStatus());
                        // 商户标签
                        i.setShopTag(merchantUtils.getShopTagNameList(shop.getMerchantId()));
                    }

                    if (merchant != null) {
                        i.setMerchantAttribute(merchant.getMerchantAttribute());
                    }

                    ShopConfigInfoDTO shopConfigInfo = merchantUtils.getShopConfigInfo(shopId);
                    if (shopConfigInfo != null) {
                        BigDecimal leastDeliveryPrice = shopConfigInfo.getLeastDeliveryPrice();
                        // 订单起送金额
                        i.setLeastDeliveryPrice(leastDeliveryPrice != null ? leastDeliveryPrice.doubleValue() : 0);
                        // 配送费
                        BigDecimal deliveryPrice = shopConfigInfo.getDeliveryPrice();
                        i.setDeliveryPrice(deliveryPrice != null ? deliveryPrice.doubleValue() : 0);
                        i.setBusinessSupport(shopConfigInfo.getBusinessSupport());
                    }
                    // 距离多少米
                    i.setLocation(EsUtil.convertLocation(dto.getLat(), dto.getLng(), i.getLocation().split(BaseUtil.COMMA)));

                    //优惠优惠
                    if (dataMap != null) {
                        i.setDiscountInfoList(dataMap.getDiscountList(i.getShopId()));
                    }
                }
        );
    }


    public Set<String> queryByCondition(EsMerchantListParamDTO dto, UserTokenDTO token, Result<PageData<EsWxMerchantDTO>> result) {

        //用Result返回的shopId 进行过滤
//        List<String> shopIdList = dto.getShopIdList();
        dto.setShopIdList(getResultIds(result));

        Set<String> shopIdSet;

        //配送方式过滤
        shopIdSet = deliveryTypeFilter(dto);

        //如果配送方式没有匹配的商家 就不用匹配优惠活动了 直接返回
        if (shopIdSet != null && shopIdSet.size() == 0) {
            return shopIdSet;
        }

        //优惠活动过滤
        shopIdSet = activityTypeFilter(dto, token, shopIdSet);

        return shopIdSet;
    }


    private Set<String> activityTypeFilter(EsMerchantListParamDTO dto, UserTokenDTO token, Set<String> shopIdSet) {

        Integer activityType = dto.getActivityType();

        if (activityType != null) {
            Set<String> queryIds = new HashSet<>();

            DiscountQuery query = DiscountQuery.builder()
                    .activityType(activityType)
                    .deliveryType(dto.getDeliveryType())
                    .cityCode(dto.getCityCode())
                    .appointShopIds(dto.getShopIdList())
                    .objectLimit(getMarketingUserType(token))
                    .build();

            DiscountTypeEnum discountTypeEnum = DiscountTypeEnum.parse(activityType);
            switch (discountTypeEnum) {
                //1：折扣商品
                case GOODS_DISCOUNT:
                    //企业用户不支持特价活动
                    if (Objects.equals(MarketingUsingObjectEnum.ENTERPRISE.getStatus(), query.getObjectLimit())) {
                        queryIds.addAll(Sets.newHashSet());
                        break;
                    }
                    queryIds.addAll(esGoodsClient.queryDiscountShop(query));
                    break;
                //4，秒杀活动
                case SECKILL_DISCOUNT:
                    query.setOfType(MarketingOfTypeEnum.SECKILL.getStatus());
                    List<EsMarketingEntity> seckillList = esMarketingClient.queryDiscountMarketing(query).getData();
                    queryIds = seckillList.stream().map(EsMarketingEntity::getShopId).collect(Collectors.toSet());
                    break;
                //5：团购活动
                case GROUPON_DISCOUNT:
                    query.setOfType(MarketingOfTypeEnum.GROUPON.getStatus());
                    List<EsMarketingEntity> grouponList = esMarketingClient.queryDiscountMarketing(query).getData();
                    queryIds.addAll(grouponList.stream().map(EsMarketingEntity::getShopId).collect(Collectors.toSet()));
                    break;
                //2：进店领券/店内领券
                case TICKET_GET_DISCOUNT:
                    List<Long> get = dto.getShopIdList().stream().map(Long::valueOf).collect(Collectors.toList());
                    List<Long> getIds = marketingTicketActivityClient.findTicketShop(1, get);
                    queryIds.addAll(getIds.stream().map(Objects::toString).collect(Collectors.toSet()));
                    break;
                //3：满减优惠券/(店外发券/平台优惠券)
                case TICKET_SEND_DISCOUNT:
                    List<Long> send = dto.getShopIdList().stream().map(Long::valueOf).collect(Collectors.toList());
                    List<Long> sendIds = marketingTicketActivityClient.findTicketShop(2, send);
                    queryIds.addAll(sendIds.stream().map(Objects::toString).collect(Collectors.toSet()));
                    break;
                //满x元免配送
                case DELIVERY_DISCOUNT:
                    List<ShopConfigInfoDTO> shopConfigList = merchantUtils.getShopConfigList(new HashSet<>(dto.getShopIdList()));
                    queryIds.addAll(
                            shopConfigList.stream().filter(
                                    e -> e.getDispatching() != null
                            ).map(e -> e.getShopId().toString()).collect(Collectors.toSet())
                    );
                    break;
                default:
                    break;
            }

            if (shopIdSet != null) {
                //配送方式有值 取配送方式 与 优惠活动的交集
                Collection intersection = CollectionUtils.intersection(shopIdSet, queryIds);
                shopIdSet = Sets.newHashSet(intersection);
            } else {
                shopIdSet = queryIds;
            }

        }
        return shopIdSet;
    }

    private Set<String> deliveryTypeFilter(EsMerchantListParamDTO dto) {
        Integer deliveryType = dto.getDeliveryType();
        if (deliveryType != null) {
            List<ShopConfigInfoDTO> shopConfigList = merchantUtils.getShopConfigList(new HashSet<>(dto.getShopIdList()));

            return shopConfigList.stream().filter(
                    config -> Objects.equals(config.getBusinessSupport(), deliveryType)
                            || Objects.equals(config.getBusinessSupport(), BusinessSupportEnum.ALL.getStatus())
            ).map(e -> e.getShopId().toString()).collect(Collectors.toSet());
        }
        return null;
    }

    public DiscountDataMap getDataMap(Set<String> matchShopIds, UserTokenDTO token) {

        DiscountDataMap map = new DiscountDataMap();


        //折扣商品
        Map<String, ShopDiscountInfoVo> goodsDiscountMap = goodsDiscount(matchShopIds, token);
        //秒杀商品
        Map<String, ShopDiscountInfoVo> seckillDiscountMap = seckillDiscount(matchShopIds, token);
        //团购商品
        Map<String, ShopDiscountInfoVo> grouponDiscountMap = grouponDiscount(matchShopIds, token);
        //免配送优惠
        Map<String, ShopDiscountInfoVo> dispatchingDiscountMap = dispatchingDiscount(matchShopIds);


        //获取门店优惠券信息
        List<Long> collect = matchShopIds.stream().map(Long::valueOf).collect(Collectors.toList());
        Map<Long, List<WxMerchantIndexTicketInfoVO>> shopTicketMap = marketingTicketActivityClient.findShopTicket(collect);

        //进店领券/店内领券
        Map<String, ShopDiscountInfoVo> ticketGetDiscountMap = ticketGetDiscount(shopTicketMap, matchShopIds);
        //满减优惠券/(店外发券/平台优惠券)
        Map<String, ShopDiscountInfoVo> ticketSendDiscountMap = ticketSendDiscount(shopTicketMap, matchShopIds);

        map.setGoodsDiscountMap(goodsDiscountMap);
        map.setSeckillDiscountMap(seckillDiscountMap);
        map.setGrouponDiscountMap(grouponDiscountMap);
        map.setTicketGetDiscountMap(ticketGetDiscountMap);
        map.setTicketSendDiscountMap(ticketSendDiscountMap);
        map.setDispatchingDiscountMap(dispatchingDiscountMap);
        return map;
    }

    private Map<String, ShopDiscountInfoVo> goodsDiscount(Set<String> matchShopIds, UserTokenDTO token) {
        //企业用户不支持特价活动
        if (token != null && token.isCompanyUser()) {
            return Maps.newHashMap();
        }

        Map<String, Double> goodsMap = esGoodsClient.queryMinDiscount(matchShopIds);
        return matchShopIds.stream().map(
                shopId -> {
                    Double minDiscount = goodsMap.get(shopId);
                    if (minDiscount != null) {
                        ShopDiscountInfoVo infoVo = new ShopDiscountInfoVo();
                        infoVo.setShopId(shopId);
                        infoVo.setDiscountType(DiscountTypeEnum.GOODS_DISCOUNT.getStatus());
                        infoVo.setDiscountStr(Lists.newArrayList(DiscountInfo.goodsDiscountMsg(minDiscount)));
                        return infoVo;
                    }
                    return null;
                }
        ).filter(Objects::nonNull).collect(Collectors.toMap(ShopDiscountInfoVo::getShopId, e -> e));
    }

    private Map<String, ShopDiscountInfoVo> seckillDiscount(Set<String> matchShopIds, UserTokenDTO token) {
        Map<String, Double> statsMap = esMarketingClient.queryMarketingDiscount(matchShopIds, MarketingOfTypeEnum.SECKILL.getStatus(), getMarketingUserType(token)).getData();
        return matchShopIds.stream().map(
                shopId -> {
                    Double min = statsMap.get(shopId);
                    if (min != null) {
                        ShopDiscountInfoVo infoVo = new ShopDiscountInfoVo();
                        infoVo.setShopId(shopId);
                        infoVo.setDiscountType(DiscountTypeEnum.SECKILL_DISCOUNT.getStatus());
                        infoVo.setDiscountStr(Lists.newArrayList(DiscountInfo.seckillMsg(min)));
                        return infoVo;
                    }
                    return null;
                }
        ).filter(Objects::nonNull).collect(Collectors.toMap(ShopDiscountInfoVo::getShopId, e -> e));

    }

    private Map<String, ShopDiscountInfoVo> grouponDiscount(Set<String> matchShopIds, UserTokenDTO token) {
        Map<String, Double> statsMap = esMarketingClient.queryMarketingDiscount(matchShopIds, MarketingOfTypeEnum.GROUPON.getStatus(), getMarketingUserType(token)).getData();
        return matchShopIds.stream().map(
                shopId -> {
                    Double min = statsMap.get(shopId);
                    if (min != null) {
                        ShopDiscountInfoVo infoVo = new ShopDiscountInfoVo();
                        infoVo.setShopId(shopId);
                        infoVo.setDiscountType(DiscountTypeEnum.GROUPON_DISCOUNT.getStatus());
                        infoVo.setDiscountStr(Lists.newArrayList(DiscountInfo.grouponMsg(min)));
                        return infoVo;
                    }
                    return null;
                }
        ).filter(Objects::nonNull).collect(Collectors.toMap(ShopDiscountInfoVo::getShopId, e -> e));
    }


    private Map<String, ShopDiscountInfoVo> ticketGetDiscount(Map<Long, List<WxMerchantIndexTicketInfoVO>> map, Set<String> matchShopIds) {
        return matchShopIds.stream().map(
                shopId -> {
                    List<WxMerchantIndexTicketInfoVO> ticketInfoVOS = map.get(Long.valueOf(shopId));
                    if (CollectionUtils.isNotEmpty(ticketInfoVOS)) {

                        ShopDiscountInfoVo infoVo = new ShopDiscountInfoVo();

                        List<String> discountStrList = ticketInfoVOS.stream().filter(e ->
                                Objects.equals(e.getActivityType(), MarketingTicketActivityTypeEnum.SHOP_IN_GET.getStatus())
                        ).sorted(Comparator.comparing(
                                WxMerchantIndexTicketInfoVO::getAmount).reversed()
                                .thenComparing(WxMerchantIndexTicketInfoVO::getConsumeCondition)
                        ).map(ticket ->
                                DiscountInfo.getTicketMsg(ticket.getAmount())
                        ).collect(Collectors.toList());

                        if (discountStrList.size() > 0) {
                            infoVo.setShopId(shopId);
                            infoVo.setDiscountType(DiscountTypeEnum.TICKET_GET_DISCOUNT.getStatus());
                            infoVo.setDiscountStr(discountStrList);
                            return infoVo;
                        }
                    }
                    return null;
                }
        ).filter(Objects::nonNull).collect(Collectors.toMap(ShopDiscountInfoVo::getShopId, e -> e));
    }

    private Map<String, ShopDiscountInfoVo> ticketSendDiscount(Map<Long, List<WxMerchantIndexTicketInfoVO>> map, Set<String> matchShopIds) {
        return matchShopIds.stream().map(
                shopId -> {
                    List<WxMerchantIndexTicketInfoVO> ticketInfoVOS = map.get(Long.valueOf(shopId));
                    if (CollectionUtils.isNotEmpty(ticketInfoVOS)) {
                        ShopDiscountInfoVo infoVo = new ShopDiscountInfoVo();
                        //店外发券  和 平台补贴(平台补贴 排到 店外发券 前面)
                        List<String> discountStrList = ticketInfoVOS.stream().filter(e ->
                                Objects.equals(e.getActivityType(), MarketingTicketActivityTypeEnum.SHOP_OUT_GET.getStatus())
                                        || Objects.equals(e.getActivityType(), MarketingTicketActivityTypeEnum.PLATFORM_SUBSIDY.getStatus())
                        ).sorted(Comparator.comparing(WxMerchantIndexTicketInfoVO::getActivityType).reversed()).map(ticket -> {
                            BigDecimal consumeCondition = ticket.getConsumeCondition();
                            if (!BaseUtil.isZero(consumeCondition)) {
                                return DiscountInfo.sendTicketSubtractMsg(consumeCondition, ticket.getAmount());
                            } else {
                                return DiscountInfo.sendTicketMsg(ticket.getAmount());
                            }
                        }).collect(Collectors.toList());

                        if (discountStrList.size() > 0) {
                            infoVo.setShopId(shopId);
                            infoVo.setDiscountType(DiscountTypeEnum.TICKET_SEND_DISCOUNT.getStatus());
                            infoVo.setDiscountStr(discountStrList);
                            return infoVo;
                        }
                    }
                    return null;
                }
        ).filter(Objects::nonNull).collect(Collectors.toMap(ShopDiscountInfoVo::getShopId, e -> e));
    }

    private Map<String, ShopDiscountInfoVo> dispatchingDiscount(Set<String> matchShopIds) {

        Map<String, ShopConfigInfoDTO> configMap = merchantUtils.getShopConfigMap(matchShopIds);

        return matchShopIds.stream().map(
                shopId -> {
                    ShopConfigInfoDTO configInfoDTO = configMap.get(shopId);
                    if (configInfoDTO != null) {
                        String dispatching = configInfoDTO.getDispatching();
                        if (dispatching != null) {
                            ShopDiscountInfoVo infoVo = new ShopDiscountInfoVo();
                            infoVo.setShopId(shopId);
                            infoVo.setDiscountType(DiscountTypeEnum.DELIVERY_DISCOUNT.getStatus());
                            infoVo.setDiscountStr(Lists.newArrayList(dispatching));
                            return infoVo;
                        }
                    }
                    return null;
                }
        ).filter(Objects::nonNull).collect(Collectors.toMap(ShopDiscountInfoVo::getShopId, e -> e));
    }


    private Integer getMarketingUserType(UserTokenDTO token) {
        return token != null ? token.getMarketingUserType() : MarketingUsingObjectEnum.PERSONAL.getStatus();
    }

    private List<String> getResultIds(Result<PageData<EsWxMerchantDTO>> result) {
        return result.getData().getList().stream().map(EsWxMerchantDTO::getShopId).collect(Collectors.toList());
    }

    private void disposePageSize(EsMerchantListParamDTO dto) {
        //筛选条件不为空
        //查询城市所有门店 或者查询指定门店
        if (dto.getDeliveryType() != null || dto.getActivityType() != null) {
            Long cityShopSum = dto.getCityShopSum();
            long pageNo = 1;
            long pageSize = cityShopSum != null ? cityShopSum : shopIdSize(dto);
            dto.setPageNo(pageNo);
            dto.setPageSize(pageSize);
        }
    }

    private long shopIdSize(EsMerchantListParamDTO dto) {
        List<String> shopIdList = dto.getShopIdList();
        return CollectionUtils.isNotEmpty(shopIdList) ? shopIdList.size() : dto.getPageSize();
    }

    private void listPage(PageData<EsWxMerchantDTO> page, Long pageNo, Long pageSize, Set<String> matchShopIds) {
        List<EsWxMerchantDTO> list = page.getList();
        list = list.stream()
                .filter(e -> matchShopIds.contains(e.getShopId()))
                .skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());

        page.setTotal(matchShopIds.size());
        page.setList(list);
        page.setLastPage(pageSize, pageNo);
    }

}
