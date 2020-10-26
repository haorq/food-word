package com.meiyuan.catering.merchant.pc.service.marketing;

import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.PageUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.enums.marketing.MarketingUsingObjectEnum;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.marketing.dto.MarketingGoodsTransferDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillGoodsDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillAddOrEditDTO;
import com.meiyuan.catering.marketing.dto.seckill.MarketingSeckillGoodsPageDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventEntity;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.*;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;
import com.meiyuan.catering.marketing.vo.seckill.*;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.feign.MerchantClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.feign.OrderActivityClient;
import com.meiyuan.catering.order.vo.marketing.MarketingOrderGoodsCountVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/08/06 09:08
 * @description 店铺活动--营销秒杀活动V1.3.0服务层
 **/

@Slf4j
@Service
public class MerchantPcMarketingSeckillService {

    @Autowired
    private MarketingSeckillClient seckillClient;
    @Autowired
    private MerchantPcMarketingService marketingService;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private ShopGoodsSkuClient shopGoodsSkuClient;
    @Autowired
    private MarketingSeckillEventRelationClient seckillEventRelationClient;
    @Autowired
    private MarketingGoodsClient marketingGoodsClient;
    @Autowired
    private OrderActivityClient orderActivityClient;
    @Autowired
    private MarketingRepertoryClient repertoryClient;
    @Autowired
    private MarketingPullNewClient pullNewClient;
    @Autowired
    private MarketingSeckillEventClient seckillEventClient;

    public Result<String> createOrEdit(MerchantAccountDTO token, MarketingSeckillAddOrEditDTO dto) {
        if (CollectionUtils.isEmpty(dto.getGoodsList())) {
            return Result.fail("未选择商品");
        }

        // 补全参数
        setAddOrEditDto(token, dto);

        // 校验数据

        // 校验商品是否在销售菜单里面
        Map<String, Long> skuMap = dto.getGoodsList().stream().collect(Collectors.toMap(MarketingSeckillGoodsDTO::getSkuCode,
                MarketingSeckillGoodsDTO::getGoodsId));
        shopGoodsSkuClient.verifyMarketingGoods(dto.getMerchantId(), dto.getShopId(), skuMap);

        // 校验活动信息
        seckillClient.verifySeckill(dto);

        // 处理商品数据
        List<MarketingGoodsTransferDTO> finalGoodsList = getFinalGoodsList(dto);
        if(!BaseUtil.judgeList(finalGoodsList)) {
            Result.fail("未查询到相关的商品信息");
        }

        Result<Boolean> res = seckillClient.createOrEdit(dto, finalGoodsList);
        if (null == dto.getId()) {
            return res.getData() ? Result.succ("秒杀活动创建成功") : Result.fail("秒杀活动创建失败");
        }
        return res.getData() ? Result.succ("秒杀活动编辑成功") : Result.fail("秒杀活动编辑失败");
    }

    private void setAddOrEditDto(MerchantAccountDTO token, MarketingSeckillAddOrEditDTO dto) {
        dto.setAccountId(token.getId());
        // 设置当前登录的店铺ID、店铺名称、商户ID、商户名称
        Long shopId = token.getAccountTypeId();
        dto.setShopId(shopId);
        ShopInfoDTO shopInfo = merchantUtils.getShop(shopId);
        dto.setShopName(shopInfo.getShopName());
        dto.setShopState(shopInfo.getShopStatus());
        dto.setShopServiceType(shopInfo.getShopServiceType());
        Long merchantId = shopInfo.getMerchantId();
        MerchantInfoDTO merchant = merchantUtils.getMerchant(merchantId);
        dto.setMerchantId(merchantId);
        dto.setMerchantName(merchant.getMerchantName());
        dto.setMerchantState(merchant.getMerchantStatus());
    }

