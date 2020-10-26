package com.meiyuan.catering.user.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.entity.CateringSearchHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 搜索历史表(CateringSearchHistory)表数据库访问层
 *
 * @author mei-tao
 * @since 2020-03-10 15:29:17
 */
@Mapper
public interface CateringSearchHistoryMapper extends BaseMapper<CateringSearchHistoryEntity>{


}