package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dto.goods.GoodsDataDTO;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsDataEntity;
import com.meiyuan.catering.goods.service.CateringGoodsDataService;
import com.meiyuan.catering.goods.dao.CateringGoodsDataMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品综合数据表(CateringGoodsData)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringGoodsDataServiceImpl extends ServiceImpl<CateringGoodsDataMapper,CateringGoodsDataEntity>
        implements CateringGoodsDataService {
    @Resource
    private CateringGoodsDataMapper cateringGoodsDataMapper;


    /**
     * 处理商品月销量的数据新增更新商品总销量
     *
     * @param goodsList 商品月销量集合
     * @author: wxf
     * @date: 2020/4/9 15:02
     * @return: {@link boolean}
     **/
    @Override
    public boolean saveUpdateBatch(List<GoodsMonthSalesDTO> goodsList) {
        List<Long> goodsIdList =
                goodsList.stream().map(GoodsMonthSalesDTO::getGoodsId).collect(Collectors.toList());
        // 查询 商品综合数据表对应的数据集合
        QueryWrapper<CateringGoodsDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CateringGoodsDataEntity::getGoodsId, goodsIdList);
        List<CateringGoodsDataEntity> goodsDataList = this.list(queryWrapper);
        // 根据 商品id分组
        Map<Long, List<GoodsMonthSalesDTO>> goodsMap = goodsList.stream().
                collect(
                        Collectors.groupingBy(GoodsMonthSalesDTO::getGoodsId)
                );
        // K = 商品id  V = 销售量
        Map<Long, Long> goodsMonthSalesMap = new HashMap<>(goodsMap.size());
        // 遍历 goodsMap 设置 goodsMonthSalesMap 的 K,V
        for (Map.Entry<Long, List<GoodsMonthSalesDTO>> goods : goodsMap.entrySet()) {
            long monthSales = goods.getValue().stream().mapToLong(GoodsMonthSalesDTO::getNumber).sum();
            goodsMonthSalesMap.put(goods.getKey(), monthSales);
        }
        // 遍历设置对应的销售量
        goodsDataList.forEach(
                goodsData -> {
                    Long monthSales = goodsMonthSalesMap.getOrDefault(goodsData.getGoodsId(), null);
                    if (null != monthSales) {
                        goodsData.setSalesCount(goodsData.getSalesCount() + monthSales);
                    }
                }
        );
        return this.updateBatchById(goodsDataList);
    }

    @Override
    public List<GoodsDataDTO> list(List<Long> goodsIdList) {
        List<CateringGoodsDataEntity> list = cateringGoodsDataMapper.list(goodsIdList);
        return BaseUtil.noNullAndListToList(list, GoodsDataDTO.class);
    }
}