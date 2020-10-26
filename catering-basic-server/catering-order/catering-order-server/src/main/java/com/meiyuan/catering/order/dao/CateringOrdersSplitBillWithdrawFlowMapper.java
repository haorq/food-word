package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillWithdrawFlowEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author GongJunZheng
 * @date 2020/10/09 11:10
 * @description 分账提现流水表数据库操作Mapper
 **/

@Mapper
public interface CateringOrdersSplitBillWithdrawFlowMapper extends BaseMapper<CateringOrdersSplitBillWithdrawFlowEntity> {
}
