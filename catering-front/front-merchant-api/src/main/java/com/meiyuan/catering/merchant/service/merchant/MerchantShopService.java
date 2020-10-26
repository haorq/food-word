package com.meiyuan.catering.merchant.service.merchant;

import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.gift.GiftDTO;
import com.meiyuan.catering.goods.feign.GoodsGiftClient;
import com.meiyuan.catering.marketing.feign.MarketingActivityClient;
import com.meiyuan.catering.merchant.dto.gift.ShopGiftGoodResponseDTO;
import com.meiyuan.catering.merchant.dto.merchant.ShopDTO;
import com.meiyuan.catering.merchant.dto.merchant.ShopHomeResponseDTO;
import com.meiyuan.catering.merchant.dto.pickup.*;
import com.meiyuan.catering.merchant.dto.shop.config.DeliveryConfigResponseDTO;
import com.meiyuan.catering.merchant.enums.ShopDeliveryTypeEnum;
import com.meiyuan.catering.merchant.feign.SelfMentionGiftServiceClient;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.feign.ShopDeliveryTimeRangeClient;
import com.meiyuan.catering.order.dto.MerchantBaseDTO;
import com.meiyuan.catering.order.dto.query.merchant.OrdersCountMerchantDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yaoozu
 * @description 门店服务
 * @date 2020/3/2010:50
 * @since v1.0.0
 */
@Service
@Slf4j
public class MerchantShopService {

    @Autowired
    private ShopDeliveryTimeRangeClient shopDeliveryTimeRangeClient;

    @Autowired
    private SelfMentionGiftServiceClient selfMentionGiftServiceClient;

    @Autowired
    private ShopClient shopClient;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private GoodsGiftClient goodsGiftClient;

    @Resource
    private MarketingActivityClient marketingActivityClient;

    /**
     * 方法描述 : 获取门店配送配置信息
     * 1、获取门店配送配置信息
     * 2、获取门店配送时间范围
     *
     * @param shopId
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 10:54
     * @return: DeliveryConfigResponseDTO
     * @Since version-1.1.0
     */
    public Result<DeliveryConfigResponseDTO> getDeliveryConfig(Long shopId) {
        return shopClient.getDeliveryConfig(shopId);
    }

    /**
     * 方法描述 : 门店配送配置信息修改
     * 1、修改门店配送配置信息
     * 2、修改门店配送时间范围
     *
     * @param shopId
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 11:09
     * @return: com.meiyuan.catering.core.util.Result
     * @Since version-1.0.0
     */
    public Result modifyDeliveryConfig(Long shopId, DeliveryConfigResponseDTO dto) {
        return shopClient.modifyDeliveryConfig(shopId, dto);
    }

    /**
     * 门店自提配置信息修改
     *
     * @param shopId
     * @param dto
     * @return
     */
    public Result modifyPickupConfig(Long shopId, PickupConfigResponseDTO dto) {
        return shopClient.modifyPickupConfig(shopId, dto);
    }

    /**
     * 获取门店首页信息
     *
     * @param shopId
     * @return
     */
    public Result<ShopHomeResponseDTO> getShopHomeInfo(Long shopId) {
        return shopClient.getShopHomeInfo(shopId);
    }

    /**
     * 方法描述 : 门店自提点 查询(自提点管理/自提点解绑)列表
     *
     * @param dto
     * @Author: MeiTao
     * @Date: 2020/6/4 0004 9:39
     * @return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.merchant.dto.pickup.PickupManagerResponseDTO>
     * @Since version-1.0.0 version-1.1.0
     */
    public Result<PickupManagerResponseDTO> listShopPickupAddress(PickupPointRequestDTO dto) {
        return shopClient.listShopPickupAddress(dto);
    }


    /**
     * 店铺自提点绑定解绑
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveOrDelShopPickup(PickupUpdateRequestDTO dto) {
        return shopClient.saveOrDelShopPickup(dto);
    }

    /**
     * 门店业务配置-查询门店业务支持类型
     *
     * @param shopId
     * @param type   修改：业务支持：1：仅配送，2：仅自提，3：全部，仅查询：type为4
     * @return
     */
    public Result<Integer> getShopSupportType(Long shopId, Integer type) {
        return shopClient.getShopSupportType(shopId, type);
    }

    /**
     * 售卖模式修改
     * 1、售卖模式功能去除
     * 2、返回值默认都是商品售卖模式
     *
     * @param shopId
     * @param type   售卖模式：1-菜单售卖模式 2-商品售卖模式
     * @return
     */
    public Result<Integer> modifyShopSellType(Long shopId, Integer type) {
        //修改商户基本信息、发送mq消息
        Result<ShopDTO> result = shopClient.modifyShopSellType(shopId, type);
        ShopDTO shopDTO = result.getData();

        return Result.succ(shopDTO.getSellType());
    }

    /**
     * 获取门店管理首页信息
     *
     * @param param
     * @return
     */
    public Result<OrdersCountMerchantDTO> getShopManagerHome(MerchantBaseDTO param) {
        return Result.succ(orderClient.ordersCountMerchant(param));
    }

