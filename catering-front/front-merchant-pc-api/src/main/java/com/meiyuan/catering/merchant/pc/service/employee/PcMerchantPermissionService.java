package com.meiyuan.catering.merchant.pc.service.employee;

import com.meiyuan.catering.admin.dto.admin.admin.CommonPermissionRespDTO;
import com.meiyuan.catering.admin.dto.admin.admin.CommonPermissionSaveParamsDTO;
import com.meiyuan.catering.admin.enums.base.PermissionTypeEnum;
import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.admin.fegin.RolePermissionRelationClient;
import com.meiyuan.catering.admin.vo.role.AdminMenuVO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PcMerchantPermissionService {

    @Autowired
    private RoleClient roleClient;

    @Autowired
    private RolePermissionRelationClient permissionRelationClient;


    /**
     * 根据角色id获取权限
     *
     * @param merchantAccountDTO
     * @param type               权限类型，0:商户pc，1：app
     * @return
     */
    public Result<List<AdminMenuVO>> getPermissionByRoleId(MerchantAccountDTO merchantAccountDTO, Integer type) {
        PermissionTypeEnum permissionTypeEnum = PermissionTypeEnum.parse(type);
        return roleClient.listMenu(permissionTypeEnum);
    }

    /**
     * 保存角色的权限关系
     *
     * @param roleId
     * @param list
     * @return
     */
    public Result<Object> savePermission( Long roleId, List<CommonPermissionSaveParamsDTO> list) {
        return permissionRelationClient.savePermission(roleId, list);
    }
}
