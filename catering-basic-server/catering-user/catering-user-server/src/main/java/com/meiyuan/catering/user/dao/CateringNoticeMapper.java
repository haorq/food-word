package com.meiyuan.catering.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.entity.CateringNoticeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zengzhangni
 * @date 2020-05-06
 */
@Mapper
public interface CateringNoticeMapper extends BaseMapper<CateringNoticeEntity> {

    /**
     * 描述:添加浏览量
     *
     * @param id
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/7 10:44
     */
    Boolean pageViewAddV101(Long id);
}
