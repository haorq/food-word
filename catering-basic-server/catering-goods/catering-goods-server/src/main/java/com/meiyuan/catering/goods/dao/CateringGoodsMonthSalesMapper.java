package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.goods.entity.CateringGoodsMonthSalesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wxf
 * @date 2020/4/8 16:51
 * @description 简单描述
 **/
@Mapper
public interface CateringGoodsMonthSalesMapper extends BaseMapper<CateringGoodsMonthSalesEntity> {
    /**
     * 获取商户id对应的商品销量数据
     *
     * @author: wxf
     * @date: 2020/4/9 10:25
     * @param merchantId 商户id
     * @param time 时间
     * @param goodsIdList 商品id集合
     * @return: {@link List< CateringGoodsMonthSalesEntity>}
     **/
    List<CateringGoodsMonthSalesEntity> list(@Param("merchantId") Long merchantId,
                                             @Param("time") LocalDate time,
                                             @Param("list") List<Long> goodsIdList);

    /**
     * 批量保存
     *
     * @author: wxf
     * @date: 2020/6/23 10:46
     * @param list 保存数据集合
     * @version 1.1.0
     **/
    void listSave(List<CateringGoodsMonthSalesEntity> list);

    /**
     * 30天的销量
     *
     * @author: wxf
     * @date: 2020/4/13 11:25
     * @param merchantList 商户id集合
     * @return: {@link List< CateringGoodsMonthSalesEntity>}
     **/
    List<CateringGoodsMonthSalesEntity> thirtyDaysSales(@Param("merchantList") List<Long> merchantList);

    /**
     * 条件删除
     *
     * @author: wxf
     * @date: 2020/6/23 10:47
     * @param shopId 门店id
     * @param time 时间
     * @param goodsIdList 商品id集合
     * @version 1.1.0
     **/
    void del(@Param("shopId") Long shopId,
             @Param("time") LocalDate time,
             @Param("list") List<Long> goodsIdList);

    /**
     * 商品总销量
     *
     * @author: wxf
     * @date: 2020/4/22 18:30
     * @param goodsIdList 商品id集合
     * @return: {@link List< CateringGoodsMonthSalesEntity>}
     **/
    List<CateringGoodsMonthSalesEntity> goodsTotal(@Param("list") List<Long> goodsIdList);

    /**
     * 删除所有
     *
     * @author: wxf
     * @date: 2020/6/23 10:48
     * @version 1.1.0
     **/
    void delAll();
}
