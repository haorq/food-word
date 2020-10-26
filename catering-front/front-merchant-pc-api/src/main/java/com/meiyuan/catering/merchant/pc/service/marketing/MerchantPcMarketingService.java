package com.meiyuan.catering.merchant.pc.service.marketing;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.sku.GoodsBySkuDTO;
import com.meiyuan.catering.goods.feign.LabelClient;
import com.meiyuan.catering.marketing.dto.marketing.MarketingSeckillGrouponPageQueryDTO;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import com.meiyuan.catering.marketing.feign.MarketingClient;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponOrderAmountCountVO;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponPageQueryVO;
import com.meiyuan.catering.marketing.vo.pullnew.MarketingPullNewCountVO;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingGoodsSelectDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingGoodsSkuDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingSelectGoodsQueryDTO;
import com.meiyuan.catering.merchant.goods.dto.goods.MarketingSpecialGoodsSelectDTO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.goods.vo.MarketingGoodsSelectVO;
import com.meiyuan.catering.merchant.goods.vo.MarketingSelectGoodsSkuVO;
import com.meiyuan.catering.merchant.goods.vo.MarketingSelectGoodsVO;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsSelectVO;
import com.meiyuan.catering.order.feign.OrderActivityClient;
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
 * @date 2020/08/05 14:08
 * @description 店铺管理--营销活动V1.3.0服务层
 **/

@Slf4j
@Service
public class MerchantPcMarketingService {

    @Autowired
    private MarketingClient marketingClient;
    @Autowired
    private OrderActivityClient orderActivityClient;
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private LabelClient labelClient;

    public Result<PageData<MarketingSeckillGrouponPageQueryVO>> pageQuery(MerchantAccountDTO token, MarketingSeckillGrouponPageQueryDTO dto) {
        // 逻辑=======
        // 设置门店ID
        dto.setShopId(token.getAccountTypeId());
        PageData<MarketingSeckillGrouponPageQueryVO> pageData = marketingClient.pageQuery(dto);
        // 只要不是未进行状态，都需要查询拉新人数，查询关联订单营业统计
        if(!MarketingStatusEnum.NO_BEGIN.getStatus().equals(dto.getMarketingStatus())) {
            // 查询当前活动完成的目标
            List<Long> marketingIds = pageData.getList().stream().map(MarketingSeckillGrouponPageQueryVO::getId).collect(Collectors.toList());
            // 查询营业增长情况
            Map<Long, MarketingSeckillGrouponOrderAmountCountVO> orderAmountCountMap = orderAmountCount(marketingIds);
            // 查询拉新情况
            Map<Long, MarketingPullNewCountVO> pullNewCountMap = marketingClient.pullNewCount(marketingIds);
            pageData.getList().forEach(item -> {
                // 获取营业增长
                MarketingSeckillGrouponOrderAmountCountVO orderAmountCount = orderAmountCountMap.get(item.getId());
                BigDecimal finishAmountCount = null == orderAmountCount ? new BigDecimal(0) : orderAmountCount.getCount();
                item.setFinishBusinessTarget(finishAmountCount);
                // 获取拉新情况
                MarketingPullNewCountVO pullCount = pullNewCountMap.get(item.getId());
                Integer finishPullNewCount = null == pullCount ? 0 : pullCount.getCount();
                item.setFinishUserTarget(finishPullNewCount);
            });
        }
        // 逻辑==========
        return Result.succ(pageData);
    }

    private Map<Long, MarketingSeckillGrouponOrderAmountCountVO> orderAmountCount(List<Long> marketingIds) {
        if(!BaseUtil.judgeList(marketingIds)) {
            // 没有活动ID集合，直接返回空MAP
            return Collections.emptyMap();
        }
        List<MarketingSeckillGrouponOrderAmountCountVO> orderAmountCountList = orderActivityClient.marketingOrderAmountCount(marketingIds);
        if (!BaseUtil.judgeList(orderAmountCountList)) {
            return new HashMap<>(16);
        }
        return orderAmountCountList.stream().collect(Collectors.toMap(MarketingSeckillGrouponOrderAmountCountVO::getOfId, Function.identity()));
    }

    private String buildMarketingTarget(Integer userTarget, BigDecimal businessTarget) {
        return String.format("拉新%s位新用户，增长%s营业额", userTarget, businessTarget);
    }