    private List<MarketingGoodsTransferDTO> getFinalGoodsList(MarketingSeckillAddOrEditDTO dto) {
        List<MarketingSeckillGoodsDTO> goodsItems = dto.getGoodsList();
        Map<String, MarketingSeckillGoodsDTO> dtoMap = goodsItems.stream().collect(Collectors.toMap(MarketingSeckillGoodsDTO::getSkuCode, Function.identity()));
        ArrayList<String> skuList = Lists.newArrayList(dtoMap.keySet());
        Result<List<GoodsBySkuDTO>> result = marketingService.listGoodsBySkuCodeList(skuList, dto.getShopId());
        if (result.failure()) {
            return Collections.emptyList();
        }
        List<GoodsBySkuDTO> goodsList = result.getData();
        if (CollectionUtils.isEmpty(goodsList)) {
            return Collections.emptyList();
        }
        List<MarketingGoodsTransferDTO> collect = goodsList.stream()
                .map(e -> goodsConvert(e, dtoMap.get(e.getSkuCode())))
                .sorted((o1, o2) -> {
                    if (o1.getCreateTime().isBefore(o2.getCreateTime())) {
                        return -1;
                    } else if (o1.getCreateTime().isEqual(o2.getCreateTime())) {
                        return 0;
                    }
                    return 1;
                }).collect(Collectors.toList());
        // 商品排序序号
        int sort = 1;
        for (MarketingGoodsTransferDTO goodsDTO : collect) {
            goodsDTO.setGoodsSort(sort);
            ++sort;
        }
        return collect;
    }

    /**
     * 商品数据DTO转换
     *
     * @param dto 商品信息
     * @param seckillGoodsDTO 秒杀商品信息
     *
     */
    private MarketingGoodsTransferDTO goodsConvert(GoodsBySkuDTO dto, MarketingSeckillGoodsDTO seckillGoodsDTO) {
        MarketingGoodsTransferDTO tarDto = new MarketingGoodsTransferDTO();
        tarDto.setCode(dto.getSpuCode());
        tarDto.setGoodsId(dto.getGoodsId());
        tarDto.setGoodsName(dto.getGoodsName());
        tarDto.setGoodsPicture(dto.getInfoPicture());
        tarDto.setSku(dto.getSkuCode());
        tarDto.setSkuValue(dto.getPropertyValue());
        // 商品价格 -原价
        tarDto.setStorePrice(dto.getMarketPrice());
        tarDto.setActivityPrice(seckillGoodsDTO.getActivityPrice());
        tarDto.setLimitQuantity(seckillGoodsDTO.getLimitQuantity());
        tarDto.setQuantity(seckillGoodsDTO.getQuantity());
        tarDto.setMinQuantity(seckillGoodsDTO.getMinQuantity());
        tarDto.setLabelList(dto.getLabelNames());
        tarDto.setGoodsDesc(dto.getGoodsDescribeText());
        tarDto.setGoodsUpDown(dto.getGoodsUpDown());
        tarDto.setGoodsSort(seckillGoodsDTO.getGoodsSort());
        tarDto.setCreateTime(dto.getCreateTime());
        tarDto.setCategoryId(dto.getCategoryId());
        tarDto.setCategoryName(dto.getCategoryName());
        tarDto.setGoodsSynopsis(dto.getGoodsSynopsis());
        tarDto.setGoodsAddType(dto.getGoodsAddType());
        tarDto.setGoodsSalesChannels(dto.getGoodsSalesChannels());
        tarDto.setGoodsSpecType(dto.getGoodsSpecType());
        tarDto.setPackPrice(dto.getPackPrice());
        return tarDto;
    }


    public Result<MarketingSeckillDetailVO> detail(Long seckillId) {
        CateringMarketingSeckillEntity seckillEntity = seckillClient.getById(seckillId);
        // 装换类型
        MarketingSeckillDetailVO result = BaseUtil.objToObj(seckillEntity, MarketingSeckillDetailVO.class);
        result.setMarketingType(MarketingTypeEnum.SECKILL.getStatus());
        // 设置团购活动对象
        List<String> objectLimit = new ArrayList<>();
        if (seckillEntity.getObjectLimit().equals(MarketingUsingObjectEnum.ALL.getStatus())) {
            objectLimit.add(MarketingUsingObjectEnum.PERSONAL.getStatus().toString());
            objectLimit.add(MarketingUsingObjectEnum.ENTERPRISE.getStatus().toString());
        } else {
            objectLimit.add(seckillEntity.getObjectLimit().toString());
        }
        result.setObjectLimit(objectLimit);
        // 判断当前活动的活动状态
        Integer upDown = seckillEntity.getUpDown();
        LocalDateTime now = LocalDateTime.now();
        if (MarketingUpDownStatusEnum.DOWN.getStatus().equals(upDown)) {
            // 被下架，说明被冻结
            result.setMarketingStatus(MarketingStatusEnum.FREEZE.getStatus());
        } else {
            // 进行时间判断
            if (now.isBefore(seckillEntity.getBeginTime())) {
                // 在开始时间之前
                result.setMarketingStatus(MarketingStatusEnum.NO_BEGIN.getStatus());
            } else if (now.isAfter(seckillEntity.getBeginTime()) && now.isBefore(seckillEntity.getEndTime())) {
                // 在开始时间之后，结束时间之前
                result.setMarketingStatus(MarketingStatusEnum.ING.getStatus());
            } else {
                // 在结束时间之后
                result.setMarketingStatus(MarketingStatusEnum.END.getStatus());
            }
        }
        // 查询活动场次
        Result<List<MarketingSeckillDetailEventVO>> eventList = seckillEventRelationClient.selectEventListBySeckillId(seckillId);
        List<MarketingSeckillDetailEventVO> eventListData = eventList.getData();
        result.setEventList(eventListData);
        // 查询商品列表数据
        // 根据秒杀ID去查询商品信息
        Result<List<MarketingSeckillGoodsDetailVO>> goodsListResult = marketingGoodsClient.detailSeckillGoods(seckillId, 0);
        List<MarketingSeckillGoodsDetailVO> goodsList = goodsListResult.getData();
        if (BaseUtil.judgeList(goodsList)) {
            result.setGoodsList(goodsList);
        } else {
            result.setGoodsList(Collections.emptyList());
        }
        return Result.succ(result);
    }

