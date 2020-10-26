package com.meiyuan.catering.order.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.dto.query.wx.CateringOrdersOperationDTO;
import com.meiyuan.catering.order.entity.CateringOrdersOperationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * operation_(CateringOrdersOperation)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringOrdersOperationMapper extends BaseMapper<CateringOrdersOperationEntity>{

    /**
     * 描述:订单进度
     *
     * @param orderId
     * @return java.util.List<com.meiyuan.catering.order.dto.query.wx.CateringOrdersOperationDTO>
     * @author zengzhangni
     * @date 2020/6/24 10:49
     * @since v1.1.1
     */
    List<CateringOrdersOperationDTO> progress(@Param("orderId") Long orderId);

}
