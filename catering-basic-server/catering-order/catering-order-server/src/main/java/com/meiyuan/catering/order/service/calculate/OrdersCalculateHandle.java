package com.meiyuan.catering.order.service.calculate;

import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.NumberUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsSimpleInfoDTO;
import com.meiyuan.catering.marketing.dto.ticket.UserTicketDetailsDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialEntity;
import com.meiyuan.catering.marketing.enums.MarketingGoodsLimitEnum;
import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingTicketUsefulConditionEnum;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftVO;
import com.meiyuan.catering.merchant.enums.MerchantAttribute;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.dto.calculate.*;
import com.meiyuan.catering.order.enums.*;
import com.meiyuan.catering.order.service.CateringMerchantGiftService;
import com.meiyuan.catering.order.utils.OrderUtils;
import com.meiyuan.catering.user.dto.cart.CartGoodsQueryDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillUserDTO;
import com.meiyuan.catering.user.dto.cart.CartSimpleDTO;
import com.meiyuan.catering.user.enums.UserTypeEnum;
import com.meiyuan.catering.user.fegin.cart.CartClient;
import com.meiyuan.catering.user.utils.CartUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.meiyuan.catering.core.exception.ErrorCode.CART_CHECK_ERROR;
import static com.meiyuan.catering.core.util.DateTimeUtil.PATTERN;
import static com.meiyuan.catering.core.util.DateTimeUtil.PATTERN_YY_MM_DD;

/**
 * @Author XiJie-Xie
 * @email 1121075903@qq.com
 * @create 2020/3/27 10:53
 */
@Component
@Slf4j
public class OrdersCalculateHandle extends AbstractOrdersCalculate {
    @Resource
    private CartClient cartClient;
    @Resource
    private UserTicketClient userTicketClient;
    @Resource
    private OrderUtils orderUtils;
    @Resource
    private CateringMerchantGiftService cateringMerchantGiftService;
    @Resource
    private OrdersCalculateSupport ordersCalculateSupport;
    @Resource
    private ShopClient shopClient;
    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private MarketingSpecialClient marketingSpecialClient;

    /**
     * 功能描述: 获取商户店铺信息
     *
     * @param param 结算信息
     */
    @Override
    public void getShopInfo(OrderCalculateDTO param) {
        /** 门店（商户）状态校验，如营业状态，禁用状态 **/
        Result result = shopClient.isWxShow(param.getStoreId());
        if (!result.success()) {
            throw new CustomException(result.getCode(), result.getMsg());
        }

        ShopInfoDTO shop = this.merchantUtils.getShopIsNullThrowEx(param.getShopId());

        param.setStoreId(shop.getId());
        param.setStoreName(shop.getShopName());
        param.setStorePicture(shop.getDoorHeadPicture());
        param.setMerchantPhone(shop.getShopPhone());
        param.setMerchantId(shop.getMerchantId());

    }