    public Result<PageData<MarketingSeckillGoodsDetailVO>> detailGoodsPage(MarketingSeckillGoodsPageDTO dto) {
        Result<PageData<MarketingSeckillGoodsDetailVO>> goodsPageResult = marketingGoodsClient.detailSeckillGoodsPage(dto);
        return Result.succ(goodsPageResult.getData());
    }

    public Result<String> freeze(Long seckillId) {
        // 根据秒杀ID集合去冻结秒杀活动
        Result<Boolean> result = seckillClient.freeze(seckillId);
        return result.getData() ? Result.succ("冻结成功") : Result.fail("冻结失败");
    }

    public Result<String> del(Long seckillId) {
        // 根据秒杀ID集合去删除秒杀活动
        Result<Boolean> result = seckillClient.del(seckillId);
        return result.getData() ? Result.succ("删除成功") : Result.fail("删除失败");
    }

    public Result<MarketingSeckillEffectVO> effect(Long seckillId) {
        MarketingSeckillEffectVO vo = new MarketingSeckillEffectVO();
        vo.setId(seckillId);
        // 查询秒杀活动预计拉新、预计增长营业额 true
        CateringMarketingSeckillEntity seckillEntity = seckillClient.getById(seckillId);
        if(null == seckillEntity) {
            throw new CustomException("数据不存在");
        }
        vo.setProjectedPullNew(seckillEntity.getUserTarget());
        vo.setProjectedBusiness(seckillEntity.getBusinessTarget());

        // 查询营销商品数据
        Result<List<CateringMarketingGoodsEntity>> marketingGoodsListResult = marketingGoodsClient.selectListByMarketingId(seckillId);
        List<CateringMarketingGoodsEntity> marketingGoodsList = marketingGoodsListResult.getData();
        if(BaseUtil.judgeList(marketingGoodsList)) {
            // 处理营销商品SKU编码集合
            Set<String> skuCodeSet = marketingGoodsList.stream().map(CateringMarketingGoodsEntity::getSku).collect(Collectors.toSet());
            // 查询秒杀场次信息
            // 查询秒杀场次
            Result<List<MarketingSeckillDetailEventVO>> listResult = seckillEventRelationClient.selectEventListBySeckillId(seckillId);
            List<MarketingSeckillDetailEventVO> seckillEventList = listResult.getData();
            // 计算总共的场次
            int eventCount = getSeckillEventCount(seckillEntity, seckillEventList);
            // 处理秒杀场次集合
            Set<Long> seckillEventIdSet = Collections.emptySet();
            if(BaseUtil.judgeList(seckillEventList)) {
                seckillEventIdSet = seckillEventList.stream().map(MarketingSeckillDetailEventVO::getEventId).collect(Collectors.toSet());
            }
            // 查询实际成本 true
            BigDecimal realCost = getRealCost(seckillId, seckillEventIdSet, skuCodeSet);
            vo.setRealCost(realCost);
            // 查询实际拉新人数 true
            Result<Integer> realPullNew = pullNewClient.marketingPullCount(seckillId);
            vo.setRealPullNew(realPullNew.getData());
            // 查询关联订单总数量 true
            Result<Integer> relationOrderCount = orderActivityClient.marketingRelationOrderCount(seckillId);
            vo.setRelationOrderCount(relationOrderCount.getData());
            // 查询秒杀商品销售额
            BigDecimal business = getBusiness(seckillId, seckillEventIdSet, skuCodeSet);
            vo.setBusiness(business);
            // 查询实际增长营业额 true
            Result<BigDecimal> realBusinessResult = orderActivityClient.marketingRealBusinessCount(seckillId);
            vo.setRealBusiness(realBusinessResult.getData() == null ? new BigDecimal(0) : realBusinessResult.getData());
            // 计算预计成本  预计成本=实际成本+未销售完毕的商品的预计成本
            // 查询每个营销商品所有场次已经售出的数量 true
            Result<List<MarketingRepertoryGoodsSoldVo>> goodsSoldCountResult = repertoryClient.marketingProjectedCostCount(seckillId);
            List<MarketingRepertoryGoodsSoldVo> goodsSoldCount = goodsSoldCountResult.getData();
            Map<Long, MarketingRepertoryGoodsSoldVo> goodsSoldMap = goodsSoldCount.stream()
                    .collect(Collectors.toMap(MarketingRepertoryGoodsSoldVo::getMGoodsId, Function.identity()));
            // 查询每个营销商品的所有场次加起来的总发行数量 以及处理商品列表数据
            BigDecimal projectedCost = new BigDecimal(0);
            if (BaseUtil.judgeList(marketingGoodsList)) {
                // 有商品
                Map<Long, CateringMarketingGoodsEntity> marketingGoodsMap = marketingGoodsList.stream()
                        .collect(Collectors.toMap(CateringMarketingGoodsEntity::getId, Function.identity()));
                Set<Map.Entry<Long, CateringMarketingGoodsEntity>> entries = marketingGoodsMap.entrySet();
                for (Map.Entry<Long, CateringMarketingGoodsEntity> item : entries) {
                    CateringMarketingGoodsEntity value = item.getValue();
                    // 计算该商品所有场次加起来的数量
                    Integer totalQuantity = value.getQuantity() * eventCount;
                    // 减去已经销售的数量得到还未卖出去的商品数量
                    int remainingQuantity = totalQuantity - goodsSoldMap.get(item.getKey()).getSoldCount();
                    // 计算还未卖出去的商品成本
                    BigDecimal goodsProjectedCost = value.getStorePrice().subtract(value.getActivityPrice()).multiply(new BigDecimal(remainingQuantity));
                    // 累计其他商品的成本
                    projectedCost = projectedCost.add(goodsProjectedCost);
                }
            }
            // 预计成本 = 已售出商品实际成本（包含退款） + 未售出商品计算成本
            if(!seckillEventIdSet.isEmpty()) {
                Result<BigDecimal> realCostResult = orderActivityClient.seckillRealCostCount(seckillId, skuCodeSet, seckillEventIdSet, 1);
                vo.setProjectedCost(projectedCost.add(realCostResult.getData() == null ? new BigDecimal(0) : realCostResult.getData()));
            } else {
                Result<BigDecimal> realCostResult = orderActivityClient.oldSeckillRealCostCount(seckillId, 1);
                vo.setProjectedCost(projectedCost.add(realCostResult.getData() == null ? new BigDecimal(0) : realCostResult.getData()));
            }
        } else {
            vo.setRealCost(new BigDecimal(0));
            vo.setBusiness(new BigDecimal(0));
            vo.setRelationOrderCount(0);
            vo.setRealBusiness(new BigDecimal(0));
            vo.setRealPullNew(0);
            vo.setProjectedCost(new BigDecimal(0));
        }
        return Result.succ(vo);
    }

