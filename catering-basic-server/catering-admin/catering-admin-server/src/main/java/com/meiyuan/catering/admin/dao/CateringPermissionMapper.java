package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.admin.entity.CateringPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CateringPermissionMapper
 * @Description
 * @Author gz
 * @Date 2020/9/29 11:13
 * @Version 1.5.0
 */
@Mapper
public interface CateringPermissionMapper extends BaseMapper<CateringPermissionEntity> {

    List<CateringPermissionEntity> getPermissionByRoleId(@Param("roleId") Long roleId, @Param("type") Integer type);

}
