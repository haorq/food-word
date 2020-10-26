package com.meiyuan.catering.finance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.finance.entity.CateringRechargeActivityEntity;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Mapper
public interface CateringRechargeActivityMapper extends BaseMapper<CateringRechargeActivityEntity> {


    /**
     * 条件查询
     *
     * @param rechargeAccount
     * @param name
     * @param beginTime
     * @param endTime
     * @return
     */
    List<CateringRechargeActivityEntity> listByCondition(@Param("rechargeAccount")BigDecimal rechargeAccount, @Param("name") String name, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
}