    /**
     * 功能描述: 获取配送信息
     *
     * @param param 结算信息
     */
    @Override
    public void getDeliveryInfo(OrderCalculateDTO param) {
        // 1、 构建订单取餐信息
        OrdersCalculateDeliveryDTO delivery;
        // 2、判断是配送还是自取方式
        if (DeliveryWayEnum.Delivery.getCode().equals(param.getDeliveryWay())) {
            // 获取用户配送信息
            delivery = this.ordersCalculateSupport.getDeliveryInfo(param);
        } else {
            // 获取用户自取信息
            delivery = this.ordersCalculateSupport.getInviteInfo(param);
        }
        if (delivery != null) {
            delivery.setEstimateTime(param.getEstimateTime());
            delivery.setEstimateEndTime(param.getEstimateEndTime());
            if (Objects.nonNull(param.getImmediateDeliveryTime()) && DeliveryWayEnum.Delivery.getCode().equals(param.getDeliveryWay())) {
                StringBuffer sb = new StringBuffer(DateTimeUtil.getDateTimeDisplayString(LocalDateTime.now(), PATTERN_YY_MM_DD)).append(" ");
                sb.append(param.getImmediateDeliveryTime()).append(":").append("00");
                delivery.setImmediateDeliveryTime(DateTimeUtil.parseTime(sb.toString(), PATTERN));
            }
            param.setDelivery(delivery);
        }

        MerchantInfoDTO merchant = merchantUtils.getMerchant(param.getMerchantId());


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeAfter7 = now.plusDays(7);

        // 团购，7天后的时间以团购结束时间开始计算
        if (OrderTypeEnum.BULK.getStatus().compareTo(param.getOrderType()) == 0) {
            List<OrdersCalculateActivityDTO> activityList = param.getActivityList();
            if (CollectionUtils.isNotEmpty(activityList)) {
                // 同一时间只能参加一个团购，这里取集合第一条数据
                OrdersCalculateActivityDTO activityDTO = activityList.get(0);
                if (activityDTO != null) {
                    timeAfter7 = activityDTO.getActivityEndTime().plusDays(7);
                }
            }
        }

        if (MerchantAttribute.SELF_BUSINESS.getBusinessAttribute() == merchant.getMerchantAttribute().intValue()) {
            // 自营，预售，取餐时间晚于今天，且早于7天后
            if (param.getEstimateTime() != null) {
                if (param.getEstimateTime().toLocalDate().isBefore(now.toLocalDate())
                        || param.getEstimateTime().toLocalDate().isAfter(timeAfter7.toLocalDate())) {
                    throw new CustomException(ErrorCode.SELF_PICKUP_TIMEOUT_ERROR, "请更改配送（自取）时间");
                }
            }
        }


        if (!OrderTypeEnum.BULK.getStatus().equals(param.getOrderType())
                && MerchantAttribute.NOT_SELF_BUSINESS.getBusinessAttribute() == merchant.getMerchantAttribute().intValue()) {
            // 非自营，现售，取餐时间得是今天【团购不做该校验】
            if (param.getEstimateTime() != null) {
                if (!now.toLocalDate().equals(param.getEstimateTime().toLocalDate())) {
                    throw new CustomException(ErrorCode.SELF_PICKUP_TIMEOUT_ERROR, "非自营，取餐时间只能是今天");
                }
            }
        }
//        validateTakeoutOrSelfPickTime(param.getEstimateTime(), param.getEstimateEndTime(), param.getShopId(), param.getDeliveryWay());
    }


    /**
     * 功能描述: 获取购物车商品信息
     *
     * @param param        结算信息
     * @param cartTypeEnum 购物车类型
     */
    @Override
    public List<CartSimpleDTO> listCartGoods(OrderCalculateDTO param, CalculateTypeEnum cartTypeEnum) {
        // 1、获取购物车商品信息
        CartGoodsQueryDTO queryDTO = new CartGoodsQueryDTO();
        if (cartTypeEnum.equals(CalculateTypeEnum.SHARE_BILL)) {
            queryDTO.setMerchantId(param.getMerchantId());
            queryDTO.setShareBillNo(param.getShareBillNo());
            queryDTO.setShopId(param.getShopId());
            //todo 暂时写死 解决拼单结算报错：普通购物车
            queryDTO.setType(2);
        } else {
            BeanUtils.copyProperties(param, queryDTO);
            //todo 这句代码在127 行 暂时放这里：拼单购物车
            queryDTO.setType(1);
        }

        Result<List<CartSimpleDTO>> listResult = this.cartClient.listSimple(queryDTO);
        if (listResult.failure()) {
            throw new CustomException(CART_CHECK_ERROR,"请重新选择菜品加入购物车！");
        }
        // 购物车中的商品信息
        List<CartSimpleDTO> data = listResult.getData();

        if (data == null || data.size() == 0) {
            throw new CustomException(CART_CHECK_ERROR,"请重新选择菜品加入购物车！");
        }

        // 2、如果是拼单购物车，填充拼单活动信息
        if (CalculateTypeEnum.SHARE_BILL.getCode().equals(param.getCalculateType())) {
            // 构建订单活动信息
            List<OrdersCalculateActivityDTO> activityList = new ArrayList<>();
            // 订单活动信息
            OrdersCalculateActivityDTO activityDTO = new OrdersCalculateActivityDTO();
            activityDTO.setRelationDimension(OrderRelationEnum.ORDER.getCode());
            activityDTO.setActivityType(OrderActivityEnum.SHARE_BILL.getCode());
            activityDTO.setActivityNo(param.getShareBillNo());
            activityList.add(activityDTO);
            param.setActivityList(activityList);
        }
        return data;
    }


