package com.meiyuan.catering.order.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersGoodsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单商品表(CateringOrdersGoods)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringOrdersGoodsMapper extends BaseMapper<CateringOrdersGoodsEntity>{


}
