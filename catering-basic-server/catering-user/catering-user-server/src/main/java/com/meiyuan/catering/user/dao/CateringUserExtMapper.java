package com.meiyuan.catering.user.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.entity.CateringUserExtEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户扩展表(CateringUserExt)表数据库访问层
 *
 * @author mei-tao
 * @since 2020-03-10 15:29:17
 */
@Mapper
public interface CateringUserExtMapper extends BaseMapper<CateringUserExtEntity>{


}