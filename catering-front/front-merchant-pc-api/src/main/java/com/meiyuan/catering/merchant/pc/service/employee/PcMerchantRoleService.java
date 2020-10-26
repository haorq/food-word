package com.meiyuan.catering.merchant.pc.service.employee;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRolePageListParamsDTO;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRoleParamsDTO;
import com.meiyuan.catering.admin.entity.CateringRoleEntity;
import com.meiyuan.catering.admin.entity.CateringRolePermissionRelationEntity;
import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.admin.fegin.RolePermissionRelationClient;
import com.meiyuan.catering.admin.fegin.SubjectRoleRelationClient;
import com.meiyuan.catering.admin.vo.admin.admin.BriefRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.CommonRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.PermissionOfRoleVO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.AssertUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import com.meiyuan.catering.merchant.feign.ShopClient;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PcMerchantRoleService {


    @Autowired
    private RoleClient roleClient;

    @Autowired
    private RolePermissionRelationClient rolePermissionRelationClient;

    @Autowired
    private SubjectRoleRelationClient subjectRoleRelationClient;

    @Autowired
    private ShopClient shopClient;

    public Result<PageData<CommonRoleVO>> queryPageList(MerchantAccountDTO merchantAccountDTO, CommonRolePageListParamsDTO merchantRolePageListParamsDTO) {
        Long accountTypeId = merchantAccountDTO.getAccountTypeId();

        merchantRolePageListParamsDTO.setShopId(this.getShopId(accountTypeId));

        return roleClient.queryShopRolePageList(merchantRolePageListParamsDTO);
    }

    private Long getShopId(Long accountTypeId) {
        CateringShopEmployeeEntity cateringShopEmployeeEntity = shopClient.getEmployeeById(accountTypeId);
        if (cateringShopEmployeeEntity != null) {
            return cateringShopEmployeeEntity.getShopId();
        }
        return accountTypeId;
    }

    /**
     * 添加角色
     *
     * @param merchantAccountDTO
     * @param commonRoleParamsDTO
     * @return
     */
    public Result<Long> addRole(MerchantAccountDTO merchantAccountDTO, CommonRoleParamsDTO commonRoleParamsDTO) {

        Long accountTypeId = merchantAccountDTO.getAccountTypeId();
        commonRoleParamsDTO.setSubjectId(this.getShopId(accountTypeId));
        return roleClient.addRole(commonRoleParamsDTO, true);
    }

    /**
     * 更新角色
     *
     * @param merchantAccountDTO
     * @param commonRoleParamsDTO
     * @param roleId
     * @return
     */
    public Result<Object> updateRole(MerchantAccountDTO merchantAccountDTO, CommonRoleParamsDTO commonRoleParamsDTO, Long roleId) {
        Long accountTypeId = merchantAccountDTO.getAccountTypeId();
        commonRoleParamsDTO.setSubjectId(this.getShopId(accountTypeId));
        return roleClient.updateRole(commonRoleParamsDTO, roleId);
    }


    /**
     * 删除角色 有关联用户时不能删除
     *
     * @param merchantAccountDTO
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> delRoleById(MerchantAccountDTO merchantAccountDTO, Long roleId) {
        subjectRoleRelationClient.verifyHasUserOfRole(roleId);
        rolePermissionRelationClient.delRolePermissionRelationByRoleId(roleId);
        return roleClient.deleteById(roleId);
    }

    /**
     * 复制角色
     *
     * @param merchantAccountDTO
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> copyRole(MerchantAccountDTO merchantAccountDTO, Long roleId) {
        CateringRoleEntity cateringRoleEntity = roleClient.getRoleById(roleId);
        // 新增一个角色，名为：原名-copy
        CommonRoleParamsDTO commonRoleParamsDTO = new CommonRoleParamsDTO();
        String roleName = cateringRoleEntity.getRoleName();
        // 处理名字
        String copyRoleName = roleName + "-copy";
        if (copyRoleName.length() > 20) {
            copyRoleName = copyRoleName.substring(0, 20);
            String suffix = copyRoleName.substring(roleName.length());
            AssertUtil.isTrue(!copyRoleName.equals(roleName) && !"-copy".contains(suffix), "复制失败！复制角色名称与已有角色名称重复");
        }

        commonRoleParamsDTO.setRoleName(copyRoleName);
        Long accountTypeId = merchantAccountDTO.getAccountTypeId();
        commonRoleParamsDTO.setSubjectId(this.getShopId(accountTypeId));
        Result<Long> result = roleClient.addRole(commonRoleParamsDTO, false);
        Long newRoleId = result.getData();
        // 插入角色权限中间表
        List<CateringRolePermissionRelationEntity> cateringRolePermissionRelationEntities = rolePermissionRelationClient.getPermissionsByRoleId(roleId);
        if (CollectionUtils.isEmpty(cateringRolePermissionRelationEntities)) {
            return Result.succ();
        }
        List<CateringRolePermissionRelationEntity> addCateringRolePermissionRelationEntityList = new ArrayList<>();
        for (CateringRolePermissionRelationEntity cateringRolePermissionRelationEntity : cateringRolePermissionRelationEntities) {
            CateringRolePermissionRelationEntity addRolePermissionRelationEntity =
                    new CateringRolePermissionRelationEntity(IdWorker.getId(), newRoleId, cateringRolePermissionRelationEntity.getPermissionId());
            addCateringRolePermissionRelationEntityList.add(addRolePermissionRelationEntity);
        }
        return rolePermissionRelationClient.addRolePermissionRelation(addCateringRolePermissionRelationEntityList);
    }

    public Result<List<BriefRoleVO>> getRoleList(MerchantAccountDTO merchantAccountDTO) {
        Long accountTypeId = merchantAccountDTO.getAccountTypeId();
        return roleClient.getRoleList(this.getShopId(accountTypeId));
    }

    public Result<PermissionOfRoleVO> getPermissionByRoleId(MerchantAccountDTO merchantAccountDTO, Long roleId) {
        return roleClient.getPermissionByRoleId(merchantAccountDTO.getAccountTypeId(), roleId);
    }
}





























