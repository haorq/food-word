package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.admin.dto.admin.admin.AdminQueryDTO;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.entity.CateringSubjectRoleRelationEntity;
import com.meiyuan.catering.admin.vo.admin.admin.AdminListQueryVo;
import com.meiyuan.catering.admin.vo.role.AdminRoleListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/6/23 0023 17:50
 * @Description 简单描述 :  CateringAdminMapper
 * @Since version-1.0.0
 */
@Mapper
public interface CateringAdminMapper extends BaseMapper<CateringAdmin> {
    /**
     * 新增账号角色关联关系
     * @param collect
     * @return
     */
    int insertRoleRelation(@Param("list") List<CateringSubjectRoleRelationEntity> collect);

    /**
     * 删除账号角色关联关系
     * @param accountId
     * @return
     */
    int removeRoleByAccountId(Long accountId);

    /**
     * 通过账号ID获取角色
     * @param id
     * @return
     */
    List<AdminRoleListVO> selectRole(Long id);

    IPage<AdminListQueryVo> querySelect(Page page, @Param("dto") AdminQueryDTO dto);
}
