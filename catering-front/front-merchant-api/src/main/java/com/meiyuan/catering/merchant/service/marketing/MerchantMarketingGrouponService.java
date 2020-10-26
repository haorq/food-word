package com.meiyuan.catering.merchant.service.marketing;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.entity.CateringMarketingGoodsEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGroupOrderEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingGrouponEntity;
import com.meiyuan.catering.marketing.enums.MarketingGroupOrderStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.feign.*;
import com.meiyuan.catering.marketing.vo.groupon.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.groupon.MerchantGrouponQueryDTO;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;
import com.meiyuan.catering.order.feign.OrderActivityClient;
import com.meiyuan.catering.order.vo.marketing.MarketingOrderGoodsCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author luohuan
 * @date 2020/3/23
 **/
@Service
public class MerchantMarketingGrouponService {
    @Autowired
    private MarketingGrouponClient grouponClient;
    @Autowired
    private MarketingGoodsClient marketingGoodsClient;
    @Autowired
    private MarketingGroupOrderClient groupOrderClient;

    @Autowired
    private OrderActivityClient orderActivityClient;
    @Autowired
    private MarketingRepertoryClient repertoryClient;
    @Autowired
    private MarketingPullNewClient pullNewClient;
    @Autowired
    private MarketingSeckillEventClient seckillEventClient;
    @Autowired
    private MarketingSeckillEventRelationClient seckillEventRelationClient;

