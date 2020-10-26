package com.meiyuan.catering.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.dto.admin.admin.CommonPermissionSaveParamsDTO;
import com.meiyuan.catering.admin.entity.CateringRolePermissionRelationEntity;
import com.meiyuan.catering.core.util.Result;

import java.util.List;

public interface CateringRolePermissionRelationService extends IService<CateringRolePermissionRelationEntity> {
    void delPermissionByRoleId(Long roleId);

    List<CateringRolePermissionRelationEntity> getPermissionsByRoleId(Long roleId);

    Result<Object> addRolePermissionRelation(List<CateringRolePermissionRelationEntity> addCateringRolePermissionRelationEntityList);

    Result<Object> savePermission(Long roleId, List<CommonPermissionSaveParamsDTO> list);
}
