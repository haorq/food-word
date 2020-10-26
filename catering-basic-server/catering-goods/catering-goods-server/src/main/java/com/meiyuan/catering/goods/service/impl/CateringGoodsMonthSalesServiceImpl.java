package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dao.CateringGoodsMonthSalesMapper;
import com.meiyuan.catering.goods.dto.monthsales.GoodsMonthSalesDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsMonthSalesEntity;
import com.meiyuan.catering.goods.service.CateringGoodsMonthSalesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author wxf
 * @date 2020/4/8 16:55
 * @description 简单描述
 **/
@Service
public class CateringGoodsMonthSalesServiceImpl extends ServiceImpl<CateringGoodsMonthSalesMapper, CateringGoodsMonthSalesEntity>
        implements CateringGoodsMonthSalesService {
    @Resource
    CateringGoodsMonthSalesMapper goodsMonthSalesMapper;

    /**
     * 获取商户id对应的商品销量数据
     *
     * @param merchantId  商户id
     * @param time        时间
     * @param goodsIdList 商品id集合
     * @author: wxf
     * @date: 2020/4/9 10:25
     * @return: {@link List < GoodsMonthSalesDTO>}
     **/
    @Override
    public List<GoodsMonthSalesDTO> list(Long merchantId, LocalDate time, List<Long> goodsIdList) {
        List<CateringGoodsMonthSalesEntity> list = goodsMonthSalesMapper.list(merchantId, time, goodsIdList);
        return BaseUtil.noNullAndListToList(list, GoodsMonthSalesDTO.class);
    }

    /**
     * 30天的销量
     *
     * @param merchantIdList  商户id集合
     * @author: wxf
     * @date: 2020/4/13 11:25
     * @return: {@link List< GoodsMonthSalesDTO>}
     **/
    @Override
    public List<GoodsMonthSalesDTO> thirtyDaysSales(List<Long> merchantIdList) {
        List<CateringGoodsMonthSalesEntity> list = goodsMonthSalesMapper.thirtyDaysSales(merchantIdList);
        return BaseUtil.noNullAndListToList(list, GoodsMonthSalesDTO.class);
    }

    /**
     * 删除对应的月销量
     *
     * @param merchantId  商户id
     * @param payTime     时间
     * @param goodsIdList 商品id集合
     * @author: wxf
     * @date: 2020/4/22 18:09
     **/
    @Override
    public void del(Long merchantId, LocalDate payTime, List<Long> goodsIdList) {
        goodsMonthSalesMapper.del(merchantId, payTime, goodsIdList);
    }

    /**
     * 商品总销量
     *
     * @param goodsIdList 商品id集合
     * @author: wxf
     * @date: 2020/4/22 18:30
     * @return: {@link List< GoodsMonthSalesDTO>}
     **/
    @Override
    public List<GoodsMonthSalesDTO> goodsTotal(List<Long> goodsIdList) {
        List<CateringGoodsMonthSalesEntity> list = goodsMonthSalesMapper.goodsTotal(goodsIdList);
        return BaseUtil.noNullAndListToList(list, GoodsMonthSalesDTO.class);
    }

    @Override
    public void delAll() {
        goodsMonthSalesMapper.delAll();
    }
}