    /**
     * 分页查询团购活动
     *
     * @param queryDTO   查询条件
     * @param merchantId 商户ID
     * @return Result<IPage               <               MerchantGrouponListVO>>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<IPage<MerchantGrouponListVO>> listPage(MerchantGrouponQueryDTO queryDTO, Long merchantId) {
        return grouponClient.listPageOfMerchant(queryDTO, merchantId);
    }

    /**
     * 团购活动详情
     *
     * @param grouponId 团购ID
     * @return Result<MerchantGrouponDetailVO>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<MerchantGrouponDetailVO> detail(Long grouponId) {
        //团购主要数据
        Result<MerchantGrouponDetailVO> entityResult = grouponClient.getDetailGroupon(grouponId);
        MerchantGrouponDetailVO detailVO = entityResult.getData();
        //数据组装
        if (ObjectUtils.isNotEmpty(detailVO)) {
            //商品数据
            // 是否需要查询已被删除的商品信息 0-否 1-是
            int type = 1;
            if(MarketingStatusEnum.NO_BEGIN.getStatus().equals(detailVO.getStatus())) {
                type = 0;
            }
            Result<List<MarketingGrouponGoodsDetailVO>> result = marketingGoodsClient.detailGrouponGoods(grouponId, type);
            //活动数据
            MarketingGrouponEffectVO effectVO = getMarketingGrouponEffectVO(detailVO);
            detailVO.setEffectVOS(effectVO);
            List<MarketingGrouponGoodsDetailVO> goodsList = result.getData();
            List<MerchantGrouponGoodsListVO> grouponGoodsList = goodsList.stream().map(item -> {
                MerchantGrouponGoodsListVO goodsVo = BaseUtil.objToObj(item, MerchantGrouponGoodsListVO.class);
                goodsVo.setMarketPrice(item.getStorePrice());
                goodsVo.setMinGroupQuantity(item.getMinGrouponQuantity());
                return goodsVo;
            }).collect(Collectors.toList());
            detailVO.setGrouponGoodsList(grouponGoodsList);
        }
        return Result.succ(detailVO);
    }

    private MarketingGrouponEffectVO getMarketingGrouponEffectVO(MerchantGrouponDetailVO detailVO) {
        Long grouponId = detailVO.getId();

        //活动数据
        MarketingGrouponEffectVO vo = new MarketingGrouponEffectVO();

        // 查询预计拉新、预计增长营业额
        CateringMarketingGrouponEntity grouponEntity = grouponClient.getById(grouponId).getData();
        if (grouponEntity == null) {
            throw new CustomException("数据不存在");
        }
        // 预计增长营业额
        vo.setProjectedBusiness(grouponEntity.getBusinessTarget());
        //预计拉新
        vo.setProjectedPullNew(grouponEntity.getUserTarget());

        // 查询营销商品列表
        Result<List<CateringMarketingGoodsEntity>> marketingGoodsListResult = marketingGoodsClient.selectListByMarketingId(grouponId);
        List<CateringMarketingGoodsEntity> marketingGoodsList = marketingGoodsListResult.getData();
        if(BaseUtil.judgeList(marketingGoodsList)) {
            // 查询实际拉新
            Result<Integer> realPullNew = pullNewClient.marketingPullCount(grouponId);
            vo.setRealPullNew(realPullNew.getData());

            // 查询实际营业金额
            Result<BigDecimal> realBusinessResult = orderActivityClient.marketingRealBusinessCount(grouponId);
            vo.setRealBusiness(realBusinessResult.getData() == null ? new BigDecimal(0) : realBusinessResult.getData());

            // 查询实际成本（不包含退款）
            Result<BigDecimal> realCost = orderActivityClient.grouponRealCostCount(grouponId, 0);
            vo.setRealCost(null == realCost.getData() ? new BigDecimal(0) : realCost.getData());

            // 预计成本直接计算：（团购商品原价-活动价）*起团总数量
            final BigDecimal[] foreCost = {new BigDecimal(0)};
            marketingGoodsList.forEach(item -> {
                BigDecimal multiply = item.getStorePrice().subtract(item.getActivityPrice()).multiply(new BigDecimal(item.getMinGrouponQuantity()));
                foreCost[0] = foreCost[0].add(multiply);
            });
            vo.setProjectedCost(foreCost[0]);
            if(MarketingStatusEnum.NO_BEGIN.getStatus().equals(detailVO.getStatus())) {
                // 未开始的团购活动，不进行成团以及参团人数统计
                // 已成团数量
                vo.setFinishGroup(0);
                // 未成团数量
                vo.setNotGroup(0);
                // 查询参团总人数
                vo.setTotalGroupMember(0);
            } else {
                // 查询团单数据
                List<CateringMarketingGroupOrderEntity> groupOrderList = groupOrderClient.listByOfId(grouponId).getData();
                // 处理团购活动的成团情况以及参团人数
                MarketingGrouponGroupInfoVo groupInfo = groupOrderClient.makeGroupInfo(grouponEntity, groupOrderList, marketingGoodsList.size()).getData();
                // 已成团数量
                vo.setFinishGroup(groupInfo.getFinishGroup());
                // 未成团数量
                vo.setNotGroup(groupInfo.getNotGroup());
                // 查询参团总人数
                vo.setTotalGroupMember(groupInfo.getTotalGroupMember());
            }
            // 查询商品销售额
            List<MarketingOrderGoodsCountVo> businessResultData = orderActivityClient.grouponGoodsBusinessCount(grouponId).getData();
            vo.setBusiness(new BigDecimal(0));
            if(BaseUtil.judgeList(businessResultData)) {
                businessResultData.forEach(item -> {
                    vo.setBusiness(vo.getBusiness().add(item.getOrderBusinessCount() == null ? new BigDecimal(0) : item.getOrderBusinessCount()));
                });
            }
        } else {
            vo.setRealPullNew(0);
            vo.setRealBusiness(new BigDecimal(0));
            vo.setRealCost(new BigDecimal(0));
            vo.setProjectedCost(new BigDecimal(0));
            vo.setFinishGroup(0);
            vo.setNotGroup(0);
            vo.setTotalGroupMember(0);
            vo.setBusiness(new BigDecimal(0));
        }
        return vo;
    }

    /**
     * 开启/取消虚拟成团
     *
     * @param id 团购ID
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result turnOnVirtual(Long id) {
        return grouponClient.turnOnVirtual(id);
    }

    public Result<String> freeze(Long id) {
        // 根据团购ID集合去冻结团购活动
        Result<Boolean> result = grouponClient.freeze(id);
        Boolean data = result.getData();
        if(data) {
            // 数据库冻结失败，处理已经参加了团购的用户的数据，处理成团购失败
            groupOrderClient.freezeEndGroup(id);
        }
        return result.getData() ? Result.succ("冻结成功") : Result.fail("冻结失败");
    }
}
