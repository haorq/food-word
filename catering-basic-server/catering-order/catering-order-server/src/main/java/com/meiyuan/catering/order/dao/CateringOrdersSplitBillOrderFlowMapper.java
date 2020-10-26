package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillOrderFlowEntity;
import com.meiyuan.catering.order.vo.splitbill.CateringOrdersSplitBillOrderFlowVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/09 11:10
 * @description 订单分账流水表数据库操作Mapper
 **/

@Mapper
public interface CateringOrdersSplitBillOrderFlowMapper extends BaseMapper<CateringOrdersSplitBillOrderFlowEntity> {

    /**
    * 查询所有可以进行订单分账的信息
    * @author: GongJunZheng
    * @date: 2020/10/11 15:46
    * @return: {@link List<CateringOrdersSplitBillOrderFlowVO>}
    * @version V1.5.0
    **/
    List<CateringOrdersSplitBillOrderFlowVO> listCanSplitBill();
}
