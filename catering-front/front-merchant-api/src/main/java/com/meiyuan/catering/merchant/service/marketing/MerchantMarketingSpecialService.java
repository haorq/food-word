package com.meiyuan.catering.merchant.service.marketing;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.enums.base.SourceEnum;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.entity.CateringMarketingSpecialEntity;
import com.meiyuan.catering.marketing.enums.*;
import com.meiyuan.catering.marketing.feign.MarketingPullNewClient;
import com.meiyuan.catering.marketing.feign.MarketingSpecialClient;
import com.meiyuan.catering.marketing.vo.special.*;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsMinQuantityVO;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.feign.OrderActivityClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/09/03 20:09
 * @description 营销特价商品服务
 **/

@Slf4j
@Service
public class MerchantMarketingSpecialService {

    @Autowired
    private MarketingSpecialClient specialClient;
    @Autowired
    private OrderActivityClient orderActivityClient;
    @Autowired
    private MarketingPullNewClient marketingPullNewClient;
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private MerchantUtils merchantUtils;

    public Result<MerchantSpecialDetailVO> detail(MerchantTokenDTO token, Long specialId) {
        // 查询基本信息
        Result<CateringMarketingSpecialEntity> specialEntityResult = specialClient.findById(specialId);
        CateringMarketingSpecialEntity specialEntity = specialEntityResult.getData();
        if(null == specialEntity) {
            return Result.succ(new MerchantSpecialDetailVO());
        }
        MerchantSpecialDetailBaseVO baseInfo = BaseUtil.objToObj(specialEntity, MerchantSpecialDetailBaseVO.class);
        baseInfo.setStatus(getStatus(specialEntity));
        baseInfo.setSource(SourceEnum.SHOP.getStatus());
        baseInfo.setSourceStr(SourceEnum.parse(baseInfo.getSource()).getDesc());
        baseInfo.setStatusStr(MarketingGrouponStatusEnum.parse(baseInfo.getStatus()).getDesc());
        baseInfo.setFixTypeStr(MarketingSpecialFixTypeEnum.parse(baseInfo.getFixType()).getDesc());
        baseInfo.setMarketingType(MarketingTypeEnum.SPECIAL.getStatus());
        // 查询商品信息
        Long shopId = token.getShopId();
        ShopInfoDTO shop = merchantUtils.getShop(shopId);
        List<MerchantSpecialDetailGoodsVO> goodsList;
        Result<List<MarketingSpecialGoodsDetailVO>> specialGoodsListResult = specialClient.selectGoodsDetail(shop.getMerchantId(), specialId);
        List<MarketingSpecialGoodsDetailVO> specialGoodsList = specialGoodsListResult.getData();
        if(BaseUtil.judgeList(specialGoodsList)) {
            goodsList = BaseUtil.objToObj(specialGoodsList, MerchantSpecialDetailGoodsVO.class);
            // 查询之前没有设置起售数量的商品的默认起售数量
            Set<String> skuCodeSet = new HashSet<>();
            List<Integer> goodsIndex = new ArrayList<>();
            for (int i = 0; i < goodsList.size(); i++) {
                MerchantSpecialDetailGoodsVO item = goodsList.get(i);
                if(null == item.getMinQuantity()) {
                    skuCodeSet.add(item.getSkuCode());
                    goodsIndex.add(i);
                }
            }
            if(!skuCodeSet.isEmpty()) {
                Result<List<MarketingSpecialGoodsMinQuantityVO>> goodsMinQuantityListResult = merchantGoodsClient.selectGoodsMinQuantity(specialEntity.getMerchantId(), skuCodeSet);
                List<MarketingSpecialGoodsMinQuantityVO> goodsMinQuantityList = goodsMinQuantityListResult.getData();
                Map<String, Integer> goodsMinQuantityMap = goodsMinQuantityList.stream().collect(Collectors.toMap(MarketingSpecialGoodsMinQuantityVO::getSkuCode, MarketingSpecialGoodsMinQuantityVO::getMinQuantity));
                goodsIndex.forEach(item -> {
                    goodsList.get(item).setMinQuantity(goodsMinQuantityMap.get(goodsList.get(item).getSkuCode()));
                });
            }

        } else {
            goodsList = Collections.emptyList();
        }
        // 查询效果数据
        MerchantSpecialDetailEffectVO effect;
        if(!BaseUtil.judgeList(goodsList)) {
            effect= new MerchantSpecialDetailEffectVO();
            effect.setRealCost(new BigDecimal(0));
            effect.setBusiness(new BigDecimal(0));
            effect.setProjectedPullNew(specialEntity.getUserTarget());
            effect.setRealPullNew(0);
            effect.setProjectedBusiness(specialEntity.getBusinessTarget());
            effect.setRealBusiness(new BigDecimal(0));
        } else {
            effect = getEffect(specialEntity);
        }
        // 组装数据
        MerchantSpecialDetailVO vo = new MerchantSpecialDetailVO();
        vo.setBaseInfo(baseInfo);
        vo.setGoodsList(goodsList);
        vo.setEffect(effect);
        return Result.succ(vo);
    }

    private Integer getStatus(CateringMarketingSpecialEntity specialEntity) {
        if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(specialEntity.getUpDown())) {
            return MarketingStatusEnum.FREEZE.getStatus();
        } else {
            return specialEntity.getStatus();
        }
    }

    private MerchantSpecialDetailEffectVO getEffect(CateringMarketingSpecialEntity specialEntity) {
        MerchantSpecialDetailEffectVO vo = new MerchantSpecialDetailEffectVO();
        // 预计增长营业额
        vo.setProjectedPullNew(specialEntity.getUserTarget());
        // 预计拉新
        vo.setProjectedBusiness(specialEntity.getBusinessTarget());
        // 实际成本
        Result<BigDecimal> realCostCountResult = orderActivityClient.specialRealCostCount(specialEntity.getId());
        BigDecimal realCostCount = realCostCountResult.getData();
        vo.setRealCost(null == realCostCount ? new BigDecimal(0) : realCostCount);
        // 商品销售额
        Result<BigDecimal> businessCountResult = orderActivityClient.specialBusinessCostCount(specialEntity.getId());
        BigDecimal businessCount = businessCountResult.getData();
        vo.setBusiness(null == businessCount ? new BigDecimal(0) : businessCount);
        // 实际拉新
        Result<Integer> pullNewCountResult = marketingPullNewClient.marketingPullCount(specialEntity.getId());
        Integer pullNewCount = pullNewCountResult.getData();
        vo.setRealPullNew(null == pullNewCount ? Integer.valueOf(0) : pullNewCount);
        // 实际增长营业额
        Result<BigDecimal> realBusinessCountResult = orderActivityClient.specialRealBusinessCount(specialEntity.getId());
        BigDecimal realBusinessCount = realBusinessCountResult.getData();
        vo.setRealBusiness(null == realBusinessCount ? new BigDecimal(0) : realBusinessCount);
        return vo;
    }

    public Result<String> freeze(Long specialId) {
        Result<Boolean> result = specialClient.freeze(specialId);
        return result.getData() ? Result.succ("冻结成功") : Result.fail("冻结失败");
    }
}
