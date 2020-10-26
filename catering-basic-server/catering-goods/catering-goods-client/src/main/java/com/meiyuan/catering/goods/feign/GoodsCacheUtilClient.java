package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.util.GoodsCacheUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wxf
 * @date 2020/5/20 11:03
 * @description 简单描述
 **/
@Service
public class GoodsCacheUtilClient {
    @Resource
    GoodsCacheUtil goodsCacheUtil;

    /**
     * 获取商品对应的月销售量
     *
     * @author: wxf
     * @date: 2020/4/9 14:02
     * @param shopId 门店id
     * @param goodsId 商品id
     **/
    public Result<Long> getGoodsMonthSalesCache(Long shopId, Long goodsId) {
        return Result.succ(goodsCacheUtil.getGoodsMonthSalesCache(shopId, goodsId));
    }

    /**
     * 描述:批量获取商品月销
     *
     * @param keys
     * @return com.meiyuan.catering.core.util.Result<java.util.Map < java.lang.String , java.lang.Long>>
     * @author zengzhangni
     * @date 2020/8/10 14:33
     * @since v1.3.0
     */
    public Result<Map<String, Long>> getMonthSalesAll(Set<String> keys) {
        return Result.succ(goodsCacheUtil.getMonthSalesAll(keys));
    }

    /**
     * 批量新增/修改缓存
     *
     * @author: wxf
     * @date: 2020/4/9 14:18
     * @param goodsList 对应的销量数据集合
     **/
    public void batchSaveOrUpdateCache(List<GoodsMonthSalesDTO> goodsList) {
        goodsCacheUtil.batchSaveOrUpdateCache(goodsList);
    }
}