    /**
     * 获取商品集合根据sku编码集合
     *
     * @param skuCodeList sku编码集合
     * @author: wxf
     * @date: 2020/3/19 16:54
     * @return: {@link List<GoodsBySkuDTO>}
     **/
    public Result<List<GoodsBySkuDTO>> listGoodsBySkuCodeList(List<String> skuCodeList,Long shopId) {
        List<MarketingGoodsSkuDTO> goodsList = merchantGoodsClient.listGoodsBySkuCodeList(skuCodeList,shopId);
        // 获取标签
        if(CollectionUtils.isNotEmpty(goodsList)){
            List<Long> collect = goodsList.stream().map(MarketingGoodsSkuDTO::getGoodsId).distinct().collect(Collectors.toList());
            Map<Long,List<String>> labelMap = labelClient.listNameByGoodsId(goodsList.get(0).getMerchantId(), collect);
            goodsList.forEach(e->{
                e.setLabelNames(labelMap.get(e.getGoodsId()));
            });
        }
        return Result.succ(ConvertUtils.sourceToTarget(goodsList,GoodsBySkuDTO.class));
    }

    public Result<List<MarketingGoodsSelectVO>> groupBuyGoods(MerchantAccountDTO token, MarketingGoodsSelectDTO dto) {
        // 设置店铺ID
        dto.setShopId(token.getAccountTypeId());
        List<MarketingSelectGoodsVO> list = merchantGoodsClient.marketingGoodsSelectQuery(dto);
        // 类型转换
        List<MarketingGoodsSelectVO> goodsList = new ArrayList<>();
        for (MarketingSelectGoodsVO goodsItem : list) {
            for (MarketingSelectGoodsSkuVO skuItem : goodsItem.getSkuList()) {
                MarketingGoodsSelectVO vo = new MarketingGoodsSelectVO();
                vo.setGoodsId(goodsItem.getGoodsId());
                vo.setGoodsName(goodsItem.getGoodsName());
                vo.setSpuCode(goodsItem.getSpuCode());
                vo.setCategoryId(goodsItem.getCategoryId());
                vo.setCategoryName(goodsItem.getCategoryName());
                vo.setUnit(goodsItem.getUnit());
                vo.setSkuId(skuItem.getId());
                vo.setSkuCode(skuItem.getSkuCode());
                vo.setGoodsSpecType(goodsItem.getGoodsSpecType());
                vo.setPropertyValue(skuItem.getPropertyValue());
                vo.setStorePrice(skuItem.getMarketPrice());
                vo.setSalesPrice(skuItem.getSalesPrice());
                vo.setEnterprisePrice(skuItem.getEnterprisePrice());
                goodsList.add(vo);
            }
        }
        return Result.succ(goodsList);
    }

    public Result<List<MarketingSpecialGoodsSelectVO>> specialGoodsSelect(MerchantAccountDTO token, MarketingSpecialGoodsSelectDTO dto) {
        List<MarketingSpecialGoodsSelectVO> goodsList = new ArrayList<>();
        MarketingGoodsSelectDTO selectDTO = new MarketingGoodsSelectDTO();
        selectDTO.setShopId(token.getAccountTypeId());
        selectDTO.setCategoryId(dto.getCategoryId());
        selectDTO.setGoodsName(dto.getGoodsName());
        List<MarketingSelectGoodsVO> list = merchantGoodsClient.marketingGoodsSelectQuery(selectDTO);
        if(BaseUtil.judgeList(list)) {
            for (MarketingSelectGoodsVO goodsItem : list) {
                for (MarketingSelectGoodsSkuVO skuItem : goodsItem.getSkuList()) {
                    MarketingSpecialGoodsSelectVO vo = new MarketingSpecialGoodsSelectVO();
                    vo.setGoodsId(goodsItem.getGoodsId());
                    vo.setCategoryId(goodsItem.getCategoryId());
                    vo.setCategoryName(goodsItem.getCategoryName());
                    vo.setGoodsName(goodsItem.getGoodsName());
                    vo.setSkuCode(skuItem.getSkuCode());
                    vo.setPropertyValue(skuItem.getPropertyValue());
                    vo.setGoodsSpecType(goodsItem.getGoodsSpecType());
                    vo.setUnit(goodsItem.getUnit());
                    vo.setMarketPrice(skuItem.getMarketPrice());
                    vo.setShopSkuId(skuItem.getId());
                    vo.setMinQuantity(skuItem.getMinQuantity());
                    goodsList.add(vo);
                }
            }
        }
        return Result.succ(goodsList);
    }
}
