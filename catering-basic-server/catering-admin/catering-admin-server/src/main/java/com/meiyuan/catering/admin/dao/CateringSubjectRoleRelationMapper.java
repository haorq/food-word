package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.admin.dto.role.RoleRelationDTO;
import com.meiyuan.catering.admin.entity.CateringSubjectRoleRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CateringSubjectRoleRelationMapper extends BaseMapper<CateringSubjectRoleRelationEntity> {

    /**
     * 根据员工id查询角色信息
     * @param subjectId 门店id或平台id
     * @return 角色信息
     */
    List<RoleRelationDTO> selectRoleRelationByShopId(@Param("id") Long subjectId);

    /**
     * describe: 批量删除角色关系
     * @author: fql
     * @date: 2020/10/10 9:24
     * @param employeeIds 员工id
     * @return: {@link }
     * @version 1.5.0
     **/
    void delAllRelation(@Param("ids") List<Long> employeeIds);
}
