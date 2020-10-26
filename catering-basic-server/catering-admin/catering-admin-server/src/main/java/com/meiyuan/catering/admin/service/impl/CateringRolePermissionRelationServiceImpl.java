package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringRolePermissionRelationMapper;
import com.meiyuan.catering.admin.dto.admin.admin.CommonPermissionSaveParamsDTO;
import com.meiyuan.catering.admin.entity.CateringRoleEntity;
import com.meiyuan.catering.admin.entity.CateringRolePermissionRelationEntity;
import com.meiyuan.catering.admin.service.CateringPermissionService;
import com.meiyuan.catering.admin.service.CateringRolePermissionRelationService;
import com.meiyuan.catering.admin.service.CateringRoleService;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.Result;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CateringRolePermissionRelationServiceImpl extends ServiceImpl<CateringRolePermissionRelationMapper, CateringRolePermissionRelationEntity>
        implements CateringRolePermissionRelationService {

    @Autowired
    private CateringPermissionService cateringPermissionService;

    @Autowired
    private CateringRoleService cateringRoleService;

    @Override
    public void delPermissionByRoleId(Long roleId) {
        LambdaQueryWrapper<CateringRolePermissionRelationEntity> rolePermissionRelationEntityLambdaQueryWrapper = Wrappers.lambdaQuery();
        rolePermissionRelationEntityLambdaQueryWrapper.eq(CateringRolePermissionRelationEntity::getRoleId, roleId);
        this.remove(rolePermissionRelationEntityLambdaQueryWrapper);
    }

    @Override
    public List<CateringRolePermissionRelationEntity> getPermissionsByRoleId(Long roleId) {
        LambdaQueryWrapper<CateringRolePermissionRelationEntity> rolePermissionRelationEntityLambdaQueryWrapper = Wrappers.lambdaQuery();
        rolePermissionRelationEntityLambdaQueryWrapper.eq(CateringRolePermissionRelationEntity::getRoleId, roleId);
        return this.list(rolePermissionRelationEntityLambdaQueryWrapper);
    }

    @Override
    public Result<Object> addRolePermissionRelation(List<CateringRolePermissionRelationEntity> addCateringRolePermissionRelationEntityList) {

        return Result.succ(this.saveBatch(addCateringRolePermissionRelationEntityList));
    }


    /**
     * 保存角色的权限
     *
     * @param roleId
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> savePermission(Long roleId, List<CommonPermissionSaveParamsDTO> list) {
        //校验角色是否存在
        CateringRoleEntity cateringRoleEntity = cateringRoleService.getById(roleId);
        if (cateringRoleEntity == null) {
            throw new CustomException("角色不存在！");
        }
        List<Long> collect = list.stream().filter(i->CollectionUtils.isNotEmpty(i.getPermissionIds())).map(CommonPermissionSaveParamsDTO::getPermissionIds).flatMap(Collection::stream).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            throw new CustomException("角色权限不能为空，请勾选角色对应权限");
        }
        //先删除数据
        this.delPermissionByRoleId(roleId);
        List<CateringRolePermissionRelationEntity> entityList = collect.stream()
                .map(e -> new CateringRolePermissionRelationEntity(IdWorker.getId(), roleId, e)).collect(Collectors.toList());

        return Result.succ(this.saveBatch(entityList));
    }
}
