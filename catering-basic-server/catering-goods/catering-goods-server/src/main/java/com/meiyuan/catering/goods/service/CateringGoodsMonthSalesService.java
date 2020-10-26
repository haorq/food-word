package com.meiyuan.catering.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsMonthSalesEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wxf
 * @date 2020/4/8 16:53
 * @description 简单描述
 **/
public interface CateringGoodsMonthSalesService extends IService<CateringGoodsMonthSalesEntity> {
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
    List<GoodsMonthSalesDTO> list(Long merchantId, LocalDate time, List<Long> goodsIdList);

    /**
     * 30天的销量
     *
     * @author: wxf
     * @date: 2020/4/13 11:25
     * @param merchantIdList 商户id集合
     * @return: {@link List< GoodsMonthSalesDTO>}
     **/
    List<GoodsMonthSalesDTO> thirtyDaysSales(List<Long> merchantIdList);

    /**
     * 删除对应的月销量
     *
     * @author: wxf
     * @date: 2020/4/22 18:09
     * @param merchantId 商户id
     * @param payTime 时间
     * @param goodsIdList 商品id集合
     **/
    void del(Long merchantId, LocalDate payTime, List<Long> goodsIdList);

    /**
     * 商品总销量
     *
     * @author: wxf
     * @date: 2020/4/22 18:30
     * @param goodsIdList 商品id集合
     * @return: {@link List< GoodsMonthSalesDTO>}
     **/
    List<GoodsMonthSalesDTO> goodsTotal(List<Long> goodsIdList);

    /**
     * 删除所有
     *
     * @author: wxf
     * @date: 2020/6/23 10:48
     * @version 1.1.0
     **/
    void delAll();
}
