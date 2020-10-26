package com.meiyuan.catering.merchant.service.marketing;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantDetailsDTO;
import com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantListDTO;
import com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantPageParamDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEntity;
import com.meiyuan.catering.marketing.enums.MarketingGrouponStatusEnum;
import com.meiyuan.catering.marketing.feign.*;
import com.meiyuan.catering.marketing.vo.groupon.MerchantGrouponDetailVO;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;
import com.meiyuan.catering.marketing.vo.seckill.*;
import com.meiyuan.catering.marketing.vo.marketing.MarketingMerchantAppListVO;
import com.meiyuan.catering.order.feign.OrderActivityClient;
import com.meiyuan.catering.order.vo.marketing.MarketingOrderGoodsCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName MerchantMarketingSeckillService
 * @Description
 * @Author gz
 * @Date 2020/3/21 10:16
 * @Version 1.1
 */
@Service
public class MerchantMarketingSeckillService {

    @Autowired
    private MarketingSeckillClient seckillClient;
    @Autowired
    private MarketingGoodsClient marketingGoodsClient;
    @Autowired
    private OrderActivityClient orderActivityClient;
    @Autowired
    private MarketingRepertoryClient repertoryClient;
    @Autowired
    private MarketingPullNewClient pullNewClient;
    @Autowired
    private MarketingSeckillEventRelationClient seckillEventRelationClient;
    /**
     * 功能描述: 商户秒杀列表<br>
     * @Param: [pageNo, pageSize, status]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.core.page.PageData<com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantListDTO>>
     * @Author: gz
     * @Date: 2020/3/21 10:10
     */
    public Result<PageData<SeckillMerchantListDTO>> pageMerchantList(SeckillMerchantPageParamDTO dto,Long merchantId){
        return seckillClient.pageMerchantList(dto.getPageNo(),dto.getPageSize(),dto.getStatus(),merchantId);
    }

    /**
     * 功能描述: 秒杀活动详情<br>
     * @Param: [id]
     * @Return: com.meiyuan.catering.core.util.Result<com.meiyuan.catering.marketing.dto.seckill.SeckillMerchantDetailsDTO>
     * @Author: gz
     * @Date: 2020/3/21 10:33
     */
    public Result<SeckillMerchantDetailsDTO> seckillInfo(Long id) {
        Result<SeckillMerchantDetailsDTO> merchantInfo = seckillClient.getMerchantInfo(id);
        if (ObjectUtils.isNotEmpty(merchantInfo.getData())){
            MarketingSeckillEffectVO vo = getEffect(merchantInfo.getData(), id);
            if(ObjectUtils.isNotEmpty(vo)){
                merchantInfo.getData().setEffectVO(vo);
            }

        }
        return merchantInfo;
    }

