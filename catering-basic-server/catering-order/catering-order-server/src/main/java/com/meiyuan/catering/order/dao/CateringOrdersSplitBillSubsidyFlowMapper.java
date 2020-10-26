package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillSubsidyFlowEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/09 11:10
 * @description 补贴分账流水表数据库操作Mapper
 **/

@Mapper
public interface CateringOrdersSplitBillSubsidyFlowMapper extends BaseMapper<CateringOrdersSplitBillSubsidyFlowEntity> {

    /**
    * 查询所有可以进行补贴分账的信息
    * @author: GongJunZheng
    * @date: 2020/10/11 16:05
    * @return: {@link List<CateringOrdersSplitBillSubsidyFlowEntity>}
    * @version V1.5.0
    **/
    List<CateringOrdersSplitBillSubsidyFlowEntity> listCanSubsidy();
}
