package com.meiyuan.catering.admin.fegin;


import com.meiyuan.catering.admin.dto.admin.admin.CommonPermissionSaveParamsDTO;
import com.meiyuan.catering.admin.entity.CateringRolePermissionRelationEntity;
import com.meiyuan.catering.admin.service.CateringRolePermissionRelationService;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionRelationClient {


    @Autowired
    private CateringRolePermissionRelationService cateringRolePermissionRelationService;

    public void delRolePermissionRelationByRoleId(Long roleId) {
        cateringRolePermissionRelationService.delPermissionByRoleId(roleId);
    }

    public List<CateringRolePermissionRelationEntity> getPermissionsByRoleId(Long roleId) {
        return cateringRolePermissionRelationService.getPermissionsByRoleId(roleId);
    }

    public Result<Object> addRolePermissionRelation(List<CateringRolePermissionRelationEntity> addCateringRolePermissionRelationEntityList) {
        return cateringRolePermissionRelationService.addRolePermissionRelation(addCateringRolePermissionRelationEntityList);
    }

    public Result<Object> savePermission(Long roleId, List<CommonPermissionSaveParamsDTO> list) {
        return cateringRolePermissionRelationService.savePermission(roleId, list);
    }
}