    /**
     * 功能描述: 验证优惠卷
     *
     * @param param 结算信息
     * @return: void
     */
    @Override
    public void couponsCheck(OrderCalculateDTO param) {

        List<Long> couponsIdList = Lists.newArrayList();
        if (param.getCouponsId() != null) {
            couponsIdList.add(param.getCouponsId());
        }
        if (param.getCouponsWithShopId() != null) {
            couponsIdList.add(param.getCouponsWithShopId());
        }
        if (CollectionUtils.isEmpty(couponsIdList)) {
            return;
        }

        // 1、根据用户优惠券ID获取用户选择的优惠券信息
        Result<List<UserTicketDetailsDTO>> userTicketList = this.userTicketClient.getUserTicketInfo(couponsIdList);
        if (userTicketList.failure()) {
            throw new CustomException("获取优惠卷信息失败");
        }
        List<UserTicketDetailsDTO> userTicketDetailsDTOList = userTicketList.getData();
        if (CollectionUtils.isEmpty(userTicketDetailsDTOList)) {
            throw new CustomException("获取优惠卷信息失败");
        }

        LocalDateTime now = LocalDateTime.now();
        for (UserTicketDetailsDTO dto : userTicketDetailsDTOList) {
            if (dto.getUsed()) {
                throw new CustomException("优惠卷已使用");
            }
            if (!(now.isAfter(dto.getUseBeginTime()) && now.isBefore(dto.getUseEndTime()))) {
                throw new CustomException("所选优惠券已失效，请重新选择~");
            }
        }


        // 2、构造订单优惠卷信息，用与后面的计算
        List<OrdersCalculateDiscountDTO> goodsDiscountList = this.ordersCalculateSupport.buildCalculateDiscount(userTicketDetailsDTOList, param.getCouponsAmount(), param.getCouponsAmountWithShop());
        param.setDiscountList(goodsDiscountList);
    }