    private MarketingSeckillEffectVO getEffect(SeckillMerchantDetailsDTO merchantInfo, Long id) {
        MarketingSeckillEffectVO vo = new MarketingSeckillEffectVO();
        vo.setId(id);

        // 计算秒杀选择的天数
        int days = 0;
        LocalDateTime beginTime = merchantInfo.getBeginTime();
        LocalDateTime endTime = merchantInfo.getEndTime();
        while (beginTime.toLocalDate().isBefore(endTime.toLocalDate()) || beginTime.toLocalDate().isEqual(endTime.toLocalDate())) {
            days ++;
            beginTime = beginTime.plusDays(1);
        }
        // 查询秒杀场次
        Result<List<MarketingSeckillDetailEventVO>> listResult = seckillEventRelationClient.selectEventListBySeckillId(id);
        List<MarketingSeckillDetailEventVO> seckillEventList = listResult.getData();
        int eventCount = 1;
        if(BaseUtil.judgeList(seckillEventList)) {
            // 每天秒杀场次
            int size = seckillEventList.size();
            // 计算一共有几场秒杀
            eventCount = days * size;
        }

        // 处理秒杀场次集合
        Set<Long> seckillEventIdSet = Collections.emptySet();
        if(BaseUtil.judgeList(seckillEventList)) {
            seckillEventIdSet = seckillEventList.stream().map(MarketingSeckillDetailEventVO::getEventId).collect(Collectors.toSet());
        }

        // 查询秒杀活动预计拉新、预计增长营业额
        CateringMarketingSeckillEntity seckillEntity = seckillClient.getById(id);
        vo.setProjectedBusiness(seckillEntity.getBusinessTarget());
        vo.setProjectedPullNew(seckillEntity.getUserTarget());

        // 查询营销商品数据
        List<CateringMarketingGoodsEntity> marketingGoodsList = marketingGoodsClient.selectListByMarketingId(id).getData();
        if(BaseUtil.judgeList(marketingGoodsList)) {
            // 处理营销商品SKU编码集合
            Set<String> skuCodeSet = marketingGoodsList.stream().map(CateringMarketingGoodsEntity::getSku).collect(Collectors.toSet());

            // 查询实际成本（不包含退款）
            if(!seckillEventIdSet.isEmpty()) {
                Result<BigDecimal> realCost = orderActivityClient.seckillRealCostCount(id, skuCodeSet, seckillEventIdSet, 0);
                vo.setRealCost(null == realCost.getData() ? new BigDecimal(0) : realCost.getData());
            } else {
                Result<BigDecimal> realCost = orderActivityClient.oldSeckillRealCostCount(id, 0);
                vo.setRealCost(null == realCost.getData() ? new BigDecimal(0) : realCost.getData());
            }

            // 查询秒杀商品销售额 true
            vo.setBusiness(new BigDecimal(0));
            // 查询每个营销商品的销售额
            if(!seckillEventIdSet.isEmpty()) {
                Result<List<MarketingOrderGoodsCountVo>> businessOrderGoodsResult = orderActivityClient.seckillGoodsBusinessCount(id, skuCodeSet, seckillEventIdSet);
                List<MarketingOrderGoodsCountVo> businessOrderGoodsList = businessOrderGoodsResult.getData();
                if (BaseUtil.judgeList(businessOrderGoodsList)) {
                    businessOrderGoodsList.forEach(item -> {
                        vo.setBusiness(vo.getBusiness().add(item.getOrderBusinessCount()));
                    });
                }
            } else {
                Result<List<MarketingOrderGoodsCountVo>> businessOrderGoodsResult = orderActivityClient.oldSeckillGoodsBusinessCount(id);
                List<MarketingOrderGoodsCountVo> businessOrderGoodsList = businessOrderGoodsResult.getData();
                if (BaseUtil.judgeList(businessOrderGoodsList)) {
                    businessOrderGoodsList.forEach(item -> {
                        vo.setBusiness(vo.getBusiness().add(item.getOrderBusinessCount()));
                    });
                }
            }

            // 查询关联订单总数量
            Result<Integer> relationOrderCount = orderActivityClient.marketingRelationOrderCount(id);
            vo.setRelationOrderCount(relationOrderCount.getData());

            // 查询实际增长营业额
            Result<BigDecimal> realBusinessResult = orderActivityClient.marketingRealBusinessCount(id);
            vo.setRealBusiness(realBusinessResult.getData() == null ? new BigDecimal(0) : realBusinessResult.getData());

            // 查询实际拉新人数
            Result<Integer> realPullNew = pullNewClient.marketingPullCount(id);
            vo.setRealPullNew(realPullNew.getData());

            // 预计成本 = 已售出商品实际成本（包含退款） + 未售出商品计算成本
            BigDecimal projectedCost = getSoldCost(id, eventCount, marketingGoodsList);
            if(!seckillEventIdSet.isEmpty()) {
                Result<BigDecimal> realCost = orderActivityClient.seckillRealCostCount(id, skuCodeSet, seckillEventIdSet, 1);
                vo.setProjectedCost(projectedCost.add(realCost.getData() == null ? new BigDecimal(0) : realCost.getData()));
            } else {
                Result<BigDecimal> realCost = orderActivityClient.oldSeckillRealCostCount(id, 1);
                vo.setProjectedCost(projectedCost.add(realCost.getData() == null ? new BigDecimal(0) : realCost.getData()));
            }
        } else {
            vo.setRealCost(new BigDecimal(0));
            vo.setBusiness(new BigDecimal(0));
            vo.setRelationOrderCount(0);
            vo.setRealBusiness(new BigDecimal(0));
            vo.setRealPullNew(0);
            vo.setProjectedCost(new BigDecimal(0));
        }
        return vo;
    }


    /**
     * 方法描述   查询成本
     * @author: lhm
     * @date: 2020/8/10 14:17
     * @param seckillId 秒杀活动ID
     * @param eventCount 秒杀活动总场次次数
     * @param marketingGoodsList 秒杀活动营销商品列表
     * @return: {@link BigDecimal}
     * @version 1.3.0
     **/
    private BigDecimal getSoldCost(Long seckillId, int eventCount, List<CateringMarketingGoodsEntity> marketingGoodsList) {
        // 查询每个营销商品所有场次已经售出的数量
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
        return projectedCost;
    }

    public Result<PageData<MarketingMerchantAppListVO>> listMarketing(Long shopId,SeckillMerchantPageParamDTO queryDTO) {
        return seckillClient.listMarketing(shopId,queryDTO);
    }

    public Result<Boolean> freeze(Long id) {
        Result<Boolean> result = seckillClient.freeze(id);
        return result;
    }




}
