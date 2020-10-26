package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRolePageListParamsDTO;
import com.meiyuan.catering.admin.dto.role.AdminRolePageDTO;
import com.meiyuan.catering.admin.entity.CateringPermissionEntity;
import com.meiyuan.catering.admin.entity.CateringRoleEntity;
import com.meiyuan.catering.admin.entity.CateringRolePermissionRelationEntity;
import com.meiyuan.catering.admin.vo.admin.admin.CommonRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.PermissionVO;
import com.meiyuan.catering.admin.vo.role.AdminMenuVO;
import com.meiyuan.catering.admin.vo.role.AdminRoleListVO;
import com.meiyuan.catering.admin.vo.role.MerchantMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CateringRoleMapper
 * @Description
 * @Author gz
 * @Date 2020/9/29 10:12
 * @Version 1.5.0
 */
@Mapper
public interface CateringRoleMapper extends BaseMapper<CateringRoleEntity> {
    /**
     * 方法描述: 角色分页 <br>
     *
     * @author: gz
     * @date: 2020/9/29 14:36
     * @param page
     * @param dto
     * @return: {@link IPage< AdminRoleListVO>}
     * @version 1.5.0
     **/
    IPage<AdminRoleListVO> pageRole(Page page, @Param("dto") AdminRolePageDTO dto);

    /**
     * 通过ID删除角色
     * @param id
     * @return
     */
    int removeById(Long id);

    /**
     * 新增角色权限关联数据
     * @param collect
     * @return
     */
    int insertRolePermission(@Param("list") List<CateringRolePermissionRelationEntity> collect);

    /**
     * 移除角色权限
     * @param roleId
     * @return
     */
    int removePermission(Long roleId);

    /**
     * 通过角色IDS获取关联的权限数据
     * @param collect
     * @return
     */
    List<AdminRoleListVO> selectPermission(@Param("list") List<Long> collect);

    /**
     * 商户端角色列表
     * @param page
     * @param CommonRolePageListParamsDTO
     * @return
     */
    IPage<CommonRoleVO> pageQueryRole(Page page, @Param("dto") CommonRolePageListParamsDTO CommonRolePageListParamsDTO);

    /**
     * 通过登录账号ID获取关联的权限信息
     * @param accountId
     * @return
     */
    List<AdminMenuVO> selectAccountPermission(Long accountId);

    List<AdminMenuVO> getAllPermissionByType(@Param("list") List<Integer> typeList);

    /**
     * app获取权限列表
     * @return
     */
    List<MerchantMenuVO> getMerchantAllPermissions();

    /**
     * 获取登录账户拥有的权限
     * @param accountTypeId
     * @return
     */
    List<MerchantMenuVO> getHasPermissions(@Param("accountTypeId") Long accountTypeId);

    /**
     * 根据员工id和type(0商户pc，1：app端)获取权限数
     * @param employeeId
     * @param type
     * @return
     */
    Integer getPermissionCountByType(@Param("employeeId") Long employeeId,@Param("type") Integer type);

    /**
     * 根据accountTypeId获取角色列表
     * @param accountTypeId
     * @return
     */
    List<CateringRoleEntity> getRoleListByAccountTypeId(@Param("accountTypeId") Long accountTypeId);

    /**
     *  根据角色id获取权限列表
     * @param roleId
     * @return
     */
    List<PermissionVO> selectPermissionByRoleId(Long roleId);

    /**
     * 根据权限code获取权限列表
     * @param asList
     * @return
     */
    List<CateringPermissionEntity> getPermissionByCodeList(@Param("list") List<String> asList);

    /**
     * describe: 查询角色集合
     * @author: fql
     * @date: 2020/10/14 12:36
     * @param roleIds 角色id集合
     * @param shopId 门店id
     * @return: {@link List<CateringRoleEntity>}
     * @version 1.5.0
     **/
    Integer selectByIds(@Param("shopId") Long shopId, @Param("ids") List<Long> roleIds);
}
