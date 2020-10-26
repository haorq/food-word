package com.meiyuan.catering.order.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.check.ContentCheckService;
import com.meiyuan.catering.core.dto.DateTimeDiffDto;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.order.delivery.OrderDelivery;
import com.meiyuan.catering.core.dto.order.goods.OrderGoods;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.redis.utils.DicUtils;
import com.meiyuan.catering.core.util.*;
import com.meiyuan.catering.core.util.dada.DadaUtils;
import com.meiyuan.catering.core.util.dada.client.DadaRetAfterAddOrder;
import com.meiyuan.catering.es.dto.merchant.EsMerchantDTO;
import com.meiyuan.catering.es.enums.goods.GoodsSpecTypeEnum;
import com.meiyuan.catering.es.fegin.EsMerchantClient;
import com.meiyuan.catering.es.util.EsUtil;
import com.meiyuan.catering.marketing.dto.ticket.TicketDataRecordDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.enums.MarketingTicketSendTicketPartyEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketUsefulConditionEnum;
import com.meiyuan.catering.marketing.feign.MarketingGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingTicketActivityClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.merchant.dto.merchant.MerchantReportDto;
import com.meiyuan.catering.merchant.dto.merchant.MerchantsPickupAddressDTO;
import com.meiyuan.catering.merchant.dto.shop.bill.*;
import com.meiyuan.catering.merchant.enums.MerchantAttribute;
import com.meiyuan.catering.merchant.enums.ShopStatusEnum;
import com.meiyuan.catering.merchant.feign.MerchantPickupAdressClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.shop.bill.BillMerchantInfoVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopBillCityVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopBillDetailVo;
import com.meiyuan.catering.merchant.vo.shop.bill.ShopListBillVo;
import com.meiyuan.catering.order.dao.*;
import com.meiyuan.catering.order.dto.*;
import com.meiyuan.catering.order.dto.calculate.OrdersCalculateGoodsInfoDTO;
import com.meiyuan.catering.order.dto.calculate.ShareBillCalculateGoodsInfoDTO;
import com.meiyuan.catering.order.dto.goods.GoodsMonthSalesDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleDTO;
import com.meiyuan.catering.order.dto.goods.GoodsSaleExcelDTO;
import com.meiyuan.catering.order.dto.order.*;
import com.meiyuan.catering.order.dto.query.GoodsSalePageParamDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailGoodsDTO;
import com.meiyuan.catering.order.dto.query.admin.*;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailGoodsWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersListWxParamDTO;
import com.meiyuan.catering.order.dto.submit.CommentDTO;
import com.meiyuan.catering.order.dto.submit.OrderTicketUsedRecordDTO;
import com.meiyuan.catering.order.entity.*;
import com.meiyuan.catering.order.enums.*;
import com.meiyuan.catering.order.mq.sender.OrderSecKillMqSender;
import com.meiyuan.catering.order.service.*;
import com.meiyuan.catering.order.utils.OrderUtils;
import com.meiyuan.catering.order.vo.*;
import com.meiyuan.catering.user.service.CateringIntegralActivityService;
import com.meiyuan.catering.user.service.CateringIntegralRecordService;
import com.meiyuan.catering.user.vo.integral.IntegralRuleListVo;
import com.meiyuan.catering.user.vo.integral.IntegralRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 订单表(CateringOrders)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Slf4j
@Service("cateringOrdersServiceImpl")
public class CateringOrdersServiceImpl extends ServiceImpl<CateringOrdersMapper, CateringOrdersEntity> implements CateringOrdersService {

    private static final String DELIVERY_TYPE = "达达";
    private static final String DELIVERY_TYPE_REMARK = "达达";

    @Resource
    private CateringOrdersMapper cateringOrdersMapper;
    @Resource
    private CateringOrdersDeliveryService cateringOrdersDeliveryService;
    @Resource
    private CateringOrdersOperationService cateringOrdersOperationService;
    @Resource
    private CateringOrdersAppraiseService cateringOrdersAppraiseService;
    @Resource
    private OrderSecKillMqSender orderSecKillMqSender;
    @Resource
    private OrdersSupport ordersSupport;
    @Resource
    private CateringOrdersDiscountsService cateringOrdersDiscountsService;
    @Resource
    private UserTicketClient userTicketClient;
    @Resource
    private ContentCheckService contentCheckService;
    @Resource
    private CateringIntegralRecordService cateringIntegralRecordService;
    @Autowired
    private CateringIntegralActivityService cateringIntegralActivityService;
    @CreateCache
    private Cache<String, String> cache;
    @Resource
    private OrderUtils orderUtils;
    @Resource
    private EsMerchantClient esMerchantClient;

    @Resource
    private MerchantPickupAdressClient merchantPickupAdressClient;
    @Resource
    private CateringOrdersGoodsService cateringOrdersGoodsService;
    @Resource
    private ShopGoodsClient shopGoodsClient;
    @Resource
    private ShopClient shopClient;
    @Resource
    private MarketingTicketActivityClient marketingTicketActivityClient;
    @Resource
    private CateringOrdersRefundMapper cateringOrdersRefundMapper;
    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private CateringOrdersRefundService ordersRefundService;
    @Resource
    private MarketingGoodsClient marketingGoodsClient;
    @Resource
    private CateringOrderDeliveryStatusHistoryMapper cateringOrderDeliveryStatusHistoryMapper;
    @Resource
    private CateringOrdersDeliveryNoMapper cateringOrdersDeliveryNoMapper;
    @Resource
    private DadaUtils dadaUtils;
    @Resource
    private CateringOrdersDeliveryCancelRecordMapper cateringOrdersDeliveryCancelRecordMapper;
    @Resource
    private CateringOrderDeliveryStatusHistoryService cateringOrderDeliveryStatusHistoryService;

    /**
     * 功能描述: 后台——订单列表查询
     *
     * @param adminParamDTO 请求参数
     * @return: 后台订单列表信息
     */
    @Override
    public IPage<OrdersListAdminDTO> orderListQueryAdmin(OrdersListAdminParamDTO adminParamDTO) {
        if (adminParamDTO.getKeyWord() != null) {
            adminParamDTO.setKeyWord(CharUtil.disposeChar(adminParamDTO.getKeyWord()));
        }
        Page<OrdersListAdminDTO> page = new Page<>(adminParamDTO.getPageNo(), adminParamDTO.getPageSize());
        return this.cateringOrdersMapper.orderListQueryAdmin(page, adminParamDTO);
    }

    /**
     * 功能描述: 商户——订单列表查询
     *
     * @param merchantParamDTO 请求参数
     * @return: 商户订单列表信息
     */
    @Override
    public IPage<OrdersListMerchantDTO> orderListQueryMerchant(OrdersListMerchantParamDTO merchantParamDTO) {
        if (merchantParamDTO.getKeyWord() != null) {
            merchantParamDTO.setKeyWord(CharUtil.disposeChar(merchantParamDTO.getKeyWord()));
        }
        Page<OrdersListMerchantDTO> page = new Page<>(merchantParamDTO.getPageNo(), merchantParamDTO.getPageSize());
        IPage<OrdersListMerchantDTO> iPage = this.cateringOrdersMapper.orderListQueryMerchant(page, merchantParamDTO);
        List<OrdersListMerchantDTO> records = iPage.getRecords();
        if (BaseUtil.judgeList(records)) {


            List<Long> orderIdList = records.stream().map(OrdersListMerchantDTO::getOrderId).collect(Collectors.toList());
            List<OrdersDetailMerchantDTO> orderInfoList = listOrderDetailQueryMerchant(orderIdList);

            for (OrdersDetailMerchantDTO dto : orderInfoList) {
                // 非自营，不展示订单归属 v1.4.0 lh
                MerchantInfoDTO merchant = merchantUtils.getMerchant(dto.getMerchantId());
                if (merchant != null
                        && merchant.getMerchantAttribute() != null
                        && merchant.getMerchantAttribute().compareTo(MerchantAttribute.NOT_SELF_BUSINESS.getBusinessAttribute()) == 0) {
                    dto.setTakeAddress(StringUtils.EMPTY);
                }
            }

            Map<Long/* order_id */, OrdersDetailMerchantDTO> orderInfoMap = orderInfoList
                    .stream()
                    .collect(Collectors.toMap(OrdersDetailMerchantDTO::getOrderId, i -> i));

            records.forEach(obj -> {

                obj.setOrderInfo(orderInfoMap.get(obj.getOrderId()));

                // 处理商户订单状态（状态值转字符串）
                MerchantOrderStatusEnum statusEnum = ordersSupport.orderStatusConver(obj.getOrderStatus(), obj.getRefundStatus(), obj.getAfterSales());
                obj.setAppOrderStatus(statusEnum.getCode());
                obj.setOrderStatusStr(statusEnum.getDesc());
                // 处理商户订单退款原因状态（状态值转字符串）
                obj.setRefundReason(ordersSupport.getRefundReasonStr(obj.getRefundReasonList()));

                // 取餐／配送日期g
                if (Objects.nonNull(obj.getDeliveryTime())) {
                    String deliveryDate = DateTimeUtil.getDateTimeDisplayString(obj.getDeliveryTime(), "yyyy-MM-dd");
                    obj.setDeliveryDate(deliveryDate);
                } else {
                    String deliveryDate = DateTimeUtil.getDateTimeDisplayString(obj.getImmediateDeliveryDate(), "yyyy-MM-dd");
                    obj.setDeliveryDate(deliveryDate);
                }
                //自取和退款单，不需要返回取餐日期
                if (obj.getDeliveryWay().compareTo(DeliveryWayEnum.invite.getCode()) == 0
                        || obj.getRefundId() != null) {
                    obj.setEstimateDate("");
                }
            });
        }


        return iPage;
    }

    @Override
    public List<MerchantReportDto> orderListReportMerchant(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime) {
        List<MerchantReportDto> orderListMerchant = cateringOrdersMapper.getOrderListMerchant(shopId, merchantId, startTime, endTime);
        return CollectionUtils.isEmpty(orderListMerchant) ? new ArrayList<>() : orderListMerchant;
    }

    /**
     * 功能描述: 商户订单列表总金额
     *
     * @param paramDTO 请求参数
     * @return: 商户订单列表总金额
     */
    @Override
    public BigDecimal orderTotalAmountMerchant(OrdersListMerchantParamDTO paramDTO) {
        if (paramDTO.getKeyWord() != null) {
            paramDTO.setKeyWord(CharUtil.disposeChar(paramDTO.getKeyWord()));
        }
        return this.cateringOrdersMapper.orderTotalAmountMerchant(paramDTO);
    }

    /**
     * 功能描述: 微信——订单列表查询
     *
     * @param wxParamDTO 请求参数
     * @return: 微信订单列表信息
     */
    @Override
    public IPage<OrdersListWxDTO> orderListQueryWx(OrdersListWxParamDTO wxParamDTO) {
        if (wxParamDTO.getKeyWord() != null) {
            wxParamDTO.setKeyWord(CharUtil.disposeChar(wxParamDTO.getKeyWord()));
        }
        Page<OrdersListWxDTO> page = new Page<>(wxParamDTO.getPageNo(), wxParamDTO.getPageSize());
        IPage<OrdersListWxDTO> iPage = this.cateringOrdersMapper.orderListQueryWx(page, wxParamDTO);
        List<OrdersListWxDTO> records = iPage.getRecords();
        if (BaseUtil.judgeList(records)) {
            records.forEach(e -> {
                ShopInfoDTO shop = this.orderUtils.getShop(e.getShopId());
                e.setStoreName(shop.getShopName());
                e.setStorePicture(shop.getDoorHeadPicture());
                MerchantInfoDTO merchant = merchantUtils.getMerchant(shop.getMerchantId());
                e.setIsWxShow(0);
                e.setIsShopDel(Boolean.FALSE);
                if (shopClient.getShopFromCache(e.getShopId()) == null || merchant == null) {
                    e.setIsShopDel(Boolean.TRUE);
                }
                //门店、品牌都启用，才是启用
                if (merchant != null && ShopStatusEnum.OPEN.getStatus().equals(shop.getShopStatus()) &&
                        ShopStatusEnum.OPEN.getStatus().equals(merchant.getMerchantStatus())) {
                    e.setShopStatus(ShopStatusEnum.OPEN.getStatus());
                } else {
                    e.setShopStatus(ShopStatusEnum.FORBIDDEN.getStatus());
                }
                e.setBusiness(shop.getBusinessStatus());

                e.setOrderStatusStr(OrderStatusEnum.parse(e.getOrderStatus()));

                Result result = shopClient.isWxShow(shop.getId());
                if (result.getCode() == 0) {
                    e.setIsWxShow(1);
                }
            });
        }
        return iPage;
    }

