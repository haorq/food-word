package com.meiyuan.catering.admin.fegin;

import com.meiyuan.catering.admin.dto.admin.admin.CommonRolePageListParamsDTO;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRoleParamsDTO;
import com.meiyuan.catering.admin.dto.role.AdminMenuDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleAuthDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleDTO;
import com.meiyuan.catering.admin.dto.role.AdminRolePageDTO;
import com.meiyuan.catering.admin.entity.CateringRoleEntity;
import com.meiyuan.catering.admin.enums.base.PermissionTypeEnum;
import com.meiyuan.catering.admin.service.CateringRoleService;
import com.meiyuan.catering.admin.vo.admin.admin.BriefRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.CommonRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.PermissionOfRoleVO;
import com.meiyuan.catering.admin.vo.role.AdminMenuVO;
import com.meiyuan.catering.admin.vo.role.AdminRoleListVO;
import com.meiyuan.catering.admin.vo.role.MerchantMenuVO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleClient
 * @Description
 * @Author gz
 * @Date 2020/9/29 9:38
 * @Version 1.5.0
 */
@Service
public class RoleClient {

    @Autowired
    private CateringRoleService roleService;

    public Result<PageData<AdminRoleListVO>> pageRole(AdminRolePageDTO dto) {
        return Result.succ(roleService.pageRole(dto));
    }

    public Result saveOrEdit(AdminRoleDTO dto) {
        return Result.succ(roleService.saveOrEdit(dto));
    }

    public Result delete(Long id) {
        return Result.succ(roleService.delete(id));
    }

    public Result authorization(AdminRoleAuthDTO dto) {
        return Result.succ(roleService.authorization(dto));
    }


    public Result<List<AdminMenuVO>> listMenu(PermissionTypeEnum typeEnum) {
        return Result.succ(roleService.listMenu(typeEnum));
    }

    public Result<List<AdminMenuVO>> rolePermission(Long id) {
        return Result.succ(roleService.rolePermission(id));
    }

    public List<Map<String, Object>> loginAccountPermission(Long accountId, boolean isPlatform) {
        return roleService.loginAccountPermission(accountId, isPlatform);
    }

    public Result<List<AdminMenuDTO>> addMenu(List<AdminMenuDTO> menuList) {
        return Result.succ(roleService.addMenu(menuList));
    }

    public Result<PageData<CommonRoleVO>> queryShopRolePageList(CommonRolePageListParamsDTO merchantRolePageListParamsDTO) {
        return Result.succ(roleService.queryShopRolePageList(merchantRolePageListParamsDTO));
    }

    /**
     *
     * @param commonRoleParamsDTO
     * @param needAddAppPermission 是否增加app默认所有权限
     * @return
     */
    public Result<Long> addRole(CommonRoleParamsDTO commonRoleParamsDTO, boolean needAddAppPermission) {
        return Result.succ(roleService.addRole(commonRoleParamsDTO, needAddAppPermission));
    }

    public Result<Object> updateRole(CommonRoleParamsDTO commonRoleParamsDTO, Long roleId) {
        return Result.succ(roleService.updateRole(commonRoleParamsDTO, roleId));
    }

    public Result<Object> deleteById(Long roleId) {
        return Result.succ(roleService.delById(roleId));
    }

    public CateringRoleEntity getRoleById(Long roleId) {
        return roleService.getById(roleId);
    }

    public List<Map<String, Object>> getAllPermission(List<Integer> typeList) {
        return roleService.getAllPermissionByType(typeList);
    }

    public Result<List<MerchantMenuVO>> getMerchantAllPermissions() {
        return Result.succ(roleService.getMerchantAllPermissions());
    }

    public Result<List<MerchantMenuVO>> getMerchantPermissionsAbsence(Long accountTypeId) {

        return Result.succ(roleService.getMerchantPermissionsAbsence(accountTypeId));
    }

    /**
     * 插入默认角色
     *
     * @param shopId 店铺id
     * @return
     */
    public Result<Boolean> insertDefaultRole(Long shopId) {
        return Result.succ(roleService.addDefaultRole(shopId));
    }

    /**
     * 判断是否员工能否登录
     *
     * @param employeeId 员工id
     * @param type       类型 0：商户后台，1：app
     * @return true 可以登录，false不能登录
     */
    public Result<Boolean> checkIfHasPermissionLogin(Long employeeId, Integer type) {
        return Result.succ(roleService.checkIfHasPermissionLogin(employeeId, type));
    }

    /**
     * 获取员工列表，只返回roleName，id
     *
     * @param accountTypeId
     * @return
     */
    public Result<List<BriefRoleVO>> getRoleList(Long accountTypeId) {
        return Result.succ(roleService.getRoleList(accountTypeId));
    }

    /**
     * 根据角色id获取app端和商户端角色id集合
     *
     * @param accountTypeId
     * @param roleId
     * @return
     */
    public Result<PermissionOfRoleVO> getPermissionByRoleId(Long accountTypeId, Long roleId) {
        return Result.succ(roleService.getPermissionByRoleId(accountTypeId, roleId));
    }

    /**
     * describe:
     * @author: fql
     * @date: 2020/10/14 14:12
     * @param shopId 门店id
     * @param roleIds 角色id
     * @return: {@link Integer}
     * @version 1.5.0
     **/
    public Integer selectByIds(Long shopId, List<Long> roleIds){
        return roleService.selectByIds(shopId,roleIds);
    }
}