    private int getSeckillEventCount(CateringMarketingSeckillEntity seckillEntity, List<MarketingSeckillDetailEventVO> seckillEventList) {
        // 计算选择的天数
        LocalDateTime beginTime = seckillEntity.getBeginTime();
        LocalDateTime endTime = seckillEntity.getEndTime();
        int days = 0;
        while (beginTime.toLocalDate().isBefore(endTime.toLocalDate()) || beginTime.toLocalDate().isEqual(endTime.toLocalDate())) {
            days++;
            beginTime = beginTime.plusDays(1);
        }
        int eventCount = 1;
        if(BaseUtil.judgeList(seckillEventList)) {
            // 每天秒杀场次
            int size = seckillEventList.size();
            // 计算一共有几场秒杀
            eventCount = days * size;
        }
        return eventCount;
    }

    private BigDecimal getRealCost(Long seckillId, Set<Long> seckillEventIdSet, Set<String> skuCodeSet) {
        BigDecimal realCost = new BigDecimal(0);
        if(!seckillEventIdSet.isEmpty()) {
            Result<BigDecimal> realCostResult = orderActivityClient.seckillRealCostCount(seckillId, skuCodeSet, seckillEventIdSet, 0);
            if(null != realCostResult.getData()) {
                realCost = realCostResult.getData();
            }
        } else {
            Result<BigDecimal> realCostResult = orderActivityClient.oldSeckillRealCostCount(seckillId, 0);
            if(null != realCostResult.getData()) {
                realCost = realCostResult.getData();
            }
        }
        return realCost;
    }

