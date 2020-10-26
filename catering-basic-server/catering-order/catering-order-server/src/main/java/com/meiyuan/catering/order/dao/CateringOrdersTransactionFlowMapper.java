package com.meiyuan.catering.order.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersTransactionFlowEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单交易流水表(CateringOrdersTransactionFlow)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringOrdersTransactionFlowMapper extends BaseMapper<CateringOrdersTransactionFlowEntity>{


}
