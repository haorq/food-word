package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringSubjectRoleRelationMapper;
import com.meiyuan.catering.admin.dto.role.RoleRelationDTO;
import com.meiyuan.catering.admin.entity.CateringSubjectRoleRelationEntity;
import com.meiyuan.catering.admin.service.CateringSubjectRoleRelationService;
import com.meiyuan.catering.core.exception.CustomException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * describe: 角色关系
 *
 * @version 1.5.0
 * @author: fql
 * @date: 2020/9/29 14:41
 **/
@Service
public class CateringSubjectRoleRelationServiceImpl extends ServiceImpl<CateringSubjectRoleRelationMapper, CateringSubjectRoleRelationEntity> implements CateringSubjectRoleRelationService {

    @Resource
    private CateringSubjectRoleRelationMapper cateringSubjectRoleRelationMapper;

    @Override
    public List<RoleRelationDTO> selectRoleRelationByShopId(Long subjectId) {
        return cateringSubjectRoleRelationMapper.selectRoleRelationByShopId(subjectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateEmployeeRole(Long employeeId, String roleStr, boolean isDel) {
        if(!isDel && StringUtils.isEmpty(roleStr)){
            return;
        }

        //删除旧角色
        UpdateWrapper<CateringSubjectRoleRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CateringSubjectRoleRelationEntity::getSubjectId, employeeId);
        this.baseMapper.delete(updateWrapper);
        if (StringUtils.isEmpty(roleStr)) {
            return;
        }
        //添加新角色
        List<String> roleList = Arrays.asList(roleStr.split(","));
        List<Long> roleIds = roleList.stream().map(Long::valueOf).collect(Collectors.toList());

        List<CateringSubjectRoleRelationEntity> addRoleList = roleIds.stream().map(id -> {
            CateringSubjectRoleRelationEntity entity = new CateringSubjectRoleRelationEntity();
            entity.setSubjectId(employeeId);
            entity.setRoleId(id);
            return entity;
        }).collect(Collectors.toList());
        this.saveBatch(addRoleList);
    }

    @Override
    public void delAllEmployeeRole(List<Long> employeeIds){
        this.baseMapper.delAllRelation(employeeIds);
    }

    @Override
    public void verifyHasUserOfRole(Long roleId) {
        LambdaQueryWrapper<CateringSubjectRoleRelationEntity> cateringSubjectRoleRelationEntityQueryWrapper = Wrappers.lambdaQuery();
        cateringSubjectRoleRelationEntityQueryWrapper.eq(CateringSubjectRoleRelationEntity::getRoleId, roleId);
        List<CateringSubjectRoleRelationEntity> cateringSubjectRoleRelationEntities = this.list(cateringSubjectRoleRelationEntityQueryWrapper);
        if (CollectionUtils.isNotEmpty(cateringSubjectRoleRelationEntities)) {
            throw new CustomException("角色下有关联账号不可删除!");
        }
    }
}