    /**
     * 功能描述: 购物车商品验证
     *
     * @param param
     * @return: void
     */
    @Override
    public void cartGoodsCheck(List<CartSimpleDTO> cateringCart, OrderCalculateDTO param) {
        // 1、构造需要计算的商品集合
        List<OrdersCalculateGoodsDTO> goodsList = new ArrayList<>();
        // 3、如果是拼单商品，填充拼单人信息
        Map<Long, CartShareBillUserDTO> shareBillUserMap = null;
        if (CalculateTypeEnum.SHARE_BILL.getCode().equals(param.getCalculateType())) {
            // 获取拼单用户信息Map集合，key——》用户id，value——》用户信息
            shareBillUserMap = this.ordersCalculateSupport.getShareBillUserMap(param.getShareBillNo());
        }
        // goods：购物车商品信息；esGoods：es商品信息
        for (CartSimpleDTO goods : cateringCart) {
            // 4、获取ES商品信息，并于购物车商品进行对比
            EsGoodsDTO esGoods;
            // 5、构造需要计算的商品信息
            OrdersCalculateGoodsDTO goodsDTO = new OrdersCalculateGoodsDTO();
            goodsDTO.setGoodsId(goods.getId());
            goodsDTO.setCategoryId(goods.getCategoryId());
            // 秒杀场次 v1.3.0 lh
            goodsDTO.setSeckillEventId(goods.getSeckillEventId());
            // 每单限X份优惠
            goodsDTO.setDiscountLimit(goods.getDiscountLimit());

            // 6、如果是秒杀商品，不能进行优惠计算
            if (CartUtil.isSeckillGoods(goods.getGoodsType())) {
                // 获取秒杀商品信息
                MarketingGoodsSimpleInfoDTO simpleInfo = this.ordersCalculateSupport.getSimpleInfo(goods.getGoodsId(), MarketingOfTypeEnum.SECKILL, null);
                // 验证秒杀商品是否可以购买，并返回秒杀商品在es的信息
                esGoods = this.ordersCalculateSupport.activityGoodsCheck(goods.getShopId().toString(), simpleInfo, goods.getPrice(), MarketingOfTypeEnum.SECKILL);

                /** 如果用户类型是企业，秒杀活动商品原价为商品的企业价,个人则为个人价 */
                if (UserTypeEnum.COMPANY.getStatus().equals(param.getUserType())
                        && esGoods.getEnterprisePrice() != null
                        && esGoods.getEnterprisePrice().compareTo(BigDecimal.ZERO) > 0) {
                    goodsDTO.setStorePrice(esGoods.getEnterprisePrice());
                } else {
                    goodsDTO.setStorePrice(esGoods.getMarketPrice());
                }
                goodsDTO.setSalesPrice(simpleInfo.getActivityPrice());

                //如果是秒杀，这里的goodsId存mGoodsId
                goodsDTO.setGoodsId(simpleInfo.getMGoodsId());
                // 记录单个商品优惠后总金额
                goodsDTO.setGoodsTotalPrice(goods.getTotalPrice());
                // 构建秒杀活动信息，用于结算后的下单数据写入
                param.addActivityList(this.ordersCalculateSupport.newActivityBuild(simpleInfo));
            } else {

                if (CartUtil.isSpecialGoods(goods.getGoodsType())) {
                    // 特价商品构建特价活动信息
                    Long activityId = goods.getSeckillEventId();
                    // 构建特价活动信息
                    param.addActivityList(buildSpecialActivity(activityId));
//                    param.setActivityList(buildSpecialActivity(activityId, param.getActivityList()));
                }

                // 7、验证普通商品上下架、购买数量限制，并返回普通商品在es的商品信息
                esGoods = this.ordersCalculateSupport.ordinaryGoodsCheck(goods);
                // 如果用户类型是企业，销售价为商品的企业价,个人则为个人价
                if (UserTypeEnum.COMPANY.getStatus().equals(param.getUserType())
                        && esGoods.getEnterprisePrice() != null
                        && esGoods.getEnterprisePrice().compareTo(BigDecimal.ZERO) > 0) {
                    goodsDTO.setSalesPrice(esGoods.getEnterprisePrice());
                } else {
                    if (esGoods.getSalesPrice() != null && esGoods.getSalesPrice().compareTo(BigDecimal.ZERO) > 0) {
                        goodsDTO.setSalesPrice(esGoods.getSalesPrice());
                    } else {
                        goodsDTO.setSalesPrice(esGoods.getMarketPrice());
                    }
                }
                goodsDTO.setStorePrice(esGoods.getMarketPrice());
                goodsDTO.setGoodsId(Long.valueOf(esGoods.getGoodsId()));
                // 记录单个商品优惠后总金额
                goodsDTO.setGoodsTotalPrice(goods.getTotalPrice());

            }
            //设置餐盒费
            if (Objects.nonNull(esGoods.getPackPrice())) {
                goodsDTO.setPackPrice(BigDecimal.ZERO.compareTo(esGoods.getPackPrice()) >= 0 ? BigDecimal.ZERO : esGoods.getPackPrice());
            } else {
                goodsDTO.setPackPrice(BigDecimal.ZERO);
            }
            //设置商品规格
            goodsDTO.setGoodsSpecType(esGoods.getGoodsSpecType());
            // 8、填充计算的商品信息
            this.ordersCalculateSupport.goodsConver(esGoods, goodsDTO);
            // 9、如果是拼单商品，填充拼单人信息
            if (CartUtil.isShareBill(goods.getType())) {
                goodsDTO.setShareBillUserId(goods.getUserId());
                CartShareBillUserDTO cartShareBillUserDTO = shareBillUserMap.get(goods.getUserId());
                if (cartShareBillUserDTO == null) {
                    log.error("获取拼单人信息失败 shareBillUserID = 【{}】，shareBillNo = 【{}】", goods.getUserId(), param.getShareBillNo());
                    throw new CustomException("获取拼单人信息失败");
                }
                goodsDTO.setShareBillUserName(cartShareBillUserDTO.getNickname());
                goodsDTO.setShareBillUserAvatar(cartShareBillUserDTO.getAvatar());
            }
            goodsDTO.setQuantity(goods.getNumber());
            goodsDTO.setGoodsType(goods.getGoodsType());
            goodsList.add(goodsDTO);
        }
        param.setGoodsList(goodsList);
    }

