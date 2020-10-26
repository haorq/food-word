package com.meiyuan.catering.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meiyuan.catering.admin.dao.CateringRoleMapper;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRolePageListParamsDTO;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRoleParamsDTO;
import com.meiyuan.catering.admin.dto.role.AdminMenuDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleAuthDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleDTO;
import com.meiyuan.catering.admin.dto.role.AdminRolePageDTO;
import com.meiyuan.catering.admin.entity.CateringPermissionEntity;
import com.meiyuan.catering.admin.entity.CateringRoleEntity;
import com.meiyuan.catering.admin.entity.CateringRolePermissionRelationEntity;
import com.meiyuan.catering.admin.enums.base.PermissionTypeEnum;
import com.meiyuan.catering.admin.service.CateringPermissionService;
import com.meiyuan.catering.admin.service.CateringRolePermissionRelationService;
import com.meiyuan.catering.admin.service.CateringRoleService;
import com.meiyuan.catering.admin.vo.admin.admin.BriefRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.CommonRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.PermissionOfRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.PermissionVO;
import com.meiyuan.catering.admin.vo.role.AdminMenuVO;
import com.meiyuan.catering.admin.vo.role.AdminRoleListVO;
import com.meiyuan.catering.admin.vo.role.MerchantMenuVO;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.IsDefaultEnum;
import com.meiyuan.catering.core.exception.AppUnauthorizedException;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.TreeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName CateringRoleServiceImpl
 * @Description
 * @Author gz
 * @Date 2020/9/29 10:12
 * @Version 1.5.0
 */
@Service
public class CateringRoleServiceImpl extends ServiceImpl<CateringRoleMapper, CateringRoleEntity> implements CateringRoleService {


    @Autowired
    private CateringPermissionService permissionService;

    @Autowired
    private CateringRolePermissionRelationService cateringRolePermissionRelationService;