    /**
     * 功能描述: 后台——订单详情查询
     *
     * @param orderId 请求参数
     * @return: 后台订单详情
     */
    @Override
    public OrdersDetailAdminDTO orderDetailQueryAdmin(Long orderId) {
        OrdersDetailAdminDTO ordersDetailAdminDTO = this.cateringOrdersMapper.orderDetailQueryAdmin(orderId);
        // 商品原价合计
        List<OrdersDetailGoodsAdminDTO> orderGoodsList = ordersDetailAdminDTO.getGoods();
        BigDecimal storePriceTotal = orderGoodsList
                .stream()
                .filter(obj -> !obj.getGifts())
                .map(OrdersDetailGoodsAdminDTO::getDiscountBeforeFee)
                .reduce((x, y) -> x.add(y)).get();
        // 商品现价合计
        BigDecimal salesPriceTotal = orderGoodsList
                .stream()
                .filter(obj -> !obj.getGifts())
                .map(OrdersDetailGoodsAdminDTO::getDiscountLaterFee)
                .reduce((x, y) -> x.add(y)).get();
        OrdersDetailFeeAdminDTO fee = ordersDetailAdminDTO.getFee();
        fee.setSalesPriceTotal(salesPriceTotal);
        fee.setStorePriceTotal(storePriceTotal);
        fee.setGoodsDiscountFee(storePriceTotal.subtract(salesPriceTotal));


        // 限购份数商品集合
        List<OrdersDetailGoodsAdminDTO> orderGoodsDiscountLimitList = Lists.newArrayList();
        Iterator<OrdersDetailGoodsAdminDTO> iterator = orderGoodsList.iterator();
        while (iterator.hasNext()) {
            OrdersDetailGoodsAdminDTO good = iterator.next();
            if (good.getDiscountLimit() == null
                    || good.getDiscountLimit().compareTo(Integer.valueOf(0)) <= 0) {
                good.setDiscountLimit(null);
            } else {
                // 有限优惠数量
                if (good.getQuantity().compareTo(good.getDiscountLimit()) > 0) {
                    // 购买数量与限购数量不同，需分开展示
                    // 优惠数据
                    OrdersDetailGoodsAdminDTO goodsWithLimit = new OrdersDetailGoodsAdminDTO();
                    BeanUtils.copyProperties(good, goodsWithLimit);
                    goodsWithLimit.setQuantity(good.getDiscountLimit());
                    goodsWithLimit.setDiscountBeforeFee(good.getStorePrice().multiply(BigDecimal.valueOf(goodsWithLimit.getQuantity())));
                    goodsWithLimit.setDiscountLaterFee(goodsWithLimit.getSalesPrice().multiply(BigDecimal.valueOf(goodsWithLimit.getQuantity())));
                    orderGoodsDiscountLimitList.add(goodsWithLimit);
                    // 未优惠价格
                    good.setQuantity(good.getQuantity() - good.getDiscountLimit());
                    good.setSalesPrice(good.getStorePrice());
                    good.setGoodsType(OrderGoodsTypeEnum.ORDINARY.getValue());
                    good.setDiscountBeforeFee(good.getStorePrice().multiply(BigDecimal.valueOf(good.getQuantity())));
                    good.setDiscountLaterFee(good.getSalesPrice().multiply(BigDecimal.valueOf(good.getQuantity())));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(orderGoodsDiscountLimitList)) {
            orderGoodsList.addAll(orderGoodsDiscountLimitList);
            orderGoodsList.sort(new Comparator<OrdersDetailGoodsAdminDTO>() {
                @Override
                public int compare(OrdersDetailGoodsAdminDTO o1, OrdersDetailGoodsAdminDTO o2) {
                    return o1.getGoodsName().compareTo(o2.getGoodsName());
                }
            });
        }
        //优惠券金额合计
        BigDecimal activityTotalPrice = BigDecimal.ZERO;
        List<OrdersDetailActivityAdminDTO> activityList = ordersDetailAdminDTO.getActivity();
        if (CollectionUtils.isNotEmpty(activityList)) {
            for (OrdersDetailActivityAdminDTO dto : activityList) {
                dto.setSendTicketPartyDesc(MarketingTicketSendTicketPartyEnum.parseType(dto.getSendTicketParty()));
            }
            activityTotalPrice = activityList.stream().map(OrdersDetailActivityAdminDTO::getActivityDiscount).reduce(BigDecimal::add).get();
        }
        ordersDetailAdminDTO.setActivityTotalPrice(activityTotalPrice);

        // 计算商品实际金额（分摊优惠后的金额）
        for (OrdersDetailGoodsAdminDTO dto : orderGoodsList) {
            // TODO: 2020-08-20 产品定义暂放
        }
        OrderRefundDto orderRefundDto = ordersRefundService.queryByOrderId(orderId);
        ordersDetailAdminDTO.setRefundInfo(orderRefundDto);
        OrderDiscountForPcDTO orderDiscountForPcDTO = buildOrderDiscountDetailForPc(orderGoodsList, fee, activityList);
        ordersDetailAdminDTO.setOrderDiscount(orderDiscountForPcDTO);


        MerchantInfoDTO merchant = merchantUtils.getMerchant(ordersDetailAdminDTO.getBase().getMerchantId());
        if (merchant == null) {
            return ordersDetailAdminDTO;
        }
        if (merchant.getMerchantAttribute() != null
                && merchant.getMerchantAttribute().compareTo(MerchantAttribute.NOT_SELF_BUSINESS.getBusinessAttribute()) == 0) {
            // 非自营，不展示订单归属 v1.4.0 lh
            ordersDetailAdminDTO.getBase().setTakeAddress(StringUtils.EMPTY);
        } else {
            if (StringUtils.isBlank(ordersDetailAdminDTO.getBase().getTakeAddress())) {
                // 非自提订单，订单归属取门店名称
                ordersDetailAdminDTO.getBase().setTakeAddress(ordersDetailAdminDTO.getBase().getStoreName());
            }
        }

        return ordersDetailAdminDTO;
    }

    /**
     * PC端订单详情，组装优惠信息
     *
     * @param orderGoodsList
     * @param fee
     * @param activityList
     */
    private OrderDiscountForPcDTO buildOrderDiscountDetailForPc(List<OrdersDetailGoodsAdminDTO> orderGoodsList, OrdersDetailFeeAdminDTO fee, List<OrdersDetailActivityAdminDTO> activityList) {
        // 拼装订单优惠明细（PC）
        OrderDiscountForPcDTO orderDiscountForPcDTO = new OrderDiscountForPcDTO();
        List<OrderDiscountDetailForPcDTO> orderDiscountDetailForPcDTOList = Lists.newArrayList();
        // 运费优惠
        if (fee.getDeliveryPriceOriginal() != null
                && fee.getDeliveryPrice() != null
                && fee.getDeliveryPrice().compareTo(fee.getDeliveryPriceOriginal()) != 0) {
            String discountType = "配送费满减";
            if (fee.getDeliveryPriceFree() != null) {
                discountType = "满" + fee.getDeliveryPriceFree() + "免配送";
            }
            OrderDiscountDetailForPcDTO dto = buildOrderDiscountDetail(
                    fee.getDeliveryPriceOriginal(),
                    1,
                    discountType,
                    "配送费");
            orderDiscountDetailForPcDTOList.add(dto);
        }
        // 秒杀优惠／特价商品优惠／团购优惠
        for (OrdersDetailGoodsAdminDTO item : orderGoodsList) {
            if (item.getGoodsType() == null) {
                // 赠品
                continue;
            }
            String goodsName = item.getGoodsName();
            if (StringUtils.isNotEmpty(item.getGoodsSpecificationDesc())
                    && !"统一规格".equals(item.getGoodsSpecificationDesc())) {
                // 多规格
                goodsName += "（" + item.getGoodsSpecificationDesc() + "）";
            }
            BigDecimal discountBeforeFee = item.getDiscountBeforeFee();
            BigDecimal discountLaterFee = item.getDiscountLaterFee();
            if (discountBeforeFee.compareTo(discountLaterFee) != 0) {
                Integer quantity = item.getQuantity();
                BigDecimal discountPrice = discountBeforeFee.subtract(discountLaterFee).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (item.getGoodsType().compareTo(OrderGoodsTypeEnum.SECONDS.getValue()) == 0) {
                    // 秒杀
                    OrderDiscountDetailForPcDTO dto = buildOrderDiscountDetail(
                            discountPrice,
                            quantity,
                            "限时秒杀",
                            goodsName);
                    orderDiscountDetailForPcDTOList.add(dto);
                } else if (item.getGoodsType().compareTo(OrderGoodsTypeEnum.BULK.getValue()) == 0) {
                    // 团购
                    OrderDiscountDetailForPcDTO dto = buildOrderDiscountDetail(
                            discountPrice,
                            quantity,
                            "团购",
                            goodsName);
                    orderDiscountDetailForPcDTOList.add(dto);
                } else {
                    // 特价
                    if (item.getStorePrice().compareTo(item.getSalesPrice()) == 0) {
                        // 非特价（上面额外添加进来的）
                        continue;
                    }
                    if (item.getDiscountLimit() != null
                            && quantity.compareTo(item.getDiscountLimit()) > 0) {
                        quantity = item.getDiscountLimit();
                    }
                    OrderDiscountDetailForPcDTO dto = buildOrderDiscountDetail(
                            discountPrice,
                            quantity,
                            "特价商品",
                            goodsName);
                    orderDiscountDetailForPcDTOList.add(dto);
                }
            }
        }
        // 优惠券优惠
        if (CollectionUtils.isNotEmpty(activityList)) {
            for (OrdersDetailActivityAdminDTO item : activityList) {
                if (item.getConsumeCondition() == null || item.getConsumeCondition().compareTo(BigDecimal.valueOf(-1)) == 0) {
                    item.setConsumeCondition(BigDecimal.ZERO);
                }
                if (item.getUsefulCondition().compareTo(MarketingTicketUsefulConditionEnum.ORDER_TICKET.getStatus()) == 0) {
                    // 订单优惠
                    OrderDiscountDetailForPcDTO dto = buildOrderDiscountDetail(
                            item.getActivityDiscount(),
                            1,
                            "满" + item.getConsumeCondition().setScale(2, BigDecimal.ROUND_HALF_UP) + "减" + item.getTicketAmount().setScale(2, BigDecimal.ROUND_HALF_UP),
                            "订单");
                    orderDiscountDetailForPcDTOList.add(dto);
                } else if (item.getUsefulCondition().compareTo(MarketingTicketUsefulConditionEnum.GOODS_TICKET.getStatus()) == 0) {
                    // 商品优惠，需要把所有商品拼接
                    // 循环里面用了DB操作，一张订单最多使用两张优惠券
                    List<CateringMarketingGoodsEntity> marketingGoodsList = marketingGoodsClient
                            .selectListByMarketingId(item.getActivityId())
                            .getData();
                    if (CollectionUtils.isNotEmpty(marketingGoodsList)) {
                        List<String> goodsNameList = marketingGoodsList.stream().map(e -> {
                            String goodsName = e.getGoodsName();
                            if (StringUtils.isNotEmpty(e.getSkuValue())
                                    && !"统一规格".equals(e.getSkuValue())) {
                                goodsName += "（" + e.getSkuValue() + "）";
                            }
                            return goodsName;
                        }).collect(Collectors.toList());
                        OrderDiscountDetailForPcDTO dto = buildOrderDiscountDetail(
                                item.getActivityDiscount(),
                                1,
                                "商品优惠",
                                Joiner.on(",").join(goodsNameList));
                        orderDiscountDetailForPcDTOList.add(dto);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(orderDiscountDetailForPcDTOList)) {
            BigDecimal orderDetailDiscountTotalPrice = orderDiscountDetailForPcDTOList
                    .stream()
                    .map(OrderDiscountDetailForPcDTO::getDiscountPrice).reduce(BigDecimal::add).get();
            orderDiscountForPcDTO.setDiscountList(orderDiscountDetailForPcDTOList);
            orderDiscountForPcDTO.setDiscountTotalPrice(orderDetailDiscountTotalPrice);
        }
        return orderDiscountForPcDTO;
    }

    /**
     * 构建优惠明细
     *
     * @param discountPrice
     * @param discountAmount
     * @param discountType
     * @param discountObj
     * @return
     */
    private OrderDiscountDetailForPcDTO buildOrderDiscountDetail(
            BigDecimal discountPrice,
            Integer discountAmount,
            String discountType,
            String discountObj) {
        OrderDiscountDetailForPcDTO dto = new OrderDiscountDetailForPcDTO();
        dto.setDiscountType(discountType);
        dto.setDiscountPrice(discountPrice);
        dto.setDiscountAmount(discountAmount);
        dto.setDiscountObj(discountObj);
        return dto;
    }

    /**
     * 功能描述: 商户——订单详情查询
     *
     * @param orderId 请求参数
     * @return: 商户订单详情
     */
    @Override
    public OrdersDetailMerchantDTO orderDetailQueryMerchant(Long orderId) {
        OrdersDetailMerchantDTO ordersDetailMerchantDTO = this.cateringOrdersMapper.orderDetailQueryMerchant(orderId);
        if (ordersDetailMerchantDTO == null) {
            return null;
        }

        List<OrderDeliveryStatusDto> list = ordersDetailMerchantDTO.getOrderDeliveryStatusList();
        if (CollectionUtils.isNotEmpty(list)) {
            list.sort(new Comparator<OrderDeliveryStatusDto>() {
                @Override
                public int compare(OrderDeliveryStatusDto o1, OrderDeliveryStatusDto o2) {
                    return o1.getCreateTime().compareTo(o2.getCreateTime());
                }
            });
            // 配送状态记录按时间倒序
            ordersDetailMerchantDTO.setOrderDeliveryStatusList(list);
        }


        log.debug("支付成功后，查询订单详情：=" + JsonUtil.toJson(ordersDetailMerchantDTO));


        buildOrderInfoItem(ordersDetailMerchantDTO);
        MerchantInfoDTO merchant = merchantUtils.getMerchant(ordersDetailMerchantDTO.getMerchantId());
        if (merchant == null) {
            return ordersDetailMerchantDTO;
        }
        if (merchant.getMerchantAttribute() != null
                && merchant.getMerchantAttribute().compareTo(MerchantAttribute.NOT_SELF_BUSINESS.getBusinessAttribute()) == 0) {
            // 非自营，不展示订单归属 v1.4.0 lh
            ordersDetailMerchantDTO.setTakeAddress(StringUtils.EMPTY);
        }
        return ordersDetailMerchantDTO;
    }


    /**
     * 批量查询订单详情
     *
     * @param orderIdList
     * @return
     */
    @Override
    public List<OrdersDetailMerchantDTO> listOrderDetailQueryMerchant(List<Long> orderIdList) {
        if (CollectionUtils.isEmpty(orderIdList)) {
            return null;
        }
        List<OrdersDetailMerchantDTO> list = cateringOrdersMapper.listOrderDetailQueryMerchant(orderIdList);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (OrdersDetailMerchantDTO ordersDetailMerchantDTO : list) {
            buildOrderInfoItem(ordersDetailMerchantDTO);
        }
        return list;
    }

    /**
     * 订单详情相关枚举解析与赠品单独抽出
     *
     * @param ordersDetailMerchantDTO
     */
    private void buildOrderInfoItem(OrdersDetailMerchantDTO ordersDetailMerchantDTO) {
        List<OrdersGoodsMerchantDTO> goods = ordersDetailMerchantDTO.getOrdersGoods();
        List<OrdersGoodsMerchantDTO> ordersGoods = goods.stream().filter(i -> !i.getGifts()).collect(Collectors.toList());

//        多规格商品规格拼在商品名称后面
        for (OrdersGoodsMerchantDTO dto : ordersGoods) {
            if (StringUtils.isEmpty(dto.getGoodsSpecificationDesc())) {
                continue;
            }
            if (Objects.equals(GoodsSpecTypeEnum.UNIFIED_SPEC.getStatus(), dto.getGoodsSpecType())) {
                dto.setGoodsName(dto.getGoodsName());
            } else {
                dto.setGoodsName(dto.getGoodsName() + "(" + dto.getGoodsSpecificationDesc() + ")");
            }
        }

        List<OrdersGoodsMerchantDTO> ordersGiftGoods = goods.stream().filter(i -> i.getGifts()).collect(Collectors.toList());
        // 构建赠品信息
        List<OrdersGiftGoodsMerchantDTO> ordersGiftGoodsList = new ArrayList<>();
        ordersGiftGoods.forEach(obj -> {
            OrdersGiftGoodsMerchantDTO ordersGiftGoodsMerchantDTO = new OrdersGiftGoodsMerchantDTO();
            BeanUtils.copyProperties(obj, ordersGiftGoodsMerchantDTO);
            ordersGiftGoodsList.add(ordersGiftGoodsMerchantDTO);
        });
        ordersDetailMerchantDTO.setOrdersGoods(ordersGoods);

        Integer orderGoodsSize = ordersGoods.stream().map(OrdersGoodsMerchantDTO::getQuantity).reduce(Integer::sum).get();
        ordersDetailMerchantDTO.setOrderGoodsSize(orderGoodsSize);

        ordersDetailMerchantDTO.setOrdersGiftGoods(ordersGiftGoodsList);
        MerchantOrderStatusEnum statusEnum = this.ordersSupport.orderStatusConver(ordersDetailMerchantDTO.getOrderStatus(), ordersDetailMerchantDTO.getRefundStatus(), ordersDetailMerchantDTO.getAfterSales());
        ordersDetailMerchantDTO.setAppOrderStatus(statusEnum.getCode());
        ordersDetailMerchantDTO.setOrderStatusStr(statusEnum.getDesc());
        ordersDetailMerchantDTO.setRefundReason(ordersSupport.getRefundReasonStr(ordersDetailMerchantDTO.getRefundReasonList()));
        //返回货物收货状态
        if (ordersDetailMerchantDTO.getCargoStatus() != null) {
            ordersDetailMerchantDTO.setCargoStatusStr(CargoStatusEnum.parse(ordersDetailMerchantDTO.getCargoStatus()).getDesc());
        }
        List<OrdersGoodsMerchantDTO> ordersGoodsList = ordersDetailMerchantDTO.getOrdersGoods();
        if (BaseUtil.judgeList(ordersGoodsList)) {
            // 计算商品合计金额
            BigDecimal goodsAmount = ordersGoodsList.stream().map(obj -> {
                return obj.getStorePrice() == null || BigDecimal.ZERO.compareTo(obj.getStorePrice()) == 0 ? obj.getTransactionPrice() : obj.getStorePrice();
            }).reduce((x, y) -> x.add(y)).get();
            ordersDetailMerchantDTO.setGoodsAmount(goodsAmount);
        }
        if (StringUtils.isBlank(ordersDetailMerchantDTO.getTakeAddress())) {
            // 非自提订单，订单归属取门店名称
            ordersDetailMerchantDTO.setTakeAddress(ordersDetailMerchantDTO.getShopName());
        }

        // 订单配送状态
        List<OrderDeliveryStatusDto> orderDeliveryStatusList = ordersDetailMerchantDTO.getOrderDeliveryStatusList();
        if (CollectionUtils.isNotEmpty(orderDeliveryStatusList)) {

            orderDeliveryStatusList.sort(new Comparator<OrderDeliveryStatusDto>() {
                @Override
                public int compare(OrderDeliveryStatusDto o1, OrderDeliveryStatusDto o2) {
                    return o2.getCreateTime().compareTo(o1.getCreateTime());
                }
            });

            for (OrderDeliveryStatusDto dto : orderDeliveryStatusList) {
                dto.setOrderStatusDesc(OrderDeliveryStatusEnum.parseCode(dto.getOrderStatus()));
            }
            ordersDetailMerchantDTO.setOrderDeliveryStatusList(orderDeliveryStatusList);
        }
    }

    /**
     * 功能描述: 微信——订单详情查询
     *
     * @param orderId 请求参数
     * @return: 后台订单详情
     */
    @Override
    public OrdersDetailWxDTO orderDetailQueryWx(Long orderId, Long userId) {
        OrdersDetailWxDTO ordersDetailWxDTO = this.cateringOrdersMapper.orderDetailQueryWx(orderId);
        if (ordersDetailWxDTO != null) {
            ordersDetailWxDTO = this.getOrdersDetailWxDto(ordersDetailWxDTO, userId);
        }

        /**
         * 小程序停留在订单详情倒计时页面，到点刷新，这时服务端定时任务还没来得及将订单状态更新，导致订单状态不一致。
         *
         * 定时任务，超时未取餐自动关闭订单
         *
         * @author lh
         * @version 1.2.0
         */
        if (OrderStatusEnum.WAIT_TAKEN.getValue().equals(ordersDetailWxDTO.getBase().getOrderStatus())
                && ordersDetailWxDTO.getDetailDelivery().getEstimateTime() != null
                && ordersDetailWxDTO.getDetailDelivery().getEstimateTime().compareTo(LocalDateTime.now()) < 0) {
            ordersDetailWxDTO.getBase().setOrderStatus(OrderStatusEnum.OFF.getValue());
            ordersDetailWxDTO.getBase().setOffReason("订单超时未提取");
        }

        /**
         * 延迟消息，超时未支付，自动取消订单
         * @author lh
         * @version 1.2.0
         */
        if (OrderStatusEnum.UNPAID.getValue().equals(ordersDetailWxDTO.getBase().getOrderStatus())
                && ordersDetailWxDTO.getBase().getPayDeadline() != null
                && ordersDetailWxDTO.getBase().getPayDeadline().compareTo(LocalDateTime.now()) < 0) {
            ordersDetailWxDTO.getBase().setOrderStatus(OrderStatusEnum.CANCELED.getValue());
            ordersDetailWxDTO.getBase().setOffReason("支付超时，订单已取消");
        }


        ordersDetailWxDTO.setIsShopDel(Boolean.FALSE);
        if (shopClient.getShopFromCache(ordersDetailWxDTO.getShopId()) == null) {
            // 缓存里面删掉了，说明门店被删掉了
            ordersDetailWxDTO.setIsShopDel(Boolean.TRUE);
        }


        ordersDetailWxDTO.getBase().setStoreId(ordersDetailWxDTO.getShopId());
        if (StringUtils.isEmpty(ordersDetailWxDTO.getDetailDelivery().getStoreId())) {
            ordersDetailWxDTO.getDetailDelivery().setStoreId(ordersDetailWxDTO.getShopId().toString());
        }
        return ordersDetailWxDTO;
    }

    private OrdersDetailWxDTO getOrdersDetailWxDto(OrdersDetailWxDTO dto, Long userId) {

        Long shopId = dto.getShopId();
        ShopInfoDTO shop = this.orderUtils.getShop(shopId);
        if (shop == null) {
            log.error("获取商家信息失败 shop = null");
            throw new CustomException("获取商家信息失败");
        }
        // 自提点信息
        Result<MerchantsPickupAddressDTO> merchantPickupAddress = this.merchantPickupAdressClient.getMerchantPickupAddress(shopId);
        if (merchantPickupAddress.failure()) {
            throw new CustomException("获取自取门店失败");
        }
        MerchantsPickupAddressDTO data = merchantPickupAddress.getData();
        if (data == null) {
            throw new CustomException("获取自取门店失败");
        }


        //        店铺默认正常状态
        dto.setIsWxShow(0);
        Result result = shopClient.isWxShow(shopId);
        if (result.success() && result.getCode() == 0) {
            dto.setIsWxShow(1);
        }


        // 百度经纬度转腾讯经纬度，因为我们存的经纬度是百度的，小程序看的是腾讯的。所以需要转一次
        String[] split = data.getMapCoordinate().split(",");
        double[] doubles = GpsCoordinateUtils.calBD09toGCJ02(Double.valueOf(split[0]), Double.valueOf(split[1]));
        String mapCoordinate = doubles[0] + "," + doubles[1];

        dto.getBase().setStoreName(shop.getShopName());
        dto.getBase().setStorePicture(shop.getDoorHeadPicture());
        dto.getBase().setAddressFull(shop.getAddressFull());
        dto.getBase().setMapCoordinate(mapCoordinate);
        dto.getBase().setStoreId(shopId);
        if (OrderTypeEnum.SHARE_BILL.getStatus().equals(dto.getBase().getOrderType())) {
            List<OrdersDetailGoodsWxDTO> goods = dto.getGoods().stream().filter(obj -> !obj.getGifts()).collect(Collectors.toList());
            Map<Long, List<OrdersDetailGoodsWxDTO>> collect = goods.stream().collect(Collectors.groupingBy(OrdersDetailGoodsWxDTO::getShareBillUserId));
            List<ShareBillCalculateGoodsInfoDTO> shareBillGoodsList = new ArrayList<>();
            List<ShareBillCalculateGoodsInfoDTO> shareBillGoodsMasterList = new ArrayList<>();
            collect.forEach((key, value) -> {
                List<OrdersCalculateGoodsInfoDTO> goodsInfoList = BaseUtil.objToObj(value, OrdersCalculateGoodsInfoDTO.class);
                String shareBillUserName = value.get(0).getShareBillUserName();
                String shareBillUserAvatar = value.get(0).getShareBillUserAvatar();
                ShareBillCalculateGoodsInfoDTO shareBillGoodsInfo = this.ordersSupport.getWxBillUser(key, shareBillUserName, shareBillUserAvatar, goodsInfoList);
                // 将拼单发起人信息单独放在一个list
                if (key.equals(userId)) {
                    shareBillGoodsMasterList.add(shareBillGoodsInfo);
                } else {
                    shareBillGoodsList.add(shareBillGoodsInfo);
                }
            });
            shareBillGoodsMasterList.addAll(shareBillGoodsList);
            dto.setShareBillGoods(shareBillGoodsMasterList);
        }
        return dto;
    }

    /**
     * 功能描述: 后台——充值消费列表查询
     *
     * @param paramDTO 请求参数
     * @return: 后台充值消费列表信息
     */
    @Override
    public IPage<TopUpConsumeListAdminDTO> topUpConsumeListQueryAdmin(TopUpConsumeListParamAdminDTO paramDTO) {
        Page<TopUpConsumeListAdminDTO> page = new Page<>(paramDTO.getPageNo(), paramDTO.getPageSize());
        return this.cateringOrdersMapper.topUpConsumeListQueryAdmin(page, paramDTO);
    }

    /**
     * @Description 商户——【订单验证二次确认，取餐时间是否是当天的订单：是：true，否：false】
     * @Version 1.0.1
     * @Date 2020/3/12 0012 15:38
     */
    @Override
    public EstimateTimeCheckDTO estimateTimeCheck(OrdersCheckParamDTO paramDTO) {
        CateringOrdersEntity cateringOrdersEntity = this.ordersSupport.getOrderByCode(paramDTO);
        OrderDelivery deliveryEntity = this.cateringOrdersDeliveryService.getByOrderId(cateringOrdersEntity.getId());
        if (deliveryEntity == null) {
            throw new CustomException("获取取餐信息失败");
        }
        //达达配送 无法核销校验
        LambdaQueryWrapper<CateringOrderDeliveryStatusHistoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringOrderDeliveryStatusHistoryEntity::getOrderId, cateringOrdersEntity.getId())
                .orderByDesc(CateringOrderDeliveryStatusHistoryEntity::getCreateTime).last("limit 1");
        CateringOrderDeliveryStatusHistoryEntity deliveryStatusHistoryEntity = cateringOrderDeliveryStatusHistoryService.getOne(queryWrapper);

        if (Objects.nonNull(deliveryStatusHistoryEntity)) {
            Integer orderStatus = deliveryStatusHistoryEntity.getOrderStatus();
            List<Integer> checkStatus = OrderDeliveryStatusEnum.getCheckStatus();
            if (checkStatus.contains(orderStatus)) {
                throw new CustomException("该订单正在配送中");
            }
        }
        Boolean toDay = true;
        String describe = null;
        LocalDate now = LocalDate.now();
        LocalDate localDate = null;
        //配送单，并且是立即送达，取立即送达时间
        if (DeliveryWayEnum.Delivery.getCode().equals(cateringOrdersEntity.getDeliveryWay()) && Objects.nonNull(deliveryEntity.getImmediateDeliveryTime())) {
            localDate = deliveryEntity.getImmediateDeliveryTime().toLocalDate();
        } else {
            localDate = deliveryEntity.getEstimateTime().toLocalDate();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月d日");
        String format = formatter.format(localDate);
        if (!now.equals(localDate)) {
            toDay = false;
            describe = "该订单为" + format + "订单，是否现在核销";
        }
        EstimateTimeCheckDTO estimateTimeCheckDTO = new EstimateTimeCheckDTO();
        estimateTimeCheckDTO.setToDay(toDay);
        estimateTimeCheckDTO.setDescribe(describe);
        return estimateTimeCheckDTO;
    }

    /**
     * 功能描述: 商户——送达、自取验证
     *
     * @param paramDTO 请求参数
     * @return: 验证结果信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrdersCheckDTO orderCheckByCode(OrdersCheckParamDTO paramDTO) {
        CateringOrdersEntity cateringOrdersEntity = this.ordersSupport.getOrderByCode(paramDTO);
        LambdaQueryWrapper<CateringOrdersDeliveryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringOrdersDeliveryEntity::getOrderId, cateringOrdersEntity.getId());
        CateringOrdersDeliveryEntity cateringOrdersDeliveryEntity = this.cateringOrdersDeliveryService.getOne(queryWrapper);
        if (cateringOrdersDeliveryEntity == null) {
            throw new CustomException("获取配送信息失败");
        }
        LambdaQueryWrapper<CateringOrdersEntity> ordersQueryWrapper = new LambdaQueryWrapper<>();
        ordersQueryWrapper.eq(CateringOrdersEntity::getId, cateringOrdersEntity.getId());
        OrdersCheckDTO ordersCheckDTO = new OrdersCheckDTO();

        ordersCheckDTO.setMemberId(cateringOrdersEntity.getMemberId());
        ordersCheckDTO.setMerchantId(cateringOrdersEntity.getMerchantId());

        LocalDateTime now = LocalDateTime.now();
        CateringOrdersEntity ordersEntity = new CateringOrdersEntity();

        ordersEntity.setStatus(OrderStatusEnum.DONE.getValue());
        // 设置可以申请售后
        ordersEntity.setCanAfterSales(true);
        ordersEntity.setUpdateBy(paramDTO.getOrderId());
        ordersEntity.setUpdateName(paramDTO.getShopName());
        ordersEntity.setUpdateTime(now);
        boolean updateOrder = this.update(ordersEntity, ordersQueryWrapper);
        if (updateOrder) {
            cateringOrdersDeliveryEntity.setActualTime(now);
            cateringOrdersDeliveryEntity.setUpdateBy(paramDTO.getOrderId());
            cateringOrdersDeliveryEntity.setUpdateName(paramDTO.getShopName());
            cateringOrdersDeliveryEntity.setUpdateTime(now);
            boolean upRes = this.cateringOrdersDeliveryService.updateById(cateringOrdersDeliveryEntity);
            if (upRes) {
                ordersCheckDTO.setOrderId(cateringOrdersEntity.getId());
                ordersCheckDTO.setMsg("验证成功");
                // 记录订单进度
                this.ordersSupport.saveOperation(cateringOrdersEntity.getId(), cateringOrdersEntity.getOrderNumber(), OrderOperationEnum.DONE,
                        paramDTO.getMerchantId(), paramDTO.getShopName(), null, OrderOffTypeEnum.EMPLOYEE_CANCEL.getCode());

                // 再次发放优惠卷  v1.1.0
                this.ordersSupport.sendTicketAgain(cateringOrdersEntity.getId());

                // v1.3.0 记录优惠券使用情况，核销订单记录
                List<Long> ticketIds = cateringOrdersMapper.listOrderDiscountIds(cateringOrdersEntity.getId());
                if (CollectionUtils.isNotEmpty(ticketIds)) {
                    Boolean isFirstOrder = isFirstOrder(cateringOrdersEntity.getMemberId(), cateringOrdersDeliveryEntity.getStoreId());
                    TicketDataRecordDTO dataRecordDTO = new TicketDataRecordDTO();
                    dataRecordDTO.setMerchantId(cateringOrdersEntity.getMerchantId());
                    dataRecordDTO.setShopId(cateringOrdersEntity.getStoreId());
                    dataRecordDTO.setOrderId(cateringOrdersEntity.getId());
                    dataRecordDTO.setOrderAmount(cateringOrdersEntity.getPaidAmount());
                    dataRecordDTO.setTicketIds(ticketIds);
                    dataRecordDTO.setNewMember(isFirstOrder);
                    dataRecordDTO.setUserId(cateringOrdersEntity.getMemberId());
                    dataRecordDTO.setDiscountBeforeFee(cateringOrdersEntity.getDiscountBeforeFee());
                    marketingTicketActivityClient.saveTicketDataRecord(dataRecordDTO);
                }


                return ordersCheckDTO;
            }
        }
        throw new CustomException("验证失败");
    }

    /**
     * @Description 商户——【订单分布情况】
     * @Date 2020/3/12 0012 15:38
     */
    @Override
    public List<OrdersDistributionDTO> orderDistribution(OrdersDistributionParamDTO paramDTO) {
        return this.cateringOrdersMapper.orderDistribution(paramDTO);
    }

    /**
     * 订单详情——内部调用
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    @Override
    public OrdersDetailDTO orderDetail(Long orderId) {
        OrdersDetailDTO ordersDetailDTO = this.cateringOrdersMapper.orderDetail(orderId);

        if (ordersDetailDTO == null) {
            return null;
        }
        Long storeId = ordersDetailDTO.getStoreId();
        List<OrdersDetailGoodsDTO> goods = ordersDetailDTO.getGoods();
        for (OrdersDetailGoodsDTO good : goods) {
            good.setShopId(storeId);
        }

        return ordersDetailDTO;
    }

    /**
     * 取消订单
     *
     * @param dto 取消订单操作信息
     * @return 取消订单操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(OrderOffDTO dto) {
        CateringOrdersEntity entity = this.cateringOrdersMapper.selectById(Long.valueOf(dto.getOrderId()));
        if (entity == null) {
            throw new CustomException("订单不存在");
        }
        // 如果是客户取消订单、或者超时未支付取消订单，则需判断订单是否是待支付订单
        if (OrderOffTypeEnum.MEMBER_CANCEL.getCode().equals(dto.getOffType()) || OrderOffTypeEnum.AUTO_OFF.getCode().equals(dto.getOffType())) {
            if (!OrderStatusEnum.UNPAID.getValue().equals(entity.getStatus())) {
                throw new CustomException("订单不是待支付，不能完成取消");
            }
        }
        int cancel = this.ordersSupport.cancel(dto, OrderStatusEnum.UNPAID);
        if (cancel == 1) {
            int save = this.ordersSupport.saveOperation(entity.getId(), entity.getOrderNumber(), OrderOperationEnum.CANCELED, dto.getOffUserId(),
                    dto.getOffUserName(), dto.getOffUserPhone(), dto.getOffType());
            if (save == 1) {

                /* 订单取消成功 */

                /* 更新普通／特价商品库存 */
                List<OrderGoods> orderGoodsList = cateringOrdersGoodsService.getByOrderId(Long.valueOf(dto.getOrderId()));
                List<OrderGoods> ordinaryGoodsList = orderGoodsList
                        .stream()
                        .filter(i ->
                                i.getGoodsType() != null
                                        && (i.getGoodsType().equals(OrderGoodsTypeEnum.ORDINARY.getValue())
                                        || i.getGoodsType().equals(OrderGoodsTypeEnum.SPECIAL.getValue())))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ordinaryGoodsList)) {
                    ConcurrentHashMap<String/* 商品SKU编码 */, Integer/* 订单商品数量 */> skuMap = new ConcurrentHashMap<>(16);

                    /* 拼单，同一个商品SKU，会有多人购买 */
                    for (OrderGoods item : ordinaryGoodsList) {
                        if (skuMap.containsKey(item.getGoodsSkuCode())) {
                            skuMap.put(item.getGoodsSkuCode(), skuMap.get(item.getGoodsSkuCode()) + item.getQuantity());
                        }
                        skuMap.put(item.getGoodsSkuCode(), item.getQuantity());
                    }

                    for (String skuCode : skuMap.keySet()) {
                        /*库存做加法*/
                        skuMap.put(skuCode, skuMap.get(skuCode));
                    }
                    shopGoodsClient.batchUpdateSkuStock(entity.getStoreId(), skuMap);
                }

                // 如果是秒杀商品，需要还原商品库存
                log.debug("待支付订单已取消，发送还原秒杀商品库存消息，orderId=【{}】", entity.getId());
                this.sendSeckillMsg(entity.getId(), false);
                // 获取用户优惠卷信息，并还原优惠卷
                LambdaQueryWrapper<CateringOrdersDiscountsEntity> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(CateringOrdersDiscountsEntity::getOrderId, entity.getId());
                queryWrapper.eq(CateringOrdersDiscountsEntity::getDiscountType, OrderDiscountTypeEnum.TICKET.getCode());
                List<CateringOrdersDiscountsEntity> ticketList = this.cateringOrdersDiscountsService.list(queryWrapper);
                if (CollectionUtils.isEmpty(ticketList)) {
                    return true;
                }
                for (CateringOrdersDiscountsEntity item : ticketList) {
                    this.userTicketClient.returnTicket(item.getDiscountId());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 团购失败，修改订单状态为已取消
     *
     * @param dto 取消订单操作信息
     * @return 取消订单操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int groupFailCancelOrder(OrderOffDTO dto) {
        CateringOrdersEntity entity = this.cateringOrdersMapper.selectById(Long.valueOf(dto.getOrderId()));
        if (entity == null) {
            throw new CustomException("订单不存在");
        }
        if (!OrderStatusEnum.GROUP.getValue().equals(entity.getStatus())) {
            throw new CustomException("订单不是团购中，不能完成取消");
        }
        int update = this.ordersSupport.cancel(dto, OrderStatusEnum.GROUP);
        return update;
    }

    /**
     * 团购成功，修改订单状态为带配送、待取餐
     *
     * @param orderNos 订单编号
     * @return 修改订单操作结果
     */
    @Override
    public void upOrderStatus(List<String> orderNos) {
        baseMapper.updateBatchStatus(orderNos);
    }

    @Override
    public IntegralRuleVo appraiseRule(Integer userType) {
        IntegralRuleVo integralRuleVo = new IntegralRuleVo();
        Integer integer = this.cateringIntegralActivityService.appraiseHighestIntegral(userType);
        List<IntegralRuleListVo> appraiseRuleList = this.cateringIntegralActivityService.appraiseRuleList(userType);
        integralRuleVo.setHighIntegral(integer);
        integralRuleVo.setAppraiseRuleList(appraiseRuleList);
        return integralRuleVo;
    }

    /**
     * 用户评价订单商品
     *
     * @return 订单操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentDTO commentOrder(CommentOrderDTO dto) {
        log.debug("订单【评价信息】：{}", dto);
        Long orderId = Long.valueOf(dto.getOrderId());
        CommentDTO commentDTO = new CommentDTO();
        //和订单评论同一把锁
        cache.tryLockAndRun(CacheLockUtil.orderCommentLock(orderId), CacheLockUtil.EXPIRE, TimeUnit.SECONDS, () -> {
            if (dto.getContent() != null && !"".equals(dto.getContent())) {
                Result result = this.contentCheckService.checkText(dto.getContent());
                if (result.failure()) {
                    log.error("评论内容中有敏感词汇 content=【{}】", dto.getContent());
                    throw new CustomException("评论内容不能包含敏感词汇");
                }
            }
            // 获取需要评论的订单信息，并修改订单的评论状态为：已评论
            CateringOrdersEntity ordersEntity = getCommentOrder(orderId, false);
            // 订单评论信息实体组装
            CateringOrdersAppraiseEntity cateringOrdersAppraiseEntity = this.commentOrderInfo(dto, ordersEntity);
            boolean save = this.cateringOrdersAppraiseService.save(cateringOrdersAppraiseEntity);
            log.debug("保存订单评价信息【cateringOrdersAppraiseEntity】：{}，结果：【{}】", cateringOrdersAppraiseEntity, save);
            if (save) {
                commentDTO.setAppraiseId(cateringOrdersAppraiseEntity.getId());
                commentDTO.setMerchantId(cateringOrdersAppraiseEntity.getMerchantId());
                Integer integerTotal = 0;

                commentDTO.setIntegral(integerTotal);
            }
        });
        return commentDTO;
    }

    /**
     * 功能描述:订单完成超时未评价，系统自动五星好评
     *
     * @param
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 16:03
     * @since v 1.1.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentDTO autoCommentOrder(Long orderId) {
        CommentDTO commentDTO = new CommentDTO();
        //和订单评论同一把锁
        cache.tryLockAndRun(CacheLockUtil.orderCommentLock(orderId), CacheLockUtil.EXPIRE, TimeUnit.SECONDS, () -> {
            // 获取需要评论的订单信息，并修改订单的评论状态为：已评论
            CateringOrdersEntity commentOrder = getCommentOrder(orderId, false);
            CommentOrderDTO dto = new CommentOrderDTO();
            dto.setUserId(commentOrder.getMemberId());
            dto.setUserAvatar(commentOrder.getAvatar());
            dto.setUserNickname(commentOrder.getMemberName());
            dto.setAppraiseType(AppraiseTypeEnum.SYSTEM.getValue());
            dto.setOrderId(String.valueOf(commentOrder.getId()));
            dto.setPack(5);
            dto.setService(5);
            dto.setTaste(5);
            // 订单评论信息实体组装
            CateringOrdersAppraiseEntity cateringOrdersAppraiseEntity = this.commentOrderInfo(dto, commentOrder);
            boolean save = this.cateringOrdersAppraiseService.save(cateringOrdersAppraiseEntity);
            log.debug("保存订单评价信息【cateringOrdersAppraiseEntity】：{}，结果：【{}】", cateringOrdersAppraiseEntity, save);
            commentDTO.setMerchantId(commentOrder.getMemberId());
        });
        return commentDTO;
    }

    /**
     * 功能描述: 获取需要评论的订单信息，并修改订单的评论状态为：已评论
     *
     * @param orderId 订单Id
     * @param type    操作类型（true：用户评价，false：系统自动评价）
     * @return void
     * @author xie-xi-jie
     * @date 2020/5/21 16:07
     * @since v 1.1.0
     */
    public CateringOrdersEntity getCommentOrder(Long orderId, Boolean type) {
// 查询订单DB信息
        CateringOrdersEntity ordersEntity = this.getById(orderId);
        if (ordersEntity == null) {
            throw new CustomException(ErrorCode.ORDER_APPRAISE_ERROE, "订单不存在");
        }
        if (!OrderStatusEnum.DONE.getValue().equals(ordersEntity.getStatus())) {
            throw new CustomException(ErrorCode.ORDER_APPRAISE_ERROE, "订单不是已完成，不能进行评价");
        }
        if (ordersEntity.getComment()) {
            throw new CustomException(ErrorCode.ORDER_APPRAISE_ERROE, "订单已评论，请勿重复提交");
        }
        CateringOrdersEntity entity = new CateringOrdersEntity();
        entity.setId(ordersEntity.getId());
        entity.setComment(true);
        if (!type) {
            entity.setUpdateBy(OrderOffTypeEnum.AUTO_OFF.getCode().longValue());
            entity.setUpdateName(OrderOffTypeEnum.AUTO_OFF.getDesc());
        }
        entity.setUpdateTime(LocalDateTime.now());
        this.updateById(entity);
        return ordersEntity;
    }

    /**
     * 今日营业情况——商户端
     *
     * @return 今日营业情况
     */
    @Override
    public OrdersCountMerchantDTO ordersCountMerchant(MerchantBaseDTO param) {
        OrdersCountMerchantDTO ordersCountMerchantDTO = new OrdersCountMerchantDTO();


        // 今日
        BizDataForMerchantDTO bizData = this.bizDataFroMerchant(param.getShopId(), DateTimeUtil.now(), null, null);

        if (bizData == null) {
            bizData = new BizDataForMerchantDTO();
            bizData.setOrderTotalAmount(0);
            bizData.setTotalPrice(BigDecimal.ZERO);
            bizData.setActualTotalPrice(BigDecimal.ZERO);
        }


        // 单量
        ordersCountMerchantDTO.setOrderTotal(bizData.getOrderTotalAmount());
        // 营业额
        ordersCountMerchantDTO.setReceivableAmount(bizData.getTotalPrice());
        // 实收
        ordersCountMerchantDTO.setActualAmount(bizData.getActualTotalPrice());

        DeliveryGoodsParamDTO dto = BaseUtil.objToObj(param, DeliveryGoodsParamDTO.class);
        dto.setTime(0);
        List<DeliveryGoodsVo> todayGoodsVos = this.cateringOrdersMapper.deliveryGoodsInfo(dto);
        Integer todayGoodsTotal = 0;
        if (BaseUtil.judgeList(todayGoodsVos)) {
            todayGoodsTotal = todayGoodsVos.stream().map(DeliveryGoodsVo::getQuantity).reduce((x, y) -> x + y).get();
        }
        dto.setTime(1);
        List<DeliveryGoodsVo> tomorrowGoodsVos = this.cateringOrdersMapper.deliveryGoodsInfo(dto);
        Integer tomorrowGoodsTotal = 0;
        if (BaseUtil.judgeList(tomorrowGoodsVos)) {
            tomorrowGoodsTotal = tomorrowGoodsVos.stream().map(DeliveryGoodsVo::getQuantity).reduce((x, y) -> x + y).get();
        }
        ordersCountMerchantDTO.setTodayGoodsTotal(todayGoodsTotal);
        ordersCountMerchantDTO.setTomorrowGoodsTotal(tomorrowGoodsTotal);

        // 昨天数据
        // 昨日
        BizDataForMerchantDTO bizDataYesterday = this.bizDataFroMerchant(param.getShopId(), DateTimeUtil.yesterday(), null, null);
        if (bizDataYesterday == null) {
            bizDataYesterday = new BizDataForMerchantDTO();
            bizDataYesterday.setOrderTotalAmount(0);
            bizDataYesterday.setTotalPrice(BigDecimal.ZERO);
            bizDataYesterday.setActualTotalPrice(BigDecimal.ZERO);
        }
        // 单量
        ordersCountMerchantDTO.setOrderTotalYesterday(bizDataYesterday.getOrderTotalAmount());
        // 营业额
        ordersCountMerchantDTO.setReceivableAmountYesterday(bizDataYesterday.getTotalPrice());
        // 实收
        ordersCountMerchantDTO.setActualAmountYesterday(bizDataYesterday.getActualTotalPrice());


        return ordersCountMerchantDTO;
    }

    /**
     * 订单查询状态数量统计——商户端
     *
     * @param param 商户信息
     * @return 订单查询状态数量
     */
    @Override
    public OrdersStatusCountDTO ordersStatusCountMerchant(OrdersListMerchantParamDTO param) {
        OrdersStatusCountDTO ordersStatusCountDTO = new OrdersStatusCountDTO();
        Integer unfinishedNum = this.cateringOrdersMapper.unfinishedNum(param);
        Integer finishedNum = this.cateringOrdersMapper.finishedNum(param);
        Integer cancelNum = this.cateringOrdersMapper.cancelNum(param);
        Integer failureNum = this.cateringOrdersMapper.failureNum(param);
        Integer unRefundNum = this.cateringOrdersMapper.unRefundNum(param);
        Integer refundNum = this.cateringOrdersMapper.refundNum(param);
        ordersStatusCountDTO.setUnfinished(unfinishedNum);
        ordersStatusCountDTO.setFinished(finishedNum);
        ordersStatusCountDTO.setCancel(cancelNum);
        ordersStatusCountDTO.setFailure(failureNum);
        ordersStatusCountDTO.setUnRefund(unRefundNum);
        ordersStatusCountDTO.setRefund(refundNum);
        return ordersStatusCountDTO;
    }

    @Override
    public Order getByOrderNumber(String orderNumber) {
        LambdaQueryWrapper<CateringOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersEntity::getOrderNumber, orderNumber);
        CateringOrdersEntity ordersEntity = baseMapper.selectOne(wrapper);
        return BaseUtil.objToObj(ordersEntity, Order.class);
    }

    @Override
    public Order getByIdAndUserId(Long orderId, Long userId) {
        LambdaQueryWrapper<CateringOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersEntity::getId, orderId);
        wrapper.eq(CateringOrdersEntity::getMemberId, userId);
        CateringOrdersEntity ordersEntity = baseMapper.selectOne(wrapper);
        return BaseUtil.objToObj(ordersEntity, Order.class);
    }

    /**
     * 订单评论信息实体组装
     *
     * @param dto 订单提交的评论信息
     * @return ordersEntity 订单评论信息实体
     */
    public CateringOrdersAppraiseEntity commentOrderInfo(CommentOrderDTO dto, CateringOrdersEntity ordersEntity) {
        CateringOrdersAppraiseEntity cateringOrdersAppraiseEntity = new CateringOrdersAppraiseEntity();
        BeanUtils.copyProperties(dto, cateringOrdersAppraiseEntity);
        // 评论评分 = （服务评分+口味评分+保证评分）/ 3
        BigDecimal appraiseLevel = new BigDecimal(dto.getTaste() + dto.getService() + dto.getPack())
                .divide(new BigDecimal(3), 1, BigDecimal.ROUND_HALF_UP);
        // 评论评分(1:差评 2-3:中评 4-5:好评)
        Integer status;
        if (appraiseLevel.compareTo(new BigDecimal(AppraiseLabelEnum.BAD.getValue())) <= 0) {
            status = 3;
        } else if (appraiseLevel.compareTo(new BigDecimal(AppraiseLabelEnum.GOOD.getValue())) >= 0) {
            status = 1;
        } else {
            status = 2;
        }
        cateringOrdersAppraiseEntity.setId(IdWorker.getId());
        cateringOrdersAppraiseEntity.setMerchantId(ordersEntity.getMerchantId());
        cateringOrdersAppraiseEntity.setAppraiseLevel(appraiseLevel);
        cateringOrdersAppraiseEntity.setStatus(status);
        cateringOrdersAppraiseEntity.setOrderId(ordersEntity.getId());
        cateringOrdersAppraiseEntity.setOrderNumber(ordersEntity.getOrderNumber());
        cateringOrdersAppraiseEntity.setStoreId(ordersEntity.getStoreId());
        cateringOrdersAppraiseEntity.setStoreName(ordersEntity.getStoreName());
        cateringOrdersAppraiseEntity.setStorePicture(ordersEntity.getStorePicture());
        cateringOrdersAppraiseEntity.setCreateTime(LocalDateTime.now());
        return cateringOrdersAppraiseEntity;
    }

    /**
     * 功能描述: 发送秒杀库存还原MQ
     *
     * @param orderId 订单ID
     * @return: void
     */
    @Override
    public void sendSeckillMsg(Long orderId, Boolean pay) {
        OrdersDetailDTO ordersDetailDTO = this.orderDetail(orderId);
        if (ordersDetailDTO != null) {
            List<OrderSecKillMqDTO> secKillMqDTOList = new ArrayList<>();
            List<OrdersDetailGoodsDTO> goods = ordersDetailDTO.getGoods();
            goods.forEach(obj -> {
                if (OrderGoodsTypeEnum.SECONDS.getValue().equals(obj.getGoodsType())) {
                    OrderSecKillMqDTO orderSecKillMqDTO = new OrderSecKillMqDTO();
                    orderSecKillMqDTO.setGoodsId(obj.getGoodsId());
                    orderSecKillMqDTO.setNumber(obj.getQuantity());
                    orderSecKillMqDTO.setUserId(ordersDetailDTO.getMemberId());
                    orderSecKillMqDTO.setOrder(Boolean.TRUE);
                    orderSecKillMqDTO.setPay(pay);
                    orderSecKillMqDTO.setSeckillEventId(obj.getSeckillEventId());
                    secKillMqDTOList.add(orderSecKillMqDTO);
                }
            });
            if (secKillMqDTOList != null && secKillMqDTOList.size() > 0) {
                // 发送取消订单还原秒杀商品库存消息
                this.orderSecKillMqSender.sendSeckillMsg(secKillMqDTOList);
            }
        }
    }

    @Override
    public Boolean firstOrder(Long userId) {
        LambdaQueryWrapper<CateringOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(CateringOrdersEntity::getPaidTime);
        wrapper.eq(CateringOrdersEntity::getMemberId, userId);
        return baseMapper.selectCount(wrapper) < 1;
    }

    @Override
    public Boolean shopFirstOrder(Long userId, Long shopId) {
        LambdaQueryWrapper<CateringOrdersEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CateringOrdersEntity::getStoreId, shopId)
                .eq(CateringOrdersEntity::getMemberId, userId);

        return baseMapper.selectCount(wrapper) <= 1;
    }

    @Override
    public MerchantCountVO merchantCount(Long shopId) {
        AppraiseStatisticsInfoVO appraiseStatisticsInfoVO = this.cateringOrdersAppraiseService.statisticsInfo(shopId);
        Integer orderTotal = this.cateringOrdersMapper.merchantOrderTotal(shopId);
        MerchantCountVO merchantCountVO = BaseUtil.objToObj(appraiseStatisticsInfoVO, MerchantCountVO.class);
        merchantCountVO.setOrdersNum(orderTotal);
        return merchantCountVO;
    }

    @Override
    public MerchantCountVO getMerchantCount(Long shopId) {
        MerchantCountVO merchantCount = orderUtils.getMerchantCount(shopId);
        if (merchantCount == null) {
            // 从数据库统计，并更新到缓存
            merchantCount = this.merchantCount(shopId);
            orderUtils.saveMerchantCount(shopId, merchantCount);
        }
        return merchantCount;
    }

    @Override
    public Map<Long, List<GoodsMonthSalesDTO>> goodsSalesByDays(Long shopId, Integer days) {
        List<GoodsMonthSalesDTO> goodsMonthSales = this.cateringOrdersMapper.goodsSalesByDays(shopId, days);
        Map<Long, List<GoodsMonthSalesDTO>> collect = null;
        if (BaseUtil.judgeList(goodsMonthSales)) {
            collect = goodsMonthSales.stream().collect(Collectors.groupingBy(GoodsMonthSalesDTO::getShopId));
        }
        return collect;
    }

    @Override
    public List<Long> shopIdList() {
        return this.cateringOrdersMapper.shopIdList();
    }

    @Override
    public Integer refundMessage(MerchantBaseDTO param) {
        return this.cateringOrdersMapper.refundMessage(param);
    }

    @Override
    public List<OrderListExcelExportDTO> listExcel(OrdersListAdminParamDTO paramDTO) {

        List<OrdersListAdminDTO> list = this.baseMapper.listForExcel(paramDTO);
        if (CollectionUtils.isEmpty(list)) {
            throw new CustomException("暂无数据");
        }
        DicUtils dictRedis = SpringContextUtils.getBean(DicUtils.class);
        Map<Integer, String> payWay = dictRedis.getNames("payWay");
        Map<Integer, String> orderStatus = dictRedis.getNames("orderStatus");
        AtomicInteger no = new AtomicInteger();
        return list.stream().map(i -> {
            OrderListExcelExportDTO dto = ConvertUtils.sourceToTarget(i, OrderListExcelExportDTO.class);
            List<OrdersListGoodsAdminDTO> goodsInfo = i.getGoodsInfo();
            StringBuilder builder = new StringBuilder();
            goodsInfo.forEach(e -> {
                builder.append(e.getGoodsName())
                        .append("（")
                        .append(e.getGoodsSkuDesc() == null ? "" : e.getGoodsSkuDesc())
                        .append("*")
                        .append(e.getQuantity())
                        .append("）")
                        .append(System.getProperty("line.separator"))
                        .append("+");
            });
            String result = builder.deleteCharAt(builder.length() - 1).toString();
            dto.setNo(no.incrementAndGet());
            dto.setGoodsInfo(result);
            dto.setBillingTime(DateTimeUtil.getDateTimeDisplayString(i.getBillingTime(), DateTimeUtil.PATTERN));
            dto.setPayWay(StringUtils.defaultString(payWay.get(i.getPayWay()), "-"));
            dto.setOrderStatus(orderStatus.get(i.getOrderStatus()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderGoodsListExcelExportDTO> listOrderGoodsExcel(OrdersListAdminParamDTO paramDTO) {
        // 订单商品信息
        List<OrdersGoodsExcelListAdminDTO> list = this.baseMapper.listOrderGoodsExcel(paramDTO);
        if (CollectionUtils.isEmpty(list)) {
            throw new CustomException("暂无数据");
        }
        // 获取订单状态字典
        DicUtils dictRedis = SpringContextUtils.getBean(DicUtils.class);
        Map<Integer, String> orderStatus = dictRedis.getNames("orderStatus");
        AtomicInteger no = new AtomicInteger();
        // 转换订单备餐excel模板对象
        return list.stream().map(i -> {
            OrderGoodsListExcelExportDTO dto = ConvertUtils.sourceToTarget(i, OrderGoodsListExcelExportDTO.class);
            dto.setNo(no.incrementAndGet());
            dto.setBillingTime(DateTimeUtil.getDateTimeDisplayString(i.getBillingTime(), DateTimeUtil.PATTERN));
            dto.setOrderStatus(orderStatus.get(i.getOrderStatus()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderBaseVo> orderListByStatus(Integer status, String estimateTime) {
        return cateringOrdersMapper.orderListByStatus(status, estimateTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderBaseVo> closeWaitingTokeOrder(String estimateTime) {
        List<OrderBaseVo> list = orderListByStatus(OrderStatusEnum.WAIT_TAKEN.getValue(), estimateTime);
        if (BaseUtil.judgeList(list)) {
            // 自提单
            List<Long> orderIdsWithSelfPickUp = new ArrayList<>(list.size());
            // 配送单
            List<Long> orderIdsWithTakeOut = new ArrayList<>(list.size());
            List<CateringOrdersOperationEntity> operationList = new ArrayList<>(list.size());
            list.forEach(orderBaseVo -> {
                if (DeliveryWayEnum.Delivery.getCode().compareTo(orderBaseVo.getDeliveryWay()) == 0) {
                    // 配送
                    orderIdsWithTakeOut.add(orderBaseVo.getOrderId());
                }
                if (DeliveryWayEnum.invite.getCode().compareTo(orderBaseVo.getDeliveryWay()) == 0) {
                    // 自取
                    orderIdsWithSelfPickUp.add(orderBaseVo.getOrderId());
                }
                CateringOrdersOperationEntity operationEntity = ConvertUtils.sourceToTarget(orderBaseVo, CateringOrdersOperationEntity.class);
                operationEntity.setOperationPhase(OrderOperationEnum.OFF.getCode());
                operationEntity.setOperationType(OrderOperationTypeEnum.SYSTEM.getValue());
                operationEntity.setOperationExplain(OrderOperationEnum.OFF.getDesc());
                operationEntity.setOperationTime(LocalDateTime.now());
                operationList.add(operationEntity);
            });
            if (CollectionUtils.isNotEmpty(orderIdsWithSelfPickUp)) {
                // 取消自提单
                String offReason = "订单超时未提取";
                batchCloseOrder(orderIdsWithSelfPickUp, offReason);
            }
            if (CollectionUtils.isNotEmpty(orderIdsWithTakeOut)) {
                // 取消配送单
                String offReason = "订单超时未配送";
                batchCloseOrder(orderIdsWithTakeOut, offReason);
            }

            // 订单操作日志
            cateringOrdersOperationService.saveBatch(operationList);
        }
        return list;
    }

    /**
     * 批量关闭订单
     *
     * @param orderIdsWithSelfPickUp
     * @param offReason
     */
    private void batchCloseOrder(List<Long> orderIdsWithSelfPickUp, String offReason) {
        LambdaUpdateWrapper<CateringOrdersEntity> wrapper = new LambdaUpdateWrapper();
        wrapper.in(CateringOrdersEntity::getId, orderIdsWithSelfPickUp);
        wrapper.set(CateringOrdersEntity::getStatus, OrderStatusEnum.OFF.getValue());
        wrapper.set(CateringOrdersEntity::getOffReason, offReason);
        this.update(wrapper);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return BaseUtil.objToObj(baseMapper.selectById(orderId), Order.class);
    }

    @Override
    public Boolean updateAfterSales(Long orderId) {
        CateringOrdersEntity newOrder = new CateringOrdersEntity();
        newOrder.setId(orderId);
        newOrder.setAfterSales(true);
        return updateById(newOrder);
    }

    @Override
    public Boolean updateOrderByPaySuccess(Order newOrder) {
        CateringOrdersEntity order = new CateringOrdersEntity();
        order.setId(newOrder.getId());
        order.setStatus(newOrder.getStatus());
        order.setPaidAmount(newOrder.getPaidAmount());
        order.setPaidTime(newOrder.getPaidTime());
        order.setPayWay(newOrder.getPayWay());
        return updateById(order);
    }

    @Override
    public Boolean updateStatusToCanceled(Long orderId, String offReason, OrderOffTypeEnum orderOffTypeEnum) {
        CateringOrdersEntity newOrder = new CateringOrdersEntity();
        newOrder.setId(orderId);
        newOrder.setOffReason(offReason);
        newOrder.setStatus(OrderStatusEnum.CANCELED.getValue());
        newOrder.setUpdateBy(orderOffTypeEnum.getCode().longValue());
        newOrder.setUpdateName(orderOffTypeEnum.getDesc());
        return updateById(newOrder);
    }

    @Override
    public Boolean updateStatusToCanceled(Long orderId, String offReason, Long userId, String userName) {
        CateringOrdersEntity newOrder = new CateringOrdersEntity();
        newOrder.setId(orderId);
        newOrder.setOffReason(offReason);
        newOrder.setStatus(OrderStatusEnum.CANCELED.getValue());
        newOrder.setUpdateBy(userId);
        newOrder.setUpdateName(userName);
        return updateById(newOrder);
    }

    @Override
    public void orderTimeOutAfterSales(String orderId) {
        CateringOrdersEntity order = this.getById(orderId);
        if (order != null) {
            if (OrderStatusEnum.DONE.getValue().equals(order.getStatus()) && !order.getAfterSales()) {
                LambdaQueryWrapper<CateringOrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(CateringOrdersEntity::getId, order.getId());
                queryWrapper.eq(CateringOrdersEntity::getStatus, OrderStatusEnum.DONE.getValue());
                queryWrapper.eq(CateringOrdersEntity::getAfterSales, false);
                CateringOrdersEntity newOrder = new CateringOrdersEntity();
                newOrder.setCanAfterSales(false);
                newOrder.setUpdateBy(OrderOffTypeEnum.AUTO_OFF.getCode().longValue());
                newOrder.setUpdateName(OrderOffTypeEnum.AUTO_OFF.getDesc());
                newOrder.setUpdateTime(LocalDateTime.now());
                boolean update = this.update(newOrder, queryWrapper);
                log.info("订单完成超时不能申请售后处理结果消息：【{}】", update);
            } else {
                log.info("{}:订单状态不是未完成状态【Status：{}】或者已申请售后【AfterSales：{}】，跳过超时处理",
                        order.getOrderNumber(), order.getStatus(), order.getAfterSales());
            }
        }
    }

    @Override
    public void merchantCount() {
        // 获取订单中的所有门店ID
        List<Long> shopIdList = this.shopIdList();
        shopIdList.forEach(shopId -> {
            // 从数据库统计，并更新到缓存
            MerchantCountVO merchantCount = this.merchantCount(shopId);
            orderUtils.saveMerchantCount(shopId, merchantCount);
            //        同步更新ES门店评分，销量
            Result<EsMerchantDTO> shopResult = esMerchantClient.getByMerchantId(shopId.toString());
            if (shopResult.success()) {
                EsMerchantDTO shop = shopResult.getData();
                if (shop != null) {
                    shop.setShopGrade(merchantCount.getShopGrade());
                    shop.setOrdersNum(merchantCount.getOrdersNum());
                    esMerchantClient.saveUpdate(shop);
                }
            }
        });
    }

    @Override
    public Order getByTradingFlow(String tradingFlow) {
        LambdaQueryWrapper<CateringOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersEntity::getTradingFlow, tradingFlow);
        return BaseUtil.objToObj(baseMapper.selectOne(wrapper), Order.class);
    }

    @Override
    public Boolean updateOrderTradingFlow(Long id, String tradingFlow) {
        CateringOrdersEntity order = new CateringOrdersEntity();
        order.setId(id);
        order.setTradingFlow(tradingFlow);
        return updateById(order);
    }

    /**
     * 描述:查询所有微信待付款订单
     *
     * @param
     * @return java.util.List<com.meiyuan.catering.order.entity.CateringOrdersEntity>
     * @author zengzhangni
     * @date 2020/4/13 13:44
     */
    @Override
    public List<Order> unPaidOrderList() {
        LambdaQueryWrapper<CateringOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersEntity::getStatus, OrderStatusEnum.UNPAID.getValue())
                .isNotNull(CateringOrdersEntity::getTradingFlow);
        List<CateringOrdersEntity> list = this.list(wrapper);
        List<Order> orders = BaseUtil.objToObj(list, Order.class);
        return orders;
    }

    /**
     * 功能描述: 根据订单编号集合获取订单集合
     *
     * @param orderNumbers
     * @return java.util.List<com.meiyuan.catering.core.dto.pay.Order>
     * @author xie-xi-jie
     * @date 2020/5/22 10:02
     * @since v 1.1.0
     */
    @Override
    public List<Order> getOrderListByOrderNumbers(List<String> orderNumbers) {
        LambdaQueryWrapper<CateringOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CateringOrdersEntity::getOrderNumber, orderNumbers);
        List<CateringOrdersEntity> list = this.list(wrapper);
        List<Order> orders = BaseUtil.objToObj(list, Order.class);
        return orders;
    }

    @Override
    public void refreshMerchantCountCache(Long merchantId) {
        if (merchantId == null) {
            return;
        }
        MerchantCountVO merchantCountVO = this.merchantCount(merchantId);
        orderUtils.saveMerchantCount(merchantId, merchantCountVO);
        MerchantCountVO merchantCount = orderUtils.getMerchantCount(merchantId);
        esMerchantClient.synGradeAndOrderNum(merchantId, merchantCount.getShopGrade(), merchantCount.getOrdersNum());
    }

    @Override
    public List<DeliveryGoodsVo> deliveryGoodsInfo(DeliveryGoodsParamDTO dto) {
        return cateringOrdersMapper.deliveryGoodsInfo(dto);
    }

    @Override
    public Map<String, Integer> redMessage(OrdersDistributionParamDTO param) {
        Map<String, Integer> map = new HashMap<>(16);
        /**待处理订单**/
        map.put(RedMessageEnum.NEW_ORDER.getStatus(), 0);
        List<OrdersDistributionDTO> orderList = this.orderDistribution(param);
        if (!CollectionUtils.isEmpty(orderList)) {
            map.put(RedMessageEnum.NEW_ORDER.getStatus(), orderList.size());
        }
        /**待处理退款单**/
        map.put(RedMessageEnum.NEW_REFUND_ORDER.getStatus(), 0);
        Integer refundOrderCount = this.refundMessage(param);
        if (refundOrderCount != null && refundOrderCount.intValue() > 0) {
            map.put(RedMessageEnum.NEW_REFUND_ORDER.getStatus(), refundOrderCount);
        }
        return map;
    }

    @Override
    public boolean isShopHavePendingOrder(Long shopId) {
        // 门店或者自提点是否有未处理完毕订单以及待退款订单
        return cateringOrdersMapper.isShopHavePendingOrder(shopId) > 0
                || cateringOrdersMapper.isSelfPickShopHavePendingOrder(shopId) > 0
                || cateringOrdersRefundMapper.isShopHavePendingOrder(shopId) > 0;
    }

    @Override
    public boolean isFirstOrder(Long userId, Long shopId) {
        int orderCount = cateringOrdersMapper.isFirstOrder(userId, shopId);
        if (orderCount == 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean isFirstOrderWithMerchant(Long userId, Long merchantId) {
        int orderCount = cateringOrdersMapper.isFirstOrderWithMerchant(userId, merchantId);
        if (orderCount == 1) {
            return Boolean.TRUE;
        }
        return false;
    }

    @Override
    public List<OrderTicketUsedRecordDTO> findTicketRecord(List<Long> orderId) {
        return this.baseMapper.findTicketRecord(orderId);
    }

    @Override
    public List<CateringOrdersEntity> queryListById(List<Long> idList) {
        QueryWrapper<CateringOrdersEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(CateringOrdersEntity::getCreateTime)
                .in(CateringOrdersEntity::getId, idList);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public PageData<OrderForMerchantPcDTO> listForMerchantPc(OrderForMerchantPcParamDto dto) {
        IPage<OrderForMerchantPcDTO> iPage = cateringOrdersMapper.listForMerchantPc(dto.getPage(), dto);
        PageData<OrderForMerchantPcDTO> page = new PageData<>(iPage.getRecords(), iPage.getTotal());
        if (page.getList().size() < page.getTotal()) {
            page.setLastPage(Boolean.TRUE);
        }
        List<OrderForMerchantPcDTO> list = page.getList();
        for (OrderForMerchantPcDTO item : list) {
            item.setDeliveryWayDesc(DeliveryWayEnum.parse(item.getDeliveryWay()));
            item.setOrderStatusDesc(OrderStatusEnum.parse(item.getOrderStatus()));
            item.setBillingTimeDesc(DateTimeUtil.getDateTimeDisplayString(item.getBillingTime(), "yyyy-MM-dd HH:mm:ss"));
            if (item.getRefundStatus() != null) {
                item.setRefundStatusDesc(RefundStatusEnum.parse(item.getRefundStatus()));
            }
        }
        return page;
    }

    @Override
    public List<OrderForMerchantPcExcelDTO> listForMerchantPcExcel(OrderForMerchantPcParamDto dto) {
        // 导出不需要分页
        List<OrderForMerchantPcDTO> list = cateringOrdersMapper.listForMerchantPcWithOutPage(dto);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        AtomicInteger no = new AtomicInteger();
        return list.stream().map(item -> {
            item.setDeliveryWayDesc(DeliveryWayEnum.parse(item.getDeliveryWay()));
            item.setOrderStatusDesc(OrderStatusEnum.parse(item.getOrderStatus()));
            item.setBillingTimeDesc(DateTimeUtil.getDateTimeDisplayString(item.getBillingTime(), "yyyy-MM-dd HH:mm:ss"));
            item.setPaidTimeDesc(Objects.nonNull(item.getPaidTime()) ? DateTimeUtil.getDateTimeDisplayString(item.getPaidTime(), "yyyy-MM-dd HH:mm:ss") : "");
            item.setPayWayDesc(Objects.nonNull(item.getPayWay()) ? PayWayEnum.parse(item.getPayWay()).getDesc() : "");
            item.setAfterSalesDesc(item.getAfterSales() ? "是" : "否");
            item.setRefundAuditStatusDesc(Objects.nonNull(item.getRefundAuditStatus()) ? AuditStatusEnum.parse(item.getRefundAuditStatus()).getDesc() : "");
            item.setDeliveryPrice(Objects.isNull(item.getDeliveryPrice()) || item.getDeliveryPrice().signum() == -1 ? new BigDecimal(0) : item.getDeliveryPrice());
            OrderForMerchantPcExcelDTO excelDTO = new OrderForMerchantPcExcelDTO();
            BeanUtils.copyProperties(item, excelDTO);
            excelDTO.setNo(no.incrementAndGet());
            return excelDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Long getFirstOrderId(Long userId) {
        return this.baseMapper.getFirstOrderId(userId);
    }

    @Override
    public List<OrderRefundDTO> listOrderRefund(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime) {

        return this.baseMapper.listOrderRefund(shopId, merchantId, startTime, endTime);
    }

    /**
     * 功能描述：根据一段时间查看营业历史趋势数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return OrderHistoryTrendDTO
     */
    @Override
    public List<OrderHistoryTrendDTO> historyTrendBusiness(Long shopId, Long merchantId, LocalDateTime startTime, LocalDateTime endTime) {
        List<OrderHistoryTrendDTO> orderHistoryTrendDTOS = this.baseMapper.historyTrendBusiness(shopId, merchantId, startTime, endTime);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        orderHistoryTrendDTOS = orderHistoryTrendDTOS.stream().filter(dto -> Objects.nonNull(dto.getBusinessDay())
                && LocalDateTime.parse(dto.getBusinessDay() + " 00:00:00", fmt).plusDays(1).isAfter(startTime)
                && LocalDateTime.parse(dto.getBusinessDay() + " 00:00:00", fmt).minusDays(1).isBefore(endTime)).collect(Collectors.toList());
        return orderHistoryTrendDTOS;
    }

    /**
     * 功能描述：商品销售报表
     *
     * @param param 查询参数
     * @return GoodsSaleDTO
     */
    @Override
    public IPage<GoodsSaleDTO> goodsSellListQuery(GoodsSalePageParamDTO param) {
        Page<GoodsSaleDTO> goodsSaleDTOPage = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<GoodsSaleDTO> page = cateringOrdersMapper.goodsSellListQuery(goodsSaleDTOPage, param);
        List<GoodsSaleDTO> records = page.getRecords();
        if (records.size() < param.getPageSize()) {
            param.setPageSize(param.getPageSize() - records.size());
            param.setExlGoodsSku(records.stream().map(GoodsSaleDTO::getGoodsSkuCode).collect(Collectors.toList()));
            Page<GoodsSaleDTO> goodsQueryPage = new Page<>(param.getPageNo(), param.getPageSize());
            IPage<GoodsSaleDTO> goodsSaleDTOIPage = cateringOrdersMapper.goodsSellListQueryGoods(goodsQueryPage, param);
            if (CollectionUtils.isEmpty(records)) {
                records = Lists.newArrayList();
            }
            records.addAll(goodsSaleDTOIPage.getRecords());
        }
        if (CollectionUtils.isNotEmpty(records)) {
            BigDecimal totalAmount = cateringOrdersMapper.goodsSellTotalAmount(param);
            if (totalAmount != null && BigDecimal.ZERO.compareTo(totalAmount) != 0) {
                BigDecimal decimal = new BigDecimal(100);
                records.forEach(dto -> dto.setSalePercentage(dto.getGoodsSaleAmout().divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(decimal)));
            }
        }
        page.setTotal(cateringOrdersMapper.countGoodsPages(param));
        page.setRecords(records);
        return page;
    }

    /**
     * 功能描述：商品销售数据导出
     *
     * @param param 查询条件
     * @return GoodsSaleExcelDTO
     */
    @Override
    public List<GoodsSaleExcelDTO> goodsExcelExport(GoodsSalePageParamDTO param) {
        List<GoodsSaleDTO> excelList = cateringOrdersMapper.goodsSellListExcel(param);
        if (CollectionUtils.isEmpty(excelList)) {
            return Collections.EMPTY_LIST;
        }
        BigDecimal totalAmount = cateringOrdersMapper.goodsSellTotalAmount(param);
        if (totalAmount != null && BigDecimal.ZERO.compareTo(totalAmount) != 0) {
            BigDecimal decimal = new BigDecimal(100);
            excelList.forEach(dto -> dto.setSalePercentage(dto.getGoodsSaleAmout().divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(decimal)));
        }
        AtomicInteger no = new AtomicInteger();
        return excelList.stream().map(item -> {
            GoodsSaleExcelDTO dto = new GoodsSaleExcelDTO();
            dto.setNo(no.incrementAndGet());
            dto.setGoodsName(item.getGoodsName());
            dto.setGoodsGroupName(item.getGoodsGroupName());
            dto.setGoodsSaleNum(item.getGoodsSaleNum());
            dto.setGoodsSaleAmout(item.getGoodsSaleAmout());
            dto.setTotalDiscountFee(item.getTotalDiscountFee());
            dto.setAfterDiscountFee(item.getAfterDiscountFee());
            dto.setSalePercentage(NumberFormat.getInstance().format(item.getSalePercentage() != null ? item.getSalePercentage() : 0) + "%");
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PrintOutPaperVO> getPrintInfo(PrintPaperDTO dto) {

        List<PrintOutPaperVO> list = new ArrayList<>();
        for (String id : dto.getOrderIds()) {
            PrintOutPaperVO printOutPaperVO = cateringOrdersMapper.getPrintInfo(Long.valueOf(id));
            if (printOutPaperVO != null) {
                List<PrintOutPaperGoodsVO> goodsList = cateringOrdersMapper.getGoodsList(Long.valueOf(id));
                printOutPaperVO.setOrdersGoods(goodsList);
                if (dto.getType() == 3) {
                    printOutPaperVO.setTypeStr("厨打单");
                } else if (printOutPaperVO.getDeliveryWay() == 1) {
                    printOutPaperVO.setTypeStr("配送单");
                } else if (printOutPaperVO.getDeliveryWay() == 2) {
                    printOutPaperVO.setTypeStr("自取单");
                }
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime start = LocalDateTime.from(dateTimeFormatter.parse(printOutPaperVO.getEstimateTime()));
                LocalDateTime end = LocalDateTime.from(dateTimeFormatter.parse(printOutPaperVO.getEstimateEndTime()));
                printOutPaperVO.setEstimateTimeStr(DateTimeFormatter.ofPattern("MM-dd HH:mm").format(start) + DateTimeFormatter.ofPattern("-HH:mm").format(end));
                printOutPaperVO.setPrintTime(dateTimeFormatter.format(LocalDateTime.now()));
                printOutPaperVO.setType(dto.getType());
            }
            list.add(printOutPaperVO);
        }

        return list;
    }

    @Override
    public PageData<ShopListBillVo> listBillShop(ShopBillDTO params) {

        Page<ShopBillDTO> page = new Page<>(params.getPageNo(), params.getPageSize());
        List<OrderItem> orderItems = new ArrayList<>();
        if (params.getSortParams() == null) {
            orderItems = OrderItem.descs("cm.create_time", "cs.create_time");
        } else {
            log.debug(com.meiyuan.catering.es.util.JsonUtils.obj2String(params.getSortParams()));
            if (params.getSortParams().containsKey(3)) {
                orderItems.add(new OrderItem().setColumn("CONVERT(cm.merchant_name USING gbk )").setAsc(params.getSortParams().get(3) == 1 ? false : true));
            }
            if (params.getSortParams().containsKey(2)) {
                orderItems.add(new OrderItem().setColumn("CONVERT(cs.shop_name USING gbk )").setAsc(params.getSortParams().get(2) == 1 ? false : true));
            }
            if (params.getSortParams().containsKey(4)) {
                orderItems.add(new OrderItem().setColumn("temp.orderCount").setAsc(params.getSortParams().get(4) == 1 ? false : true));
            }
            if (params.getSortParams().containsKey(5)) {
                orderItems.add(new OrderItem().setColumn("temp.orderAmount").setAsc(params.getSortParams().get(5) == 1 ? false : true));
            }
            if (params.getSortParams().containsKey(6)) {
                orderItems.add(new OrderItem().setColumn("temp.orderIncome").setAsc(params.getSortParams().get(6) == 1 ? false : true));
            }
            if (params.getSortParams().containsKey(7)) {
                orderItems.add(new OrderItem().setColumn("temp.platformDiscount").setAsc(params.getSortParams().get(7) == 1 ? false : true));
            }
            if (params.getSortParams().containsKey(8)) {
                orderItems.add(new OrderItem().setColumn("temp.merchantDiscount").setAsc(params.getSortParams().get(8) == 1 ? false : true));
            }
            if (params.getSortParams().containsKey(9)) {
                orderItems.add(new OrderItem().setColumn("temp.merchantIncome").setAsc(params.getSortParams().get(9) == 1 ? false : true));
            }
            if (params.getSortParams().containsKey(10)) {
                orderItems.add(new OrderItem().setColumn("temp.refundAmount").setAsc(params.getSortParams().get(10) == 1 ? false : true));
            }
            log.debug("orderItems:::{}" + orderItems);

        }
        page.setOrders(orderItems);
        IPage<ShopListBillVo> iPage = cateringOrdersMapper.listBillShop(page, params);

        PageData<ShopListBillVo> pages = new PageData<>(iPage.getRecords(), iPage.getTotal(), params.getPageNo() == iPage.getPages());
        return pages;
    }


    @Override
    public ShopBillTotalDTO totalOrderIncomeAndFound(ShopBillDTO paramDTO) {
        ShopBillTotalDTO billTotalDTO = cateringOrdersMapper.totalOrderIncomeAndFound(paramDTO);
//        ShopBillTotalDTO cancelOrder =  cateringOrdersMapper.totalCancelOrders(paramDTO);
//        ShopBillTotalDTO refundOrder =  cateringOrdersMapper.totalRefundOrders(paramDTO);
//        if(cancelOrder != null){
////            billTotalDTO.setTotalOrderAmount(billTotalDTO.getTotalOrderAmount().subtract(cancelOrder.getTotalOrderAmount()));
//            billTotalDTO.setTotalOrderIncome(billTotalDTO.getTotalOrderIncome().subtract(cancelOrder.getTotalOrderIncome()));
//            billTotalDTO.setTotalOrderCount(billTotalDTO.getTotalOrderCount().subtract(cancelOrder.getTotalOrderCount()));
//            billTotalDTO.setTotalPlatformDiscount(billTotalDTO.getTotalPlatformDiscount().subtract(cancelOrder.getTotalPlatformDiscount()));
//            billTotalDTO.setTotalMerchantIncome(billTotalDTO.getTotalMerchantIncome().subtract(cancelOrder.getTotalMerchantIncome()));
//        }
//        if(refundOrder != null){
////            billTotalDTO.setTotalOrderAmount(billTotalDTO.getTotalOrderAmount().subtract(refundOrder.getTotalOrderAmount()));
//            billTotalDTO.setTotalOrderIncome(billTotalDTO.getTotalOrderIncome().subtract(refundOrder.getTotalOrderIncome()));
//            billTotalDTO.setTotalOrderCount(billTotalDTO.getTotalOrderCount().subtract(refundOrder.getTotalOrderCount()));
//            billTotalDTO.setTotalPlatformDiscount(billTotalDTO.getTotalPlatformDiscount().subtract(refundOrder.getTotalPlatformDiscount()));
//            billTotalDTO.setTotalMerchantIncome(billTotalDTO.getTotalMerchantIncome().subtract(refundOrder.getTotalMerchantIncome()));
//        }
        return billTotalDTO;
    }


    @Override
    public Result<PageData<ShopBillDetailVo>> billShopDetailQuery(ShopBillDTO dto) {
        Page<ShopBillDetailDTO> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<ShopBillDetailVo> iPage = cateringOrdersMapper.billShopDetailQuery(page, dto);
        PageData<ShopBillDetailVo> pageData = new PageData<>(iPage.getRecords(), iPage.getTotal(), dto.getPageNo() == iPage.getPages());
        return Result.succ(pageData);
    }


    @Override
    public List<BillExcelExportDTO> billExcelExport(ShopBillDTO paramDTO) {
//        List<ShopBillDetailVo> list = cateringOrdersMapper.billShopDetailQuery(new Page(), paramDTO).getRecords();
        List<ShopBillDetailVo> list = cateringOrdersMapper.billShopDetailQueryExport(paramDTO);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            throw new CustomException("暂无数据");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeUtil.PATTERN);
        DicUtils dictRedis = SpringContextUtils.getBean(DicUtils.class);
        Map<Integer, String> payWay = dictRedis.getNames("payWay");
        Map<Integer, String> orderStatus = dictRedis.getNames("orderStatus");
        AtomicInteger no = new AtomicInteger();

        return list.stream().map(i -> {
            BillExcelExportDTO dto = ConvertUtils.sourceToTarget(i, BillExcelExportDTO.class);
//            dto.setNo(no.incrementAndGet());
            dto.setPaidTime(formatter.format(i.getPaidTime()));
            dto.setPayWay(org.apache.commons.lang3.StringUtils.defaultString(payWay.get(i.getPayWay()), "-"));
            dto.setOrderStatus(orderStatus.get(i.getOrderStatus()));
            dto.setMerchantDiscount(dto.getMerchantDiscount().setScale(2, BigDecimal.ROUND_HALF_UP));
            dto.setPlatformDiscount(dto.getPlatformDiscount().setScale(2, BigDecimal.ROUND_HALF_UP));
            dto.setMerchantIncome(dto.getMerchantIncome().setScale(2, BigDecimal.ROUND_HALF_UP));
            if (i.getDeliveryFee().compareTo(BigDecimal.ZERO) < 0 || i.getDeliveryWay() > 1) {
                dto.setDeliveryFeeStr("-");
            } else {
                dto.setDeliveryFeeStr(i.getDeliveryFee() + "");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ShopBillCityVo> getBillShopCityCode() {
        return cateringOrdersMapper.getBillShopCityCode();
    }

    @Override
    public ShopBillTotalDTO getBillGeneral(ShopBillDTO paramDTO) {
        paramDTO.setType(1);
        ShopBillTotalDTO totalGeneral = cateringOrdersMapper.getBillGeneral(paramDTO);
//        paramDTO.setType(2);
//        ShopBillTotalDTO refundGeneral = cateringOrdersMapper.getBillGeneral(paramDTO);
//        paramDTO.setType(3);
//        ShopBillTotalDTO cancelGeneral = cateringOrdersMapper.getBillGeneral(paramDTO);
//        if(refundGeneral != null){
//            totalGeneral.setTotalOrderIncome(totalGeneral.getTotalOrderIncome().subtract(refundGeneral.getTotalOrderIncome()));
//            totalGeneral.setTotalOrderCount(totalGeneral.getTotalOrderCount().subtract(refundGeneral.getTotalOrderCount()));
//            totalGeneral.setTotalPlatformDiscount(totalGeneral.getTotalPlatformDiscount().subtract(refundGeneral.getTotalPlatformDiscount()));
//            totalGeneral.setTotalMerchantDiscount(totalGeneral.getTotalMerchantDiscount().subtract(refundGeneral.getTotalMerchantDiscount()));
//            totalGeneral.setTotalShopSellGoods(totalGeneral.getTotalShopSellGoods().subtract(refundGeneral.getTotalShopSellGoods()));
//        }
//        if(cancelGeneral != null){
//            totalGeneral.setTotalOrderIncome(totalGeneral.getTotalOrderIncome().subtract(cancelGeneral.getTotalOrderIncome()));
//            totalGeneral.setTotalOrderCount(totalGeneral.getTotalOrderCount().subtract(cancelGeneral.getTotalOrderCount()));
//            totalGeneral.setTotalPlatformDiscount(totalGeneral.getTotalPlatformDiscount().subtract(cancelGeneral.getTotalPlatformDiscount()));
//            totalGeneral.setTotalMerchantDiscount(totalGeneral.getTotalMerchantDiscount().subtract(cancelGeneral.getTotalMerchantDiscount()));
//            totalGeneral.setTotalShopSellGoods(totalGeneral.getTotalShopSellGoods().subtract(cancelGeneral.getTotalShopSellGoods()));
//        }
        return totalGeneral;
    }

    @Override
    public List<BillTopTenDTO> getBillGoodsTopTen(ShopBillDTO paramDTO, int type) {
        if (type == 1) {
            return cateringOrdersMapper.getBillGoodsTopTen(paramDTO);
        } else {
            return cateringOrdersMapper.getBillShopTopTen(paramDTO);
        }
    }

    @Override
    public Result<List<BillMerchantInfoVo>> getMerchantInfo(BillMerchantInfoDTO billMerchantInfoDTO) {
        List<BillMerchantInfoVo> list = new ArrayList<>();
        if (billMerchantInfoDTO.getName() == null || "".equals(billMerchantInfoDTO.getName().trim())) {
            return Result.succ(list);
        }
        if (billMerchantInfoDTO.getType() == 1) {
            list = cateringOrdersMapper.getMerchant(billMerchantInfoDTO);
        } else {
            list = cateringOrdersMapper.getShop(billMerchantInfoDTO);
        }
        return Result.succ(list);
    }

    @Override
    public BizDataForMerchantDTO bizDataFroMerchant(Long shopId, Date orderDate, Date orderStartDate, Date orderEndDate) {
        // 营业额，单量
        BizDataForMerchantDTO ret = cateringOrdersMapper.bizData(shopId, orderDate, orderStartDate, orderEndDate);
        if (ret == null) {
            return null;
        }
        if (ret.getTotalPrice() == null) {
            ret.setTotalPrice(BigDecimal.ZERO);
        } else {
            ret.setTotalPrice(ret.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        if (ret.getActualTotalPrice() == null) {
            ret.setActualTotalPrice(BigDecimal.ZERO);
        } else {
            ret.setActualTotalPrice(ret.getActualTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        if (ret.getDiscountTotalPrice() == null) {
            ret.setDiscountTotalPrice(BigDecimal.ZERO);
        } else {
            ret.setDiscountTotalPrice(ret.getDiscountTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        if (ret.getRefundTotalPrice() == null) {
            ret.setRefundTotalPrice(BigDecimal.ZERO);
        } else {
            ret.setRefundTotalPrice(ret.getRefundTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (ret.getRefundOrderTotalAmount() == null) {
            ret.setRefundOrderTotalAmount(0);
        }


        return ret;
    }

    @Override
    public BizDataForMerchantDTO bizDataWithTime(Long merchantId, Long shopId, LocalDateTime orderStartDate, LocalDateTime orderEndDate) {
        return cateringOrdersMapper.bizDataWithTime(merchantId, shopId, orderStartDate, orderEndDate);
    }


    @Override
    public List<Long> listOrderIdByOrderNumber(List<String> orderNumberList) {
        if (CollectionUtils.isEmpty(orderNumberList)) {
            return null;
        }
        List<Long> list = cateringOrdersMapper.listOrderIdByOrderNumber(orderNumberList);
        return list;
    }

    @Override
    public List<Order> getExGrouponOrder() {
        return baseMapper.getExGrouponOrder();
    }

    @Override
    public List<OrderDeliveryStatusDto> listCancelOrderByDada(Long shopId) {
        List<OrderDeliveryStatusDto> orderList = cateringOrdersDeliveryCancelRecordMapper.listWaitToDeal(shopId);
        if (CollectionUtils.isEmpty(orderList)) {
            return null;
        }
        List<Long> orderIdList = orderList.stream().map(OrderDeliveryStatusDto::getOrderId).collect(Collectors.toList());
        List<CateringOrderDeliveryStatusHistoryEntity> statusList = cateringOrderDeliveryStatusHistoryMapper.listByOrderIds(orderIdList);
        Map<Long/* orderId */, List<CateringOrderDeliveryStatusHistoryEntity>> map = new HashMap<>(16);
        for (CateringOrderDeliveryStatusHistoryEntity entity : statusList) {
            if (!map.containsKey(entity.getOrderId())) {
                map.put(entity.getOrderId(), Lists.newArrayList());
            }
            map.get(entity.getOrderId()).add(entity);
        }

        for (OrderDeliveryStatusDto order : orderList) {
            CateringOrderDeliveryStatusHistoryEntity entity = map.get(order.getOrderId()).get(0);
            order.setDmId(entity.getDmId());
            order.setDmName(entity.getDmName());
            order.setDmMobile(entity.getDmMobile());
            order.setOrderStatus(entity.getOrderStatus());
            order.setOrderStatusDesc(OrderDeliveryStatusEnum.parseCode(entity.getOrderStatus()));
        }
        return orderList;
    }

    @Override
    public void cancelDadaOrder(Long orderId, Integer cancelReasonId, String cancelReason) {
        List<CateringOrderDeliveryStatusHistoryEntity> statusList = cateringOrderDeliveryStatusHistoryMapper.listByOrderIds(Lists.newArrayList(orderId));
        if (CollectionUtils.isEmpty(statusList)) {
            throw new CustomException("只有待接单状态，才可以取消订单");
        }
        CateringOrderDeliveryStatusHistoryEntity entity = null;
        if (CollectionUtils.isNotEmpty(statusList)) {
            // 最新的状态
            entity = statusList.get(0);
            if (!OrderDeliveryStatusEnum.wait_accept.getCode().equals(entity.getOrderStatus())) {
                // 只有待接单状态，商家才可以取消订单
                throw new CustomException("只有待接单状态，才可以取消订单");
            }
        }
        // 商家取消订单后违约金记录
        BigDecimal deductFee = dadaUtils.cancelOrder(orderId, cancelReasonId, cancelReason);
        updateDadaDeductFee(orderId, deductFee);
    }

    @Override
    public void updateDadaDeductFee(Long orderId, BigDecimal deductFee) {
        cateringOrdersDeliveryNoMapper.updateDeductFee(orderId, deductFee);
    }

    @Override
    public List<CateringOrdersDeliveryNoEntity> listDadaOrderByOrderId(Long orderId) {
        return cateringOrdersDeliveryNoMapper.listByOrderId(orderId);
    }

    @Override
    public void confirmDadaCancelOrderMessage(Long orderId, String dadaOrderId, Integer isConfirm) {
        dadaUtils.messageConfirm(orderId, dadaOrderId, isConfirm);
    }

    @Override
    public BigDecimal getOrderSubsidyAmount(Long id) {
        return baseMapper.getOrderSubsidyAmount(id);
    }

    @Override
    public CateringOrdersDeliveryNoEntity addOrderToDada(Long orderId, Integer isReOrder) {
        OrdersDetailMerchantDTO order = orderDetailQueryMerchant(orderId);

        if (order == null) {
            throw new CustomException("配送单下发到达达时，没有查询到订单信息");
        }


        if (OrderStatusEnum.DONE.getValue().equals(order.getOrderStatus())) {
            throw new CustomException("已完成订单不可再次下发到达达");
        }


        if (!DeliveryWayEnum.Delivery.getCode().equals(order.getDeliveryWay())) {
            // 自取订单不可下发到达达
            throw new CustomException("自取订单不可下发到达达");
        }

        if (order.getShopDeliveryFlag() == null || order.getShopDeliveryFlag().equals(Integer.valueOf(0))) {
            // 自配送订单不可下发到达达
            throw new CustomException("自配送订单不可下发到达达");
        }

        if (OrderStatusEnum.CANCELED.getValue().equals(order.getOrderStatus())) {
            // 已取消订单不可下发到达达
            throw new CustomException("已取消订单不可下发到达达");
        }


        String mapCoordinate = order.getMapCoordinate();
        // 收货地址经纬度，腾讯系，和高德系一致
        double[] latLngArrWithDeliveryAddress = latLng2arr(mapCoordinate);

        // 门店经纬度，百度系，需要转为高德系
        String shopMapCoordinate = order.getShopMapCoordinate();
        double[] latLngArrWithShop = bdLatLng2Gc02(shopMapCoordinate);
        log.debug("订单详情：" + JsonUtil.toJson(order));

        if (order.getEstimateTimeWithTime() != null) {
            // 非立即配送，也就是指定时间配送
            // 计算门店与配送地址之间的的距离
            double distance = EsUtil.convertLocation(latLngArrWithDeliveryAddress[1], latLngArrWithDeliveryAddress[0], latLngArrWithShop[1], latLngArrWithShop[0]);
            // 计算配送需要的时间
            double minute = calMinuteByDistance(distance);

            DateTimeDiffDto diff = DateTimeUtil.dateDiff(DateTimeUtil.now(), order.getEstimateTimeWithTime());
            double diffM = new Double(diff.getMinuteTotal()).doubleValue();
            log.debug("配送耗时计算结果：" + minute + "。指定送达时间距离当前时间分钟数：" + diffM);
            if (diffM > minute) {
                // 当前时间和指定送达时间大于配送耗时，不用立即发单
                log.debug("下发订单到达达为时尚早～");
                return null;
            }
        }

        // 配送单下发到达达
        DadaRetAfterAddOrder dadaRetAfterAddOrder = null;
        if (isReOrder.equals(Integer.valueOf(0))) {
            dadaRetAfterAddOrder = dadaUtils.addOrder(
                    order.getShopId(),
                    orderId,
                    order.getShopCity(),
                    order.getOrderAmount(),
                    order.getConsigneeName(),
                    order.getConsigneeAddress(),
                    BigDecimal.valueOf(latLngArrWithDeliveryAddress[1]),
                    BigDecimal.valueOf(latLngArrWithDeliveryAddress[0]),
                    order.getConsigneePhone());
        } else {

            List<OrderDeliveryStatusDto> statusList = order.getOrderDeliveryStatusList();
            if (CollectionUtils.isEmpty(statusList)) {
                throw new CustomException("只有被取消或投递异常且确认物品返回的订单才可以重发");
            }
            OrderDeliveryStatusDto statusDto = statusList.get(0);
            if (!OrderDeliveryStatusEnum.cancel.getCode().equals(statusDto.getOrderStatus())
                    && !OrderDeliveryStatusEnum.error_back_finish.getCode().equals(statusDto.getOrderStatus())) {
                throw new CustomException("只有被取消或投递异常且确认物品返回的订单才可以重发");
            }

            // 重发单
            dadaRetAfterAddOrder = dadaUtils.reAddOrder(
                    order.getShopId(),
                    orderId,
                    order.getShopCity(),
                    order.getOrderAmount(),
                    order.getConsigneeName(),
                    order.getConsigneeAddress(),
                    BigDecimal.valueOf(latLngArrWithDeliveryAddress[1]),
                    BigDecimal.valueOf(latLngArrWithDeliveryAddress[0]),
                    order.getConsigneePhone());
        }

        // 记录配送单下发到达达记录
        CateringOrdersDeliveryNoEntity item = new CateringOrdersDeliveryNoEntity();
        BeanUtils.copyProperties(dadaRetAfterAddOrder, item);
        item.setCreateTime(DateTimeUtil.now());
        item.setOrderId(orderId);
        item.setDeliveryNo(orderId.toString());
        item.setDeliveryType(DELIVERY_TYPE);
        item.setDeliveryTypeRemark(DELIVERY_TYPE_REMARK);
        cateringOrdersDeliveryNoMapper.insert(item);

        return item;
    }

    /**
     * 根据距离计算配送时间，3KM内60分钟，超过3KM，每超过1KM加15分钟，超出部分，不足1KM按1KM算
     *
     * @param distance 单位：米
     * @return
     */
    private double calMinuteByDistance(double distance) {
        double minute = 60;
        if (distance > 3000) {
            double distanceDiff = distance - 3000;
            minute = minute + new Double((distanceDiff / 1500) * 15) + new Double((distanceDiff % 1500) > 0 ? 15 : 0);
        }
        return minute;
    }

    @Override
    public void confirmDadaOrderGoods(Long orderId) {
        List<CateringOrderDeliveryStatusHistoryEntity> list = cateringOrderDeliveryStatusHistoryMapper.listByOrderIds(Lists.newArrayList(orderId));
        if (CollectionUtils.isNotEmpty(list)) {
            CateringOrderDeliveryStatusHistoryEntity item = list.get(0);
            if (!OrderDeliveryStatusEnum.error_back_in.getCode().equals(item.getOrderStatus())) {
                throw new CustomException("妥投异常之物品返回中的订单才可以做此操作");
            }
        }
        dadaUtils.confirmGoods(orderId);
    }

    /**
     * 字符串坐标转坐标数组
     *
     * @param mapCoordinate
     * @return
     */
    private double[] latLng2arr(String mapCoordinate) {
        String[] split = mapCoordinate.split(",");
        double[] d = new double[]{new Double(split[0]), new Double(split[1])};
        return d;
    }


    /**
     * 百度坐标转高德坐标
     *
     * @param mapCoordinate
     * @return
     */
    private double[] bdLatLng2Gc02(String mapCoordinate) {
        String[] split = mapCoordinate.split(",");
        return GpsCoordinateUtils.bd09_To_Gcj02(new Double(split[0]), new Double(split[1]));
    }


    /**
     * 批量发单到达达
     */
    @Override
    public void batchAddOrderToDada() {
        List<Long> orderList = cateringOrdersMapper.listWaitDeliveryTakeOutInnerHour();
        if (CollectionUtils.isEmpty(orderList)) {
            return;
        }
        for (Long orderId : orderList) {
            addOrderToDada(orderId, 0);
        }
    }


    @Override
    public void addDadaCancelOrderMessage(Long orderId, String cancelReason, Long dadaOrderId) {
        CateringOrdersDeliveryCancelRecordEntity entity = new CateringOrdersDeliveryCancelRecordEntity();
        entity.setOrderId(orderId);
        entity.setDadaOrderId(dadaOrderId);
        entity.setCancelReason(cancelReason);
        entity.setCreateTime(DateTimeUtil.now());
        // 默认待处理
        entity.setDealRet(0);
        cateringOrdersDeliveryCancelRecordMapper.insert(entity);
    }

    @Override
    public void dealDadaCancelOrder(Long orderId, Integer dealRet) {
        cateringOrdersDeliveryCancelRecordMapper.dealDadaCancelOrder(orderId, dealRet);
    }

    @Override
    public List<CateringOrdersDeliveryCancelRecordEntity> listWaitDealByOrderId(Long orderId) {
        return cateringOrdersDeliveryCancelRecordMapper.listWaitDealByOrderId(orderId);
    }

    public static void main(String[] args) {
        double x = 102.90042982206947;
        double y = 467.0;
        System.out.println(y > x);
    }


    @Override
    public void delOrdersLogicByMerchantId(Long merchantId) {
        cateringOrdersMapper.delOrdersLogicByMerchantId(merchantId);
    }

    @Override
    public void recoverOrdersLogicByMerchantId(Long merchantId) {
        cateringOrdersMapper.recoverOrdersLogicByMerchantId(merchantId);
    }
}