    private BigDecimal getBusiness(Long seckillId, Set<Long> seckillEventIdSet, Set<String> skuCodeSet) {
        final BigDecimal[] business = {new BigDecimal(0)};
        // 查询每个营销商品的销售额
        if(!seckillEventIdSet.isEmpty()) {
            Result<List<MarketingOrderGoodsCountVo>> businessOrderGoodsResult = orderActivityClient.seckillGoodsBusinessCount(seckillId, skuCodeSet, seckillEventIdSet);
            List<MarketingOrderGoodsCountVo> businessOrderGoodsList = businessOrderGoodsResult.getData();
            if (BaseUtil.judgeList(businessOrderGoodsList)) {
                businessOrderGoodsList.forEach(item -> {
                    business[0] = business[0].add(item.getOrderBusinessCount());
                });
            }
        } else {
            Result<List<MarketingOrderGoodsCountVo>> businessOrderGoodsResult = orderActivityClient.oldSeckillGoodsBusinessCount(seckillId);
            List<MarketingOrderGoodsCountVo> businessOrderGoodsList = businessOrderGoodsResult.getData();
            if (BaseUtil.judgeList(businessOrderGoodsList)) {
                businessOrderGoodsList.forEach(item -> {
                    business[0] = business[0].add(item.getOrderBusinessCount());
                });
            }
        }
        return business[0];
    }

