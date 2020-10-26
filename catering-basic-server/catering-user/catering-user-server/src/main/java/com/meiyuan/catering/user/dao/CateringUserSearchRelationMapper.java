package com.meiyuan.catering.user.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.entity.CateringUserSearchRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户用户搜素历史关联表(CateringUserSearchRelation)表数据库访问层
 *
 * @author mei-tao
 * @since 2020-03-10 15:29:17
 */
@Mapper
public interface CateringUserSearchRelationMapper extends BaseMapper<CateringUserSearchRelationEntity>{


}