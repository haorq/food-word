package com.meiyuan.catering.job.service.goods;

import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.GoodsDataDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsMonthSalesEntity;
import com.meiyuan.catering.goods.feign.GoodsDataClient;
import com.meiyuan.catering.goods.feign.GoodsMonthSalesClient;
import com.meiyuan.catering.goods.feign.GoodsUtilClient;
import com.meiyuan.catering.goods.util.GoodsCacheUtil;
import com.meiyuan.catering.order.dto.goods.GoodsMonthSalesDTO;
import com.meiyuan.catering.order.feign.OrderClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/4/22 9:10
 * @description 简单描述
 **/
@Service
public class JobGoodsDataService {
    @Resource
    GoodsUtilClient goodsUtilClient;
    @Resource
    OrderClient orderClient;
    @Resource
    GoodsMonthSalesClient goodsMonthSalesClient;
    @Resource
    GoodsDataClient goodsDataClient;
    @Resource
    GoodsCacheUtil goodsCacheUtil;

    /**
     * 月销
     *
     * @author: wxf
     * @date: 2020/4/22 15:54
     **/
    @Transactional(rollbackFor = Exception.class)
    public void goodsMonthSales(Integer days) {
        // 获取当日完成订单对应的商品数据
        // K 门店id == V 当日对应的所有商品销量
        Map<Long, List<GoodsMonthSalesDTO>> dataMap = orderClient.goodsSalesByDays(null, days);
        if (dataMap != null && !dataMap.isEmpty()) {
            dataMap.forEach(
                    (k, v) -> {
                        List<CateringGoodsMonthSalesEntity> entityList = BaseUtil.objToObj(v, CateringGoodsMonthSalesEntity.class);
                        if (BaseUtil.judgeList(entityList)) {
                            // 执行 处理销量办法
                            goodsUtilClient.orderComplete(k, LocalDate.now(), BaseUtil.objToObj(entityList, com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO.class));
                            List<Long> goodsIdList =
                                    entityList.stream().map(CateringGoodsMonthSalesEntity::getGoodsId).collect(Collectors.toList());
                            // 更新对应data库里面对应商品总销量
                            Result<List<com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO>> goodsMonthListResult
                                    = goodsMonthSalesClient.goodsTotal(goodsIdList);
                            if (goodsMonthListResult.success() && BaseUtil.judgeList(goodsMonthListResult.getData())) {
                                List<com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO> goodsMonthSalesList = goodsMonthListResult.getData();
                                // 获取对应的 商品综合数据集合
                                Result<List<GoodsDataDTO>> goodsDataResult =
                                        goodsDataClient.list(goodsIdList);
                                if (goodsDataResult.success() && BaseUtil.judgeList(goodsDataResult.getData())) {
                                    List<GoodsDataDTO> goodsDataList = goodsDataResult.getData();
                                    // K = 商品id  V = 本身
                                    Map<Long, com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO> goodsMonthSalesMap
                                            = goodsMonthSalesList.stream().
                                            collect(Collectors.toMap(com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO::getGoodsId, i -> i));
                                    // 遍历 设置销量
                                    goodsDataList.forEach(
                                            i -> {
                                                com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO dto
                                                        = goodsMonthSalesMap.getOrDefault(i.getGoodsId(), null);
                                                if (null != dto) {
                                                    i.setSalesCount(dto.getNumber());
                                                }
                                            }
                                    );
                                    goodsDataClient.updateBatchById(goodsDataList);
                                }
                            }
                        }
                    }
            );
            // 获取对应商户对应商品集合的30天销量
            Result<List<com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO>> goodsMonthListResult =
                    goodsMonthSalesClient.thirtyDaysSales(new ArrayList<>(dataMap.keySet()));
            if (goodsMonthListResult.success() && BaseUtil.judgeList(goodsMonthListResult.getData())) {
                List<com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO> saveList = goodsMonthListResult.getData();
                // 更新缓存
                goodsCacheUtil.batchSaveOrUpdateCache(saveList);
            }
        }
    }

    /**
     * 重置所有店铺的商品月销量
     *
     * @author: wxf
     * @date: 2020/4/24 10:00
     **/
    @Transactional(rollbackFor = Exception.class)
    public void resetMonthSales() {
        // 清空 商品月销量表
        goodsMonthSalesClient.delAll();
        // 获取所有完成订单的商品数据
        Map<Long, List<GoodsMonthSalesDTO>> allGoodsSalesMap = orderClient.goodsSalesByDays(null, null);
        if (null != allGoodsSalesMap) {
            Collection<List<GoodsMonthSalesDTO>> values = allGoodsSalesMap.values();
            // 全部商品数据
            List<GoodsMonthSalesDTO> goodsMonthSalesSaveList = values.stream().flatMap(Collection::stream).collect(Collectors.toList());

            goodsMonthSalesClient.saveOrUpdateBatch(BaseUtil.objToObj(goodsMonthSalesSaveList, com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO.class), goodsMonthSalesSaveList.size());
            // key -> 商品id value -> 对应销量
            Map<Long, Long> goodsSalesMap = goodsMonthSalesSaveList.stream().collect(
                    Collectors.groupingBy(
                            GoodsMonthSalesDTO::getGoodsId,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    i -> i.stream().mapToLong(GoodsMonthSalesDTO::getNumber).sum()
                            )
                    )
            );
            Result<List<GoodsDataDTO>> goodsDataResult = goodsDataClient.list();
            if (goodsDataResult.success() && BaseUtil.judgeList(goodsDataResult.getData())) {
                // 修改商品总销量表的数据
                List<GoodsDataDTO> goodsDataList = goodsDataResult.getData();
                goodsDataList.forEach(
                        i -> {
                            Long number = goodsSalesMap.getOrDefault(i.getGoodsId(), null);
                            if (null != number) {
                                i.setSalesCount(number);
                            }
                        }
                );
                goodsDataClient.updateBatchById(goodsDataList);
                // 清空缓存
                goodsCacheUtil.delAll();
                Integer month = 30;
                // 取30天的数据覆盖缓存
                Map<Long, List<GoodsMonthSalesDTO>> goodsMonthSalesMap = orderClient.goodsSalesByDays(null, month);
                if (null != goodsMonthSalesMap) {
                    List<GoodsMonthSalesDTO> cacheDtoList =
                            goodsMonthSalesMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                    if (BaseUtil.judgeList(cacheDtoList)) {
                        goodsCacheUtil.batchSaveOrUpdateCache(BaseUtil.objToObj(cacheDtoList, com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO.class));
                    }
                }
            }
        }
    }

    /**
     * 更新昨天的销量并加上今天的月销
     *
     * @author: wxf
     * @date: 2020/4/24 13:42
     **/
    public void updateYesterdayGoodsSales() {
        Integer yesTerDay = 1;
        goodsMonthSales(yesTerDay);
        Integer today = 0;
        goodsMonthSales(today);
    }
}