    @Override
    public PageData<AdminRoleListVO> pageRole(AdminRolePageDTO dto) {
        IPage<AdminRoleListVO> iPage = this.baseMapper.pageRole(dto.getPage(), dto);
        List<AdminRoleListVO> records = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> collect = records.stream().map(AdminRoleListVO::getId).collect(Collectors.toList());
            List<AdminRoleListVO> list = this.baseMapper.selectPermission(collect);
            if (CollectionUtils.isNotEmpty(list)) {
                Map<Long, AdminRoleListVO> map = list.stream().collect(Collectors.toMap(AdminRoleListVO::getId, Function.identity()));
                records.forEach(e -> {
                    AdminRoleListVO vo = map.get(e.getId());
                    if (Objects.isNull(vo)) {
                        return;
                    }
                    List<Long> parentList = vo.getParentList();
                    List<Long> permissionList = vo.getPermissionList().stream().filter(i -> !parentList.contains(i)).collect(Collectors.toList());
                    e.setPermissionList(permissionList);
                });
                iPage.setRecords(records);
            }
        }
        return new PageData<>(iPage);
    }

    @Override
    public Boolean saveOrEdit(AdminRoleDTO dto) {
        CateringRoleEntity entity = ConvertUtils.sourceToTarget(dto, CateringRoleEntity.class);
        return this.saveOrUpdate(entity);
    }

    @Override
    public Boolean delete(Long id) {
        return this.baseMapper.removeById(id) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean authorization(AdminRoleAuthDTO dto) {
        this.baseMapper.removePermission(dto.getRoleId());
        List<CateringRolePermissionRelationEntity> collect = dto.getMenuList().stream().map(e -> {
            CateringRolePermissionRelationEntity entity = new CateringRolePermissionRelationEntity();
            entity.setId(IdWorker.getId());
            entity.setPermissionId(e);
            entity.setRoleId(dto.getRoleId());
            return entity;
        }).collect(Collectors.toList());
        return this.baseMapper.insertRolePermission(collect) > 0;
    }

    @SuppressWarnings("all")
    @Override
    public List<Map<String, Object>> loginAccountPermission(Long accountId, boolean isPlatform) {
        List<Map<String, Object>> resList = Lists.newArrayList();
        List<AdminMenuVO> list = this.baseMapper.selectAccountPermission(accountId);
        if (CollectionUtils.isEmpty(list)) {
            return resList;
        }
        List<AdminMenuVO> tree = TreeUtils.build(list);
        if (isPlatform) {
            return tree.stream().map(e -> {
                Map<String, Object> map = Maps.newHashMap();
                map.put(e.getCode(), e.getChildren().stream().map(AdminMenuVO::getCode).collect(Collectors.toList()));
                return map;
            }).collect(Collectors.toList());
        }
        // 商户PC权限由于和平台权限层级数不一样，需要单独处理
        resList = tree.stream().filter(i -> CollectionUtils.isEmpty(i.getChildren())).map(e -> {
            Map<String, Object> map = Maps.newHashMap();
            map.put(e.getCode(), e.getChildren());
            return map;
        }).collect(Collectors.toList());
        resList.addAll(tree.stream().map(AdminMenuVO::getChildren).flatMap(Collection::stream).map(e -> {
            Map<String, Object> map = Maps.newHashMap();
            map.put(e.getCode(), e.getChildren().stream().map(AdminMenuVO::getCode).collect(Collectors.toList()));
            return map;
        }).collect(Collectors.toList()));
        return resList;
    }


    @Override
    public List<Map<String, Object>> getAllPermissionByType(List<Integer> typeList) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<AdminMenuVO> list = this.baseMapper.getAllPermissionByType(typeList);
        if (CollectionUtils.isEmpty(list)) {
            return resultList;
        }
        List<AdminMenuVO> tree = TreeUtils.build(list);
        return tree.stream().map(e -> {
            Map<String, Object> map = Maps.newHashMap();
            map.put(e.getCode(), e.getChildren().stream().map(AdminMenuVO::getCode).collect(Collectors.toList()));
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AdminMenuVO> listMenu(PermissionTypeEnum typeEnum) {
        List<AdminMenuVO> resList = Lists.newArrayList();
        LambdaQueryWrapper<CateringPermissionEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringPermissionEntity::getType, typeEnum.getStatus())
                .eq(CateringPermissionEntity::getDel, DelEnum.NOT_DELETE.getFlag())
                .orderByDesc(CateringPermissionEntity::getSort);
        List<CateringPermissionEntity> list = permissionService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return resList;
        }
        List<AdminMenuVO> collect = list.stream().map(e -> {
            AdminMenuVO vo = ConvertUtils.sourceToTarget(e, AdminMenuVO.class);
            vo.setLabel(e.getMenuName());
            return vo;
        }).sorted(Comparator.comparing(AdminMenuVO::getSort)).collect(Collectors.toList());
        resList = TreeUtils.build(collect);
        return resList;
    }

    @Override
    public List<AdminMenuVO> rolePermission(Long id) {
        return null;
    }

    @Override
    public List<AdminMenuDTO> addMenu(List<AdminMenuDTO> menuList) {
        if (CollectionUtils.isEmpty(menuList)) {
            return null;
        }
        List<CateringPermissionEntity> list = ConvertUtils.sourceToTarget(menuList, CateringPermissionEntity.class);
        boolean batch = permissionService.saveOrUpdateBatch(list);
        if (batch) {
            return ConvertUtils.sourceToTarget(list, AdminMenuDTO.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRole(CommonRoleParamsDTO commonRoleParamsDTO, boolean needAddAppPermission) {
        this.verifyRoleName(commonRoleParamsDTO.getRoleName(), commonRoleParamsDTO.getSubjectId(), null);
        CateringRoleEntity cateringRoleEntity = ConvertUtils.sourceToTarget(commonRoleParamsDTO, CateringRoleEntity.class);
        long roleId = IdWorker.getId();
        cateringRoleEntity.setId(roleId);
        cateringRoleEntity.setDefaultFlag(IsDefaultEnum.NO_DEFAULT.getStatus());
        cateringRoleEntity.setIsDel(DelEnum.NOT_DELETE.getStatus());
        cateringRoleEntity.setRoleName(this.processRoleName(cateringRoleEntity.getRoleName()));
        this.save(cateringRoleEntity);
        if (needAddAppPermission) {
            //增加默认所有app权限
            this.addAllAppPermission(roleId);
        }
        return roleId;
    }

    /**
     * 增加角色时，默认添加所有app权限
     *
     * @param roleId
     */
    private void addAllAppPermission(Long roleId) {
        List<CateringPermissionEntity> appPermissionList = permissionService.getPermissionByType(PermissionTypeEnum.MERCHANT_APP.getStatus());

        List<CateringRolePermissionRelationEntity> rolePermissionRelationEntityList = appPermissionList.stream().map(cateringPermissionEntity -> {
            CateringRolePermissionRelationEntity cateringRolePermissionRelationEntity = new CateringRolePermissionRelationEntity();
            cateringRolePermissionRelationEntity.setId(IdWorker.getId());
            cateringRolePermissionRelationEntity.setRoleId(roleId);
            cateringRolePermissionRelationEntity.setPermissionId(cateringPermissionEntity.getId());
            return cateringRolePermissionRelationEntity;
        }).collect(Collectors.toList());
        cateringRolePermissionRelationService.addRolePermissionRelation(rolePermissionRelationEntityList);

    }

    @Override
    public PageData<CommonRoleVO> queryShopRolePageList(CommonRolePageListParamsDTO merchantRolePageListParamsDTO) {
        IPage<CommonRoleVO> commonRoleVOIPage = this.baseMapper.pageQueryRole(merchantRolePageListParamsDTO.getPage(), merchantRolePageListParamsDTO);
        List<CommonRoleVO> records = commonRoleVOIPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            // 去除父permission_id
            List<Long> roleIds = records.stream().map(CommonRoleVO::getId).collect(Collectors.toList());
            List<AdminRoleListVO> adminRoleListVOS = this.baseMapper.selectPermission(roleIds);
            if (CollectionUtils.isNotEmpty(adminRoleListVOS)) {
                Map<Long, AdminRoleListVO> adminRoleListVOMap = adminRoleListVOS.stream().collect(Collectors.toMap(AdminRoleListVO::getId, Function.identity()));
                records.forEach(commonRoleVO -> {
                    AdminRoleListVO adminRoleListVO = adminRoleListVOMap.get(commonRoleVO.getId());
                    if (adminRoleListVO == null) {
                        return;
                    }
                    List<Long> parentIds = adminRoleListVO.getParentList();
                    List<Long> permissionIds = adminRoleListVO.getPermissionList();
                    List<Long> newPermissionIds = permissionIds.stream().filter(permissionId -> !parentIds.contains(permissionId)).collect(Collectors.toList());
                    commonRoleVO.setPermissionIds(newPermissionIds);
                });
            }
            commonRoleVOIPage.setRecords(records);
        }
        return new PageData<>(commonRoleVOIPage);
    }

    @Override
    public Boolean updateRole(CommonRoleParamsDTO commonRoleParamsDTO, Long roleId) {
        this.verifyRoleName(commonRoleParamsDTO.getRoleName(), commonRoleParamsDTO.getSubjectId(), roleId);
        CateringRoleEntity cateringRoleEntity = new CateringRoleEntity();
        cateringRoleEntity.setId(roleId);
        String roleName = commonRoleParamsDTO.getRoleName();
        String processRoleName = processRoleName(roleName);
        cateringRoleEntity.setRoleName(processRoleName);
        return updateById(cateringRoleEntity);
    }

    /**
     * 橘色名称长度大于20时截取20的长度
     *
     * @param roleName
     * @return
     */
    private String processRoleName(String roleName) {
        if (roleName.length() > 20) {
            roleName = roleName.substring(0, 20);
        }
        return roleName;
    }

    @Override
    public Boolean delById(Long roleId) {

        CateringRoleEntity cateringRoleEntity = this.getById(roleId);
        if (IsDefaultEnum.DEFAULT.getStatus().equals(cateringRoleEntity.getDefaultFlag())) {
            throw new CustomException("默认角色不能删除！");
        }
        return this.removeById(roleId);
    }

    private void verifyRoleName(String roleName, Long subjectId, Long roleId) {
        if (StringUtils.isEmpty(roleName)) {
            throw new CustomException("角色名称不能为空！");
        }
        LambdaQueryWrapper<CateringRoleEntity> cateringRoleEntityLambdaQueryWrapper = new QueryWrapper<CateringRoleEntity>().lambda();
        cateringRoleEntityLambdaQueryWrapper.eq(CateringRoleEntity::getSubjectId, subjectId).
                eq(CateringRoleEntity::getRoleName, roleName);
        List<CateringRoleEntity> cateringRoleEntities = this.list(cateringRoleEntityLambdaQueryWrapper);

        if (CollectionUtils.isNotEmpty(cateringRoleEntities) && !cateringRoleEntities.get(0).getId().equals(roleId)) {
            throw new CustomException("该角色名称已经存在！");
        }
    }

    @Override
    public List<MerchantMenuVO> getMerchantAllPermissions() {
        return this.baseMapper.getMerchantAllPermissions();
    }

    @Override
    public List<MerchantMenuVO> getMerchantPermissionsAbsence(Long accountTypeId) {
        List<MerchantMenuVO> merchantAllPermissions = this.baseMapper.getMerchantAllPermissions();
        List<MerchantMenuVO> hasPermissions = this.baseMapper.getHasPermissions(accountTypeId);
        List<Long> permissionIds = hasPermissions.stream().map(MerchantMenuVO::getId).collect(Collectors.toList());
        // 所有id，包含父亲id
        List<MerchantMenuVO> allPermission = merchantAllPermissions.stream().
                filter(permission -> !permissionIds.contains(permission.getId())).collect(Collectors.toList());
//        // 去除父id，只保留叶子节点
        List<Long> parentIds = allPermission.stream().map(MerchantMenuVO::getParentId).collect(Collectors.toList());

        List<MerchantMenuVO> allPermissionNotContainParentIdList = merchantAllPermissions.stream().filter(permission -> !parentIds.contains(permission.getId())).collect(Collectors.toList());

        List<MerchantMenuVO> permissionAbsenceList = allPermission.stream().filter(permission -> !parentIds.contains(permission.getId())).collect(Collectors.toList());

        if (allPermissionNotContainParentIdList.size() == permissionAbsenceList.size()){
            throw new AppUnauthorizedException("无登录权限，请联系门店负责人");
        }
        return permissionAbsenceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addDefaultRole(Long shopId) {
        CateringRoleEntity cateringRoleEntity = new CateringRoleEntity();
        Long roleId = IdWorker.getId();
        cateringRoleEntity.setId(roleId);
        cateringRoleEntity.setRoleName("店员-默认");
        cateringRoleEntity.setSubjectId(shopId);
        cateringRoleEntity.setDefaultFlag(IsDefaultEnum.DEFAULT.getStatus());
        cateringRoleEntity.setIsDel(DelEnum.NOT_DELETE.getStatus());
        this.addDefaultRolePermission(roleId);
        return this.save(cateringRoleEntity);
    }

    /**
     * 默认角色的默认权限  checkPreorder sureOrder cancleOrder dealRefund
     *
     * @param roleId
     */
    private void addDefaultRolePermission(Long roleId) {
        List<CateringPermissionEntity> cateringPermissionEntityList = this.baseMapper.
                getPermissionByCodeList(Arrays.asList("checkPreorder", "sureOrder", "cancleOrder", "dealRefund"));
        if (CollectionUtils.isEmpty(cateringPermissionEntityList)) return;

        List<CateringRolePermissionRelationEntity> cateringRolePermissionRelationEntityList =
                cateringPermissionEntityList.stream().map(cateringPermissionEntity -> {
                    CateringRolePermissionRelationEntity cateringRolePermissionRelationEntity = new CateringRolePermissionRelationEntity();
                    cateringRolePermissionRelationEntity.setId(IdWorker.getId());
                    cateringRolePermissionRelationEntity.setRoleId(roleId);
                    cateringRolePermissionRelationEntity.setPermissionId(cateringPermissionEntity.getId());
                    return cateringRolePermissionRelationEntity;
                }).collect(Collectors.toList());
        cateringRolePermissionRelationService.addRolePermissionRelation(cateringRolePermissionRelationEntityList);
    }

    @Override
    public Boolean checkIfHasPermissionLogin(Long employeeId, Integer type) {
        Integer permissionCount = this.baseMapper.getPermissionCountByType(employeeId, type);
        if (permissionCount > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public List<BriefRoleVO> getRoleList(Long accountTypeId) {
        List<CateringRoleEntity> cateringRoleEntities = this.baseMapper.getRoleListByAccountTypeId(accountTypeId);

        return cateringRoleEntities.stream().map(cateringRoleEntity -> {
            BriefRoleVO briefRoleVO = new BriefRoleVO();
            briefRoleVO.setId(cateringRoleEntity.getId());
            briefRoleVO.setRoleName(cateringRoleEntity.getRoleName());
            return briefRoleVO;
        }).collect(Collectors.toList());
    }

    @Override
    public PermissionOfRoleVO getPermissionByRoleId(Long accountTypeId, Long roleId) {

        List<PermissionVO> permissionVOS = this.baseMapper.selectPermissionByRoleId(roleId);
        if (permissionVOS == null) return new PermissionOfRoleVO();
        PermissionOfRoleVO permissionOfRoleVO = new PermissionOfRoleVO();

        Map<Integer, List<PermissionVO>> typeToPermissionVOList = permissionVOS.stream().collect(Collectors.groupingBy(PermissionVO::getType));
        List<PermissionVO> pcPermissionList = typeToPermissionVOList.get(PermissionTypeEnum.MERCHANT_PC.getStatus());
        if (CollectionUtils.isNotEmpty(pcPermissionList)) {
            List<Long> pcPermissionIds = pcPermissionList.stream().map(PermissionVO::getPermissionId).collect(Collectors.toList());
            List<Long> pcParentPermissionIds = pcPermissionList.stream().map(PermissionVO::getParentId).collect(Collectors.toList());
            List<Long> targetPcPermissionId = pcPermissionIds.stream().filter(permissionId -> !pcParentPermissionIds.contains(permissionId)).collect(Collectors.toList());
            permissionOfRoleVO.setPcPermissionsIds(targetPcPermissionId);

        }
        List<PermissionVO> appPermissionList = typeToPermissionVOList.get(PermissionTypeEnum.MERCHANT_APP.getStatus());
        if (CollectionUtils.isNotEmpty(appPermissionList)) {
            List<Long> appPermissionIds = appPermissionList.stream().map(PermissionVO::getPermissionId).collect(Collectors.toList());
            List<Long> appParentPermissionIds = appPermissionList.stream().map(PermissionVO::getParentId).collect(Collectors.toList());
            List<Long> targetAppPermissionId = appPermissionIds.stream().filter(permissionId -> !appParentPermissionIds.contains(permissionId)).collect(Collectors.toList());
            permissionOfRoleVO.setAppPermissionIds(targetAppPermissionId);

        }

        return permissionOfRoleVO;
    }

    @Override
    public Integer selectByIds(Long shopId, List<Long> roleIds){
        return baseMapper.selectByIds(shopId,roleIds);
    }
}
