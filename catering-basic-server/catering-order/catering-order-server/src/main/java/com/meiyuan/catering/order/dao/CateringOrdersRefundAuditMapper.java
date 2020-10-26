package com.meiyuan.catering.order.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersRefundAuditEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款订单审核表(CateringOrdersRefundAudit)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringOrdersRefundAuditMapper extends BaseMapper<CateringOrdersRefundAuditEntity>{


}