    /**
     * 方法描述 : 获取门店自提配置信息
     * 1、自提时间范围
     * 2、自提赠品信息
     *
     * @param shopId
     * @Author: MeiTao
     * @Date: 2020/5/20 0020 10:53
     * @return: PickupConfigResponseDTO
     * @Since version-1.0.0
     */
    public Result<PickupConfigResponseDTO> getPickupConfig(Long shopId) {
        PickupConfigResponseDTO result = new PickupConfigResponseDTO();
        result.setId(shopId);
        //1、获取门店自提赠品信息
        //查询未删除的赠品id
        List<GiftDTO> goodsGiftEntities = goodsGiftClient.listGiftGood(null).getData();
        if (BaseUtil.judgeList(goodsGiftEntities)) {
            List<Long> giftIdList = goodsGiftEntities.stream().map(GiftDTO::getId).collect(Collectors.toList());

            //获取自提赠品信息
            List<SelfMentionGiftDTO> list = selfMentionGiftServiceClient.listShopGift(shopId, giftIdList).getData();

            //店铺赠品数据组装
            List<ShopGiftGoodResponseDTO> giftGoods = new ArrayList<>();
            if (BaseUtil.judgeList(list)) {
                Map<Long, GiftDTO> giftEntityMap = goodsGiftEntities.stream().collect(Collectors.toMap(GiftDTO::getId, giftEntity -> giftEntity));
                list.forEach(g -> {
                    ShopGiftGoodResponseDTO gift = new ShopGiftGoodResponseDTO();
                    BeanUtils.copyProperties(g, gift);
                    GiftDTO giftEntity = giftEntityMap.get(g.getGiftId());
                    BeanUtils.copyProperties(giftEntity, gift);
                    gift.setId(g.getId());
                    giftGoods.add(gift);
                });
            }

            //设置自提赠品信息
            result.setGiftGoods(giftGoods);
        }

        //组装时间范围-自提
        result.setPickupTimes(shopDeliveryTimeRangeClient.getTimeRangeList(shopId, ShopDeliveryTypeEnum.PICKUP.getStatus()).getData());
        return Result.succ(result);
    }


    /*public MerchantSaleSurveyVO getSaleSurvey(SaleOverviewParamDTO dto) {
        MerchantSaleSurveyVO merchantSaleSurveyVO = new MerchantSaleSurveyVO();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Integer queryType = dto.getType();
        //今日
        if (queryType.equals(1)) {
            startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }
        //昨日
        if (queryType.equals(2)) {
            startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1);
            endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(1);
        }
        //自定义
        if (queryType.equals(3)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String start = dtf.format(dto.getStartTime()) + " 00:00:00";
            String end = dtf.format(dto.getEndTime()) + " 23:59:59";
            startTime = LocalDateTime.parse(start,df);
            endTime = LocalDateTime.parse(end,df);
        }
        Integer[] status = {3,4,5,7,8};
        List<Integer> statusList = Arrays.asList(status);
        List<MerchantReportDto> merchantReportAllList = orderClient.orderListReportMerchant(dto.getShopId(), null, startTime, endTime);
        List<MerchantReportDto> orderSaleMerchantList = merchantReportAllList.stream()
                .filter(item -> statusList.contains(item.getStatus()) || (item.getStatus().equals(6) && Objects.isNull(item.getUpdateName()))).collect(Collectors.toList());
        //商户取消退款订单
        List<MerchantReportDto> merchantRefundOrders = merchantReportAllList.stream().filter(item -> item.getStatus().equals(6) && Objects.isNull(item.getUpdateName())).collect(Collectors.toList());
        List<Long> ids = orderSaleMerchantList.stream().map(MerchantReportDto::getId).collect(Collectors.toList());
        List<MarketingSubsidyVo> marketingSubsidyVos = new ArrayList<>();
        List<OrderRefundDTO> orderRefundDTOS = new ArrayList<>();
        BigDecimal marketingSubsidy = BigDecimal.ZERO;
        BigDecimal userRefundAmount = BigDecimal.ZERO;
        BigDecimal merchantRefundAmount = BigDecimal.ZERO;
        if(CollectionUtils.isNotEmpty(ids)){
            //平台补贴
            marketingSubsidyVos = marketingActivityClient.listMarketingTicketByOrderId(dto.getShopId(), null, ids);
            marketingSubsidy = marketingSubsidyVos.stream().map(MarketingSubsidyVo::getManageAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            //退款单
            orderRefundDTOS = orderClient.listOrderRefund(dto.getShopId(), null, ids);
            //用户退款金额
            userRefundAmount = orderRefundDTOS.stream().map(OrderRefundDTO::getRefundAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }
        if(CollectionUtils.isNotEmpty(merchantRefundOrders)){
            merchantRefundAmount = merchantRefundOrders.stream().map(MerchantReportDto::getPaidAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }
        //优惠金额
        BigDecimal totalDiscount = orderSaleMerchantList.stream().map(MerchantReportDto::getDiscountFee).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        //营业额
        BigDecimal saleAmount = orderSaleMerchantList.stream().map(MerchantReportDto::getPaidAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).add(totalDiscount);
        //订单实收
        BigDecimal orderActualAmount = saleAmount.add(marketingSubsidy).subtract(userRefundAmount).subtract(merchantRefundAmount);
        //订单数
        int orderNum = orderSaleMerchantList.size();
        //退款单数
        int refundOrderNum = orderRefundDTOS.size() + merchantRefundOrders.size();
        merchantSaleSurveyVO.setOrderActualAmount(orderActualAmount);
        merchantSaleSurveyVO.setTurnover(saleAmount);
        merchantSaleSurveyVO.setDiscountAmount(totalDiscount);
        merchantSaleSurveyVO.setRefundAmount(userRefundAmount.add(merchantRefundAmount));
        merchantSaleSurveyVO.setOrderNum(orderNum);
        merchantSaleSurveyVO.setRefundOrderNum(refundOrderNum);

        return merchantSaleSurveyVO;
    }*/
}