    /**
     * 构建特价商品活动信息
     *
     * @param activityId
     * @param activityList
     * @return
     */
    private List<OrdersCalculateActivityDTO> buildSpecialActivity(Long activityId, List<OrdersCalculateActivityDTO> activityList) {
        CateringMarketingSpecialEntity ret = marketingSpecialClient.findById(activityId).getData();
        if (ret == null) {
            return activityList;
        }
        OrdersCalculateActivityDTO activityDTO = new OrdersCalculateActivityDTO();
        activityDTO.setActivityId(activityId);
        activityDTO.setRelationDimension(OrderRelationEnum.GOODS.getCode());
        activityDTO.setActivityType(OrderActivityEnum.SPECIAL.getCode());
        activityDTO.setActivityBeginTime(ret.getBeginTime());
        activityDTO.setActivityEndTime(ret.getEndTime());
        activityDTO.setActivityName(ret.getName());
        if (CollectionUtils.isEmpty(activityList)) {
            activityList = Lists.newArrayList();
        }
        activityList.add(activityDTO);
        return activityList;
    }

    private OrdersCalculateActivityDTO buildSpecialActivity(Long activityId) {
        CateringMarketingSpecialEntity ret = marketingSpecialClient.findById(activityId).getData();
        if (ret == null) {
            return null;
        }
        OrdersCalculateActivityDTO activityDTO = new OrdersCalculateActivityDTO();
        activityDTO.setActivityId(activityId);
        activityDTO.setRelationDimension(OrderRelationEnum.GOODS.getCode());
        activityDTO.setActivityType(OrderActivityEnum.SPECIAL.getCode());
        activityDTO.setActivityBeginTime(ret.getBeginTime());
        activityDTO.setActivityEndTime(ret.getEndTime());
        activityDTO.setActivityName(ret.getName());
        return activityDTO;
    }

    /**
     * 功能描述: 团购商品校验
     *
     * @param param 结算信息
     * @return: void
     */
    @Override
    public void bulkGoodsCheck(OrderCalculateDTO param) {
        // 1、验证团购活动是否可以购买，并返回团购在es的商品信息
        MarketingGoodsSimpleInfoDTO simpleInfo = this.ordersCalculateSupport.getSimpleInfo(
                param.getGoodsId(), MarketingOfTypeEnum.GROUPON, param.getPackPrice());
        EsGoodsDTO esGoods = this.ordersCalculateSupport.activityGoodsCheck(
                param.getShopId().toString(),
                simpleInfo, param.getGoodsSalesPrice(),
                MarketingOfTypeEnum.GROUPON);

        // 2、订单活动信息
        OrdersCalculateActivityDTO activityDTO = new OrdersCalculateActivityDTO();
        BeanUtils.copyProperties(simpleInfo, activityDTO);

        activityDTO.setRelationDimension(OrderRelationEnum.ORDER.getCode());
        activityDTO.setActivityType(OrderActivityEnum.BULK.getCode());

        List<OrdersCalculateActivityDTO> activityList = new ArrayList<>();
        activityList.add(activityDTO);
        param.setActivityList(activityList);

        // 3、构造需要计算的商品信息
        OrdersCalculateGoodsDTO goodsDTO = new OrdersCalculateGoodsDTO();
        BeanUtils.copyProperties(esGoods, goodsDTO);
        //设置餐盒费
        if (Objects.nonNull(esGoods.getPackPrice())) {
            goodsDTO.setPackPrice(BigDecimal.ZERO.compareTo(esGoods.getPackPrice()) >= 0 ? BigDecimal.ZERO : esGoods.getPackPrice());
        } else {
            goodsDTO.setPackPrice(BigDecimal.ZERO);
        }
        //设置商品规格
        goodsDTO.setGoodsSpecType(esGoods.getGoodsSpecType());
        /* 多图片用逗号分割 */
        String infoPicture = esGoods.getInfoPicture();
        if (StringUtils.isNotEmpty(infoPicture)) {
            goodsDTO.setGoodsPicture(infoPicture.split(",")[0]);
        }
        // 如果用户类型是企业，原价为商品的企业价,个人则为个人价
        if (UserTypeEnum.COMPANY.getStatus().equals(param.getUserType())
                && esGoods.getEnterprisePrice() != null
                && esGoods.getEnterprisePrice().compareTo(BigDecimal.ZERO) > 0) {
            goodsDTO.setStorePrice(esGoods.getEnterprisePrice());
        } else {
            goodsDTO.setStorePrice(esGoods.getMarketPrice());
        }
        goodsDTO.setGoodsSkuCode(simpleInfo.getSku());

        // 商品规格
        goodsDTO.setGoodsSpecificationDesc(esGoods.getSkuValue());


        //如果是团购，这里的goodsId存mGoodsId
        goodsDTO.setGoodsId(simpleInfo.getMGoodsId());
        goodsDTO.setSalesPrice(simpleInfo.getActivityPrice());
        goodsDTO.setGifts(false);
        goodsDTO.setQuantity(param.getGoodsQuantity());
        goodsDTO.setGoodsType(OrderGoodsTypeEnum.BULK.getValue());

        // 4、构造需要计算的商品集合
        List<OrdersCalculateGoodsDTO> goodsList = new ArrayList<>();
        goodsList.add(goodsDTO);
        param.setGoodsList(goodsList);
    }


