package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.entity.CateringIntegralRuleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 积分规则表(CateringIntegralRule)表数据库访问层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:15:10
 */
@Mapper
public interface CateringIntegralRuleMapper extends BaseMapper<CateringIntegralRuleEntity> {


    /**
     * 所有
     *
     * @return
     */
    List<CateringIntegralRuleEntity> listAll();

}
