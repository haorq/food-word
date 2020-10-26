package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.admin.entity.CateringLogErrorEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yaoozu
 * @description 操作错误日志
 * @date 2020/3/188:55
 * @since v1.0.0
 */
@Mapper
public interface CateringLogErrorMapper extends BaseMapper<CateringLogErrorEntity> {
}
