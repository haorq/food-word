package com.meiyuan.catering.goods.util;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.google.gson.Gson;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.JsonUtil;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsMonthSalesEntity;
import com.meiyuan.catering.goods.service.CateringGoodsMonthSalesService;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/4/9 13:52
 * @description 简单描述
 **/
@Component
@Slf4j
public class GoodsCacheUtil {
    @Resource
    CateringGoodsMonthSalesService goodsMonthSalesService;
    @CreateCache(name = GoodsJetcacheNames.GOODS_MONTH_SALES, area = JetcacheAreas.GOODS_AREA)
    private Cache<String, Long> goodsMonthSalesCache;
    @AdvancedCreateCache(@CreateCache(name = GoodsJetcacheNames.GOODS_MONTH_SALES, area = JetcacheAreas.GOODS_AREA))
    private AdvancedCache cache;

    /**
     * 设置商品对应的月销售量
     *
     * @param merchantId 商户id
     * @param goodsId    商品id
     * @param monthSales 月销售量
     * @author: wxf
     * @date: 2020/4/9 14:02
     **/
    public void saveGoodsMonthSalesCache(Long merchantId, Long goodsId, Long monthSales) {
        goodsMonthSalesCache.put(billKey(merchantId, goodsId), monthSales);
    }

    /**
     * 获取商品对应的月销售量
     *
     * @param shopId  门店id
     * @param goodsId 商品id
     * @author: wxf
     * @date: 2020/4/9 14:02
     **/
    public Long getGoodsMonthSalesCache(Long shopId, Long goodsId) {
        Long monthSales = goodsMonthSalesCache.get(billKey(shopId, goodsId));
        return null != monthSales ? monthSales : 0L;
    }

    public Map<String, Long> getMonthSalesAll(Set<String> keys) {
        return goodsMonthSalesCache.getAll(keys);
    }

    private String billKey(Long shopId, Long goodsId) {
        return BaseUtil.billKey(shopId, goodsId);
    }

    /**
     * 批量新增/修改缓存
     *
     * @param dtoList 对应的销量数据集合
     * @author: wxf
     * @date: 2020/4/9 14:18
     **/
    public void batchSaveOrUpdateCache(List<GoodsMonthSalesDTO> dtoList) {
        if (BaseUtil.judgeList(dtoList)) {
            List<CateringGoodsMonthSalesEntity> goodsList = BaseUtil.objToObj(dtoList, CateringGoodsMonthSalesEntity.class);
            Map<String, List<CateringGoodsMonthSalesEntity>> goodsMap
                    = goodsList.stream().collect(
                    Collectors.groupingBy(
                            i -> billKey(i.getShopId(), i.getGoodsId())
                    )
            );
            Map<String, Long> saveMap = new HashMap<>(goodsMap.size());
            goodsMap.forEach(
                    (k, v) -> {
                        long number = v.stream().mapToLong(CateringGoodsMonthSalesEntity::getNumber).sum();
                        saveMap.put(k, number);
                    }
            );

            log.debug("更新门店商品月销量：" + JsonUtil.toJson(saveMap));
            goodsMonthSalesCache.putAll(saveMap);
        }
    }

    /**
     * 删除月销缓存库里面所有的数据
     *
     * @author: wxf
     * @date: 2020/4/24 9:51
     **/
    public void delAll() {
        cache.deleteByPattern(GoodsJetcacheNames.GOODS_MONTH_SALES);
    }
}