    public Result<PageData<MarketingSeckillGoodsEffectVO>> effectGoods(MarketingSeckillGoodsPageDTO dto) {
        // 查询秒杀商品信息
        Result<PageData<CateringMarketingGoodsEntity>> marketingGoodsResult = marketingGoodsClient
                .selectPageByMarketingId(dto.getPageNo(), dto.getPageSize(), dto.getSeckillId());
        PageData<CateringMarketingGoodsEntity> marketingGoodsData = marketingGoodsResult.getData();
        List<CateringMarketingGoodsEntity> marketingGoodsList = marketingGoodsData.getList();
        if (!BaseUtil.judgeList(marketingGoodsList)) {
            return Result.succ(new PageData<>(Collections.emptyList(), 0, true));
        }

        // 类型转换为结果数据
        List<MarketingSeckillGoodsEffectVO> goodsList = BaseUtil.objToObj(marketingGoodsList, MarketingSeckillGoodsEffectVO.class);

        // 处理营销商品SKU编码集合
        Set<String> skuCodeSet = goodsList.stream().map(MarketingSeckillGoodsEffectVO::getSku).collect(Collectors.toSet());

        // 查询秒杀活动信息
        CateringMarketingSeckillEntity seckillEntity = seckillClient.getById(dto.getSeckillId());
        // 查询每个营销商品的所有场次加起来的总发行数量
        // 查询秒杀场次
        Result<List<MarketingSeckillDetailEventVO>> seckillEventListResult = seckillEventRelationClient.selectEventListBySeckillId(seckillEntity.getId());
        List<MarketingSeckillDetailEventVO> seckillEventList = seckillEventListResult.getData();
        // 计算总共的场次
        int eventCount = getSeckillEventCount(seckillEntity, seckillEventList);
        // 处理秒杀场次集合
        Set<Long> seckillEventIdSet = Collections.emptySet();
        if(BaseUtil.judgeList(seckillEventList)) {
            seckillEventIdSet = seckillEventList.stream().map(MarketingSeckillDetailEventVO::getEventId).collect(Collectors.toSet());
        }

        // 查询每个营销商品所有场次实际已经售出的数量（不包含退款）
        Result<List<MarketingRepertoryGoodsSoldVo>> goodsSoldCountResult;
        if(!seckillEventIdSet.isEmpty()) {
            goodsSoldCountResult = orderActivityClient.seckillSoldCount(seckillEntity.getId(), skuCodeSet, seckillEventIdSet);
        }else {
            goodsSoldCountResult = orderActivityClient.oldSeckillSoldCount(seckillEntity.getId());
        }
        List<MarketingRepertoryGoodsSoldVo> goodsSoldCount = goodsSoldCountResult.getData();
        Map<String, MarketingRepertoryGoodsSoldVo> goodsSoldMap = goodsSoldCount.stream()
                .collect(Collectors.toMap(MarketingRepertoryGoodsSoldVo::getGoodsSkuCode, Function.identity()));

        // 查询每个营销商品所有场次实际已经售出的数量（包含退款）
        Result<List<MarketingRepertoryGoodsSoldVo>> goodsAllSoldCountResult = repertoryClient.marketingProjectedCostCount(seckillEntity.getId());
        List<MarketingRepertoryGoodsSoldVo> goodsAllSoldCount = goodsAllSoldCountResult.getData();
        Map<Long, MarketingRepertoryGoodsSoldVo> goodsAllSoldMap = goodsAllSoldCount.stream()
                .collect(Collectors.toMap(MarketingRepertoryGoodsSoldVo::getMGoodsId, Function.identity()));

        // 查询每个营销商品的销售额
        Result<List<MarketingOrderGoodsCountVo>> businessOrderGoodsResult;
        if(!seckillEventIdSet.isEmpty()) {
            businessOrderGoodsResult = orderActivityClient.seckillGoodsBusinessCount(seckillEntity.getId(), skuCodeSet, seckillEventIdSet);
        } else {
            businessOrderGoodsResult = orderActivityClient.oldSeckillGoodsBusinessCount(seckillEntity.getId());
        }
        List<MarketingOrderGoodsCountVo> businessOrderGoodsList = businessOrderGoodsResult.getData();
        Map<String, MarketingOrderGoodsCountVo> businessOrderGoodsMap = businessOrderGoodsList.stream()
                .collect(Collectors.toMap(MarketingOrderGoodsCountVo::getSku, Function.identity()));
        // 循环商品计算商品总场次发行量、剩余数量、售出总数量、每个商品的销售额
        Map<Long, MarketingSeckillGoodsEffectVO> marketingGoodsMap = goodsList.stream()
                .collect(Collectors.toMap(MarketingSeckillGoodsEffectVO::getId, Function.identity()));
        Set<Map.Entry<Long, MarketingSeckillGoodsEffectVO>> entries = marketingGoodsMap.entrySet();
        for (Map.Entry<Long, MarketingSeckillGoodsEffectVO> item : entries) {
            MarketingSeckillGoodsEffectVO value = item.getValue();
            // 计算该商品所有场次加起来的数量
            Integer totalQuantity = value.getQuantity() * eventCount;
            value.setQuantity(totalQuantity);
            // 减去已经销售的数量得到还未卖出去的商品数量
            MarketingRepertoryGoodsSoldVo soldOutQuantity = goodsSoldMap.get(item.getValue().getSku());
            int remainingQuantity = totalQuantity - goodsAllSoldMap.get(item.getKey()).getSoldCount();
            value.setRemainingQuantity(remainingQuantity);
            // 设置售卖总数量
            value.setSoldOutQuantity(soldOutQuantity == null ? Integer.valueOf(0) : soldOutQuantity.getSoldCount());
            // 关联订单量
            // 设置销售额 以及 关联订单量
            MarketingOrderGoodsCountVo orderGoodsCount = businessOrderGoodsMap.get(value.getSku());
            if (orderGoodsCount == null) {
                value.setRelationOrderNum(0);
                value.setBusiness(new BigDecimal(0));
            } else {
                value.setRelationOrderNum(orderGoodsCount.getOrderNumCount());
                value.setBusiness(orderGoodsCount.getOrderBusinessCount());
            }
        }
        PageData<MarketingSeckillGoodsEffectVO> result = new PageData<>();
        result.setTotal(marketingGoodsData.getTotal());
        result.setList(goodsList);
        result.setLastPage(PageUtil.lastPages(marketingGoodsData.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        return Result.succ(result);
    }

    public Result<List<MarketingSeckillEventSelectVO>> eventSelect() {
        Result<List<CateringMarketingSeckillEventEntity>> seckillEventList = seckillEventClient.list();
        List<CateringMarketingSeckillEventEntity> data = seckillEventList.getData();
        List<MarketingSeckillEventSelectVO> result = BaseUtil.objToObj(data, MarketingSeckillEventSelectVO.class);
        result.forEach(item -> {
            item.setEventTime(item.getBeginTime().toString() + "-" + item.getEndTime().toString());
        });
        return Result.succ(result);
    }

}
