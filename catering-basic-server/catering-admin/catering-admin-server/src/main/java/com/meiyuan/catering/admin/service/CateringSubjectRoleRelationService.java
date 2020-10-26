package com.meiyuan.catering.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.dto.role.RoleRelationDTO;
import com.meiyuan.catering.admin.entity.CateringSubjectRoleRelationEntity;

import java.util.List;

public interface CateringSubjectRoleRelationService extends IService<CateringSubjectRoleRelationEntity> {

    /**
     * describe:
     * @author: fql
     * @date: 2020/9/29 14:46
     * @param subjectId 员工id
     * @return: {@link List<RoleRelationDTO>}
     * @version 1.5.0
     **/
    List<RoleRelationDTO> selectRoleRelationByShopId(Long subjectId);


    /**
     * describe:
     * @author: fql
     * @date: 2020/9/29 16:13
     * @param employeeId 员工id
     * @param roleStr 角色id集合字符串
     * @param isDel 是否删除
     * @return: {@link}
     * @version 1.5.0
     **/
    void updateEmployeeRole(Long employeeId, String roleStr, boolean isDel);


    /**
     * describe: 批量删除角色关系
     * @author: fql
     * @date: 2020/10/10 9:08
     * @param employeeIds 员工ids
     * @return: {@link }
     * @version 1.5.0
     **/
    void delAllEmployeeRole(List<Long> employeeIds);


    /**
     * 校验角色是否关联用户
     * @param roleId
     */
    void verifyHasUserOfRole(Long roleId);

}
