package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.entity.CateringIntegralActivityEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-18
 */
@Mapper
public interface CateringIntegralActivityMapper extends BaseMapper<CateringIntegralActivityEntity> {

    /**
     * 通过id更新 不过滤null
     *
     * @param entity
     * @return
     */
    boolean modifyById(CateringIntegralActivityEntity entity);

    /**
     * 查询所有没删除的积分活动的规则编码
     *
     * @return
     */
    List<String> queryNotDeleteList();

    /**
     * 评价相关积分
     *
     * @param userType
     * @return
     */
    List<CateringIntegralActivityEntity> appraiseIntegral(Integer userType);
}