    /**
     * 功能描述: 优惠券针对商品使用，判断哪些商品可以参与优惠
     *
     * @param param
     * @return: void
     */
    @Override
    public void discountInfo(OrderCalculateDTO param) {
        // 1、获取结算中的商品信息
        List<OrdersCalculateGoodsDTO> goodsList = param.getGoodsList();
        // 2、获取订单优惠券信息
        List<OrdersCalculateDiscountDTO> discountList = param.getDiscountList();
        // 【是否使用优惠卷】
        if (BaseUtil.judgeList(discountList)) {
            List<OrdersCalculateDiscountDTO> collect = discountList.stream().filter(obj -> OrderDiscountTypeEnum.TICKET.getCode().equals(obj.getDiscountType())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect)) {
                return;
            }
            for (OrdersCalculateDiscountDTO ordersCalculateDiscountDTO : collect) {
//                ordersCalculateDiscountDTO.setDiscountAmountActual(ordersCalculateDiscountDTO.getDiscountAmount());
                // v1.3.0 同时支持平台优惠券和门店优惠券
                // 如果需要使用优惠卷，判断商品是否可以使用优惠卷
                if (ordersCalculateDiscountDTO == null) {
                    continue;
                }
                if (MarketingTicketUsefulConditionEnum.ORDER_TICKET.getStatus().equals(ordersCalculateDiscountDTO.getUsefulCondition())) {
                    // 3、处理【订单】优惠卷
                    this.ordersCalculateSupport.orderDiscount(ordersCalculateDiscountDTO, goodsList);
                } else {
                    // 商品优惠
                    if (MarketingGoodsLimitEnum.ALL.getStatus().equals(ordersCalculateDiscountDTO.getGoodsLimit())) {
                        // 4、处理【全部商品】优惠卷
                        this.ordersCalculateSupport.orderDiscount(ordersCalculateDiscountDTO, goodsList);
                    } else if (MarketingGoodsLimitEnum.GOODS.getStatus().equals(ordersCalculateDiscountDTO.getGoodsLimit())) {
                        // 5、【指定商品】集合优惠卷
                        this.ordersCalculateSupport.partGoods(ordersCalculateDiscountDTO, goodsList);
                    } else {
                        // 6、【指定商品分类】集合优惠卷
                        this.ordersCalculateSupport.goodsCategory(ordersCalculateDiscountDTO, goodsList);
                    }
                }
            }
        }
    }

    /**
     * 功能描述: 计算金额
     *
     * @param param 结算信息
     */
    @Override
    public void calculateAmount(OrderCalculateDTO param) {

        // 1、获取结算中的商品信息
        List<OrdersCalculateGoodsDTO> goodsList = param.getGoodsList();
        if (goodsList == null || goodsList.size() == 0) {
            throw new CustomException("获取计算商品信息失败");
        }

        // 2、获取订单优惠券信息
        List<OrdersCalculateDiscountDTO> discountList = param.getDiscountList();

        // 订单优惠金额（初始值记录了运费满减金额）
        BigDecimal discountFee = param.getDiscountFee();
        if (discountFee == null) {
            discountFee = BigDecimal.ZERO;
        }
        // 订单中所有的商品优惠前金额 = 商品原价合计金额（如果原价为空或0，取商品销售价）
        BigDecimal goodsDiscountBeforeTotalAmount = BigDecimal.ZERO;
        // 订单中所有的商品优惠后金额 = 商品销售合计金额
        BigDecimal goodsDiscountLaterTotalAmount = BigDecimal.ZERO;
        // 订单商品优惠信息
        List<OrdersCalculateGoodsDiscountDTO> goodsDiscountList = new ArrayList<>();
        //特价商品、活动商品不可折扣的金额
        BigDecimal notOrdinaryGoodsDiscountFee = BigDecimal.ZERO;

        // 3、计算订单商品相关金额
        for (int i = 0; i < goodsList.size(); i++) {
            OrdersCalculateGoodsDTO goodsDTO = goodsList.get(i);
                // 商品优惠后金额 = 商品数量 * 销售价
                BigDecimal goodsDiscountLaterFee = NumberUtils.setScale(new BigDecimal(goodsDTO.getQuantity()).multiply(goodsDTO.getSalesPrice()));
                if (goodsDTO.getGoodsTotalPrice() != null) {
                    //每单限指定份数优惠，商品总金额取购物车金额
                    goodsDiscountLaterFee = goodsDTO.getGoodsTotalPrice();
                }
                if(!OrderGoodsTypeEnum.ORDINARY.getValue().equals(goodsDTO.getGoodsType())){
                    notOrdinaryGoodsDiscountFee = notOrdinaryGoodsDiscountFee.add(goodsDiscountLaterFee);
                }
                // 商品优惠前金额 = 商品数量 * 原价（如果原价为空或0，取商品销售价）
                BigDecimal goodsDiscountBeforeFee;
                if (goodsDTO.getStorePrice() != null && BigDecimal.ZERO.compareTo(goodsDTO.getStorePrice()) != 0) {
                    goodsDiscountBeforeFee = NumberUtils.setScale(new BigDecimal(goodsDTO.getQuantity()).multiply(goodsDTO.getStorePrice()));
                } else {
                    goodsDiscountBeforeFee = goodsDiscountLaterFee;
                }
                goodsDiscountLaterTotalAmount = goodsDiscountLaterTotalAmount.add(goodsDiscountLaterFee);
                goodsDiscountBeforeTotalAmount = goodsDiscountBeforeTotalAmount.add(goodsDiscountBeforeFee);
                goodsDTO.setDiscountBeforeFee(goodsDiscountBeforeFee);
                goodsDTO.setDiscountLaterFee(goodsDiscountLaterFee);
                // 优惠卷信息不空，用户选择了优惠卷

                if (CollectionUtils.isNotEmpty(discountList)) {
                    for (OrdersCalculateDiscountDTO discountDTO : discountList) {
                        // 构建商品优惠金额（这里只计算了商品（原价-现价）的一个优惠，并未将优惠金额均摊到商品上）
                        OrdersCalculateGoodsDiscountDTO goodsDiscountDTO = this.ordersCalculateSupport.goodsDiscountBuild(
                                goodsDiscountBeforeFee,
                                goodsDiscountLaterFee,
                                goodsDTO,
                                discountDTO);
                        goodsDiscountList.add(goodsDiscountDTO);
                    }
                }
        }

        // 4、优惠卷信息不空，用户选择了优惠卷

        if (CollectionUtils.isNotEmpty(discountList)) {
            for (OrdersCalculateDiscountDTO dto : discountList) {
                if (dto.getDiscountAmountActual() == null) {
                    dto.setDiscountAmountActual(dto.getDiscountAmount());
                }
            }
            // 可优惠金额（优惠券金额之和，有可能大于订单金额）
            discountFee = discountList
                    .stream()
                    .map(OrdersCalculateDiscountDTO::getDiscountAmountActual)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            goodsDiscountLaterTotalAmount = goodsDiscountLaterTotalAmount.subtract(notOrdinaryGoodsDiscountFee);
            if (discountFee.compareTo(goodsDiscountLaterTotalAmount.subtract(notOrdinaryGoodsDiscountFee)) > 0) {
                discountFee = goodsDiscountLaterTotalAmount.subtract(notOrdinaryGoodsDiscountFee);
            }
            param.setNotOrdinaryGoodsDiscountFee(notOrdinaryGoodsDiscountFee);
            for (OrdersCalculateDiscountDTO dto : discountList) {
                for (OrdersCalculateGoodsDiscountDTO goodsDiscountDTO : goodsDiscountList) {
                    if (goodsDiscountDTO.getOrderDiscountsId().compareTo(dto.getDiscountId()) == 0) {
                        if (CollectionUtils.isEmpty(dto.getGoodsDiscountList())) {
                            dto.setGoodsDiscountList(Lists.newArrayList());
                        }
                        dto.getGoodsDiscountList().add(goodsDiscountDTO);
                    }
                }
            }
        }
        countOrderAmount(param, goodsList, discountFee, goodsDiscountBeforeTotalAmount, goodsDiscountLaterTotalAmount,notOrdinaryGoodsDiscountFee);

    }

    private void countOrderAmount(OrderCalculateDTO param, List<OrdersCalculateGoodsDTO> goodsList, BigDecimal discountFee,
                                  BigDecimal goodsDiscountBeforeTotalAmount, BigDecimal goodsDiscountLaterTotalAmount,BigDecimal notOrdinaryGoodsDiscountFee) {
        // 5、计算订单相关金额
        // 订单总的餐盒费
        BigDecimal totalPackPrices = goodsList.stream().reduce(BigDecimal.ZERO, (x, y) -> x.add(y.getPackPrice().multiply(BigDecimal.valueOf(y.getQuantity()))), BigDecimal::add);
        // 订单优惠前金额 = 订单中所有的商品优惠前金额（如果有配送费 则需要加上 配送费 ）+ 餐盒费
        BigDecimal discountBeforeFee = (param.getDeliveryPriceOriginal() != null ? goodsDiscountBeforeTotalAmount
                .add(param.getDeliveryPriceOriginal()) : goodsDiscountBeforeTotalAmount).add(totalPackPrices != null ? totalPackPrices : BigDecimal.ZERO);
        // 订单优惠总金额 = 商品的优惠金额 + 优惠卷的优惠金额
        discountFee = goodsDiscountBeforeTotalAmount.subtract(goodsDiscountLaterTotalAmount).add(discountFee);
        // 如果省去了配送费，配送费需要叠加在优惠金额里面
        // param.getDeliveryPriceOriginal()商家配置的配送费
        // param.getDeliveryPrice()实际配送费，如果运费满减（运费满减按优惠后金额算），上一步已经计算为0
        if (param.getDeliveryPriceOriginal() != null
                && param.getDeliveryPrice() != null
                && param.getDeliveryPriceOriginal().compareTo(param.getDeliveryPrice()) > 0) {
            discountFee = discountFee.add(param.getDeliveryPriceOriginal());
        }
        // 订单优惠后金额= 订单优惠前金额-订单优惠总金额
        BigDecimal discountLaterFee = discountBeforeFee.subtract(discountFee);
        // 如果折后的金额是零元，折后金额设成0.01，防止零元订单，0元订单不走微信支付，不需要特殊处理v1.5.0
//        if (discountLaterFee.compareTo(BigDecimal.ZERO) <= 0) {
//            discountLaterFee = new BigDecimal("0.01");
//        }

        // 6、填充订单金额信息
        // 商品总件数
        Integer totalQuantity = goodsList.stream().map(OrdersCalculateGoodsDTO::getQuantity).reduce((x, y) -> x + y).get();
        param.setGoodsAmount(goodsDiscountLaterTotalAmount);
        param.setOrderAmount(discountLaterFee);
        param.setDiscountBeforeFee(discountBeforeFee);
        param.setDiscountLaterFee(discountLaterFee);
        param.setDiscountFee(discountFee);
        param.setPackPrice(totalPackPrices);
        param.setTotalQuantity(totalQuantity);
    }

    /**
     * 功能描述: 获取赠品信息
     *
     * @param param
     * @return: void
     */
    @Override
    public void listShopGift(OrderCalculateDTO param) {
        List<ShopGiftVO> shopGiftVoS = this.cateringMerchantGiftService.listShopGift(param.getStoreId());
        if (shopGiftVoS != null && shopGiftVoS.size() > 0) {
            List<OrdersGiftGoodsWxDTO> giftGoodsList = new ArrayList<>();
            shopGiftVoS.forEach(e -> {
                OrdersGiftGoodsWxDTO ordersGiftGoodsWxDTO = new OrdersGiftGoodsWxDTO();
                ordersGiftGoodsWxDTO.setGiftsActivityId(e.getPickupId());
                ordersGiftGoodsWxDTO.setOrderGoodsId(e.getGiftId());
                ordersGiftGoodsWxDTO.setGoodsName(e.getGiftName());
                ordersGiftGoodsWxDTO.setGoodsPicture(e.getGiftPicture());
                ordersGiftGoodsWxDTO.setQuantity(e.getNumber());
                ordersGiftGoodsWxDTO.setGifts(true);
                giftGoodsList.add(ordersGiftGoodsWxDTO);
            });
            param.setGiftGoodsList(giftGoodsList);
        }
    }


}
