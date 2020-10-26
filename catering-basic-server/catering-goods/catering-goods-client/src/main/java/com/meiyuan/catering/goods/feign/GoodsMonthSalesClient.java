package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsMonthSalesEntity;
import com.meiyuan.catering.goods.service.CateringGoodsMonthSalesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author wxf
 * @date 2020/5/22 18:15
 * @description 简单描述
 **/
@Service
public class GoodsMonthSalesClient {
    @Resource
    CateringGoodsMonthSalesService cateringGoodsMonthSalesService;

    /**
     * 获取商户id对应的商品销量数据
     *
     * @author: wxf
     * @date: 2020/4/9 10:25
     * @param merchantId 商户id
     * @param time 时间
     * @param goodsIdList 商品id集合
     * @return: {@link List< GoodsMonthSalesDTO>}
     **/
    public Result<List<GoodsMonthSalesDTO>> list(Long merchantId, LocalDate time, List<Long> goodsIdList) {
        return Result.succ(cateringGoodsMonthSalesService.list(merchantId, time, goodsIdList));
    }

    /**
     * 30天的销量
     *
     * @author: wxf
     * @date: 2020/4/13 11:25
     * @param shopIdList 门店id集合
     * @return: {@link List< GoodsMonthSalesDTO>}
     **/
    public Result<List<GoodsMonthSalesDTO>> thirtyDaysSales(List<Long> shopIdList) {
        return Result.succ(cateringGoodsMonthSalesService.thirtyDaysSales(shopIdList));
    }

    /**
     * 删除对应的月销量
     *
     * @author: wxf
     * @date: 2020/4/22 18:09
     * @param merchantId 商户id
     * @param payTime 时间
     * @param goodsIdList 商品id集合
     **/
    public void del(Long merchantId, LocalDate payTime, List<Long> goodsIdList) {
        cateringGoodsMonthSalesService.del(merchantId, payTime, goodsIdList);
    }

    /**
     * 商品总销量
     *
     * @author: wxf
     * @date: 2020/4/22 18:30
     * @param goodsIdList 商品id集合
     * @return: {@link List< GoodsMonthSalesDTO>}
     **/
    public Result<List<GoodsMonthSalesDTO>> goodsTotal(List<Long> goodsIdList) {
        return Result.succ(cateringGoodsMonthSalesService.goodsTotal(goodsIdList));
    }

    public void delAll() {
        cateringGoodsMonthSalesService.delAll();
    }

    public Result<Boolean> saveOrUpdateBatch(List<GoodsMonthSalesDTO> dtoList, int size) {
        return Result.succ(cateringGoodsMonthSalesService.saveOrUpdateBatch(BaseUtil.objToObj(dtoList, CateringGoodsMonthSalesEntity.class), size));
    }


}
