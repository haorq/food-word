package com.meiyuan.catering.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.order.entity.CateringOrdersSplitBillEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author GongJunZheng
 * @date 2020/10/09 10:10
 * @description 分账表数据库操作Mapper
 **/

@Mapper
public interface CateringOrdersSplitBillMapper extends BaseMapper<CateringOrdersSplitBillEntity> {
}
