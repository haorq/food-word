package com.meiyuan.catering.admin.fegin;

import com.meiyuan.catering.admin.dto.role.RoleRelationDTO;
import com.meiyuan.catering.admin.service.CateringSubjectRoleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description：
 *
 * @author fql
 * @version 1.5.0
 * @date 2020/9/29 14:44
 */

@Service
public class RoleRelationClient {

    @Autowired
    private CateringSubjectRoleRelationService cateringSubjectRoleRelationService;

    public List<RoleRelationDTO> selectRoleRelationByShopId(Long subjectId) {
        return cateringSubjectRoleRelationService.selectRoleRelationByShopId(subjectId);
    }

    public void updateEmployeeRole(Long employeeId, String roleStr, boolean isDel){
        cateringSubjectRoleRelationService.updateEmployeeRole(employeeId, roleStr, isDel);
    }

    public void delAllEmployeeRole(List<Long> ids) {
        cateringSubjectRoleRelationService.delAllEmployeeRole(ids);
    }
}
