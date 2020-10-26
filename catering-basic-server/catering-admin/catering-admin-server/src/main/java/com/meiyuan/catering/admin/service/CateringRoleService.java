package com.meiyuan.catering.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRolePageListParamsDTO;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRoleParamsDTO;
import com.meiyuan.catering.admin.dto.role.AdminMenuDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleAuthDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleDTO;
import com.meiyuan.catering.admin.dto.role.AdminRolePageDTO;
import com.meiyuan.catering.admin.entity.CateringRoleEntity;
import com.meiyuan.catering.admin.enums.base.PermissionTypeEnum;
import com.meiyuan.catering.admin.vo.admin.admin.BriefRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.CommonRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.PermissionOfRoleVO;
import com.meiyuan.catering.admin.vo.role.AdminMenuVO;
import com.meiyuan.catering.admin.vo.role.AdminRoleListVO;
import com.meiyuan.catering.admin.vo.role.MerchantMenuVO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleClient
 * @Description
 * @Author gz
 * @Date 2020/9/29 9:38
 * @Version 1.5.0
 */
public interface CateringRoleService extends IService<CateringRoleEntity> {

    /**
     * 方法描述: 角色管理-角色列表 <br>
     *
     * @author: gz
     * @date: 2020/9/29 9:30
     * @param dto
     * @return: {@link Result < PageData< AdminRoleListVO>>}
     * @version 1.5.0
     **/
    PageData<AdminRoleListVO> pageRole(AdminRolePageDTO dto);
    /**
     * 方法描述: 角色管理-新增<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:31
     * @param dto
     * @return: {@link Result}
     * @version 1.5.0
     **/
    Boolean saveOrEdit(AdminRoleDTO dto);
    /**
     * 方法描述: 角色管理-删除<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:31
     * @param id
     * @return: {@link Result}
     * @version 1.5.0
     **/
    Boolean delete(Long id);
    /**
     * 方法描述: 角色管理-授权<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:31
     * @param dto
     * @return: {@link Result}
     * @version 1.5.0
     **/
    Boolean authorization(AdminRoleAuthDTO dto);


    /**
     * 方法描述: 通过登录账号ID获取权限<br>
     *
     * @author: gz
     * @date: 2020/9/29 17:45
     * @param accountId
     * @param isPlatform 是否平台权限
     * @return: {@link List< Map< String, Object>>}
     * @version 1.5.0
     **/
    List<Map<String,Object>> loginAccountPermission(Long accountId,boolean isPlatform);
    /**
     * 方法描述: 角色管理-菜单详情<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:31
     * @param typeEnum
     * @return: {@link Result< List< AdminMenuVO>>}
     * @version 1.5.0
     **/
    List<AdminMenuVO> listMenu(PermissionTypeEnum typeEnum);
    /**
     * 方法描述: 角色管理-角色权限详情<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:32
     * @param id
     * @return: {@link Result< List< AdminMenuVO>>}
     * @version 1.5.0
     **/
    List<AdminMenuVO> rolePermission(Long id);
    /**
     * 方法描述: 新增菜单<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:32
     * @param menuList
     * @return: {@link Result< AdminMenuDTO>}
     * @version 1.5.0
     **/
    List<AdminMenuDTO> addMenu(List<AdminMenuDTO> menuList);

    /**
     * 增加角色
     * @param commonRoleParamsDTO
     * @param needAddAppPermission 是否需要增加app权限
     * @return
     */
    Long addRole(CommonRoleParamsDTO commonRoleParamsDTO,boolean needAddAppPermission);

    /**
     * 修改角色
     * @param commonRoleParamsDTO
     * @param roleId
     * @return
     */
    Boolean updateRole(CommonRoleParamsDTO commonRoleParamsDTO, Long roleId);

    /**
     * 根据i删除角色
     * @param roleId
     * @return
     */
    Boolean delById(Long roleId);

    /**
     * 分页查询
     * @param merchantRolePageListParamsDTO
     * @return
     */
    PageData<CommonRoleVO> queryShopRolePageList(CommonRolePageListParamsDTO merchantRolePageListParamsDTO);

    /**
     * 获取根据账号获取全部权限
     * @param typeList type集合
     * @return
     */
    List<Map<String, Object>> getAllPermissionByType(List<Integer> typeList);

    /** app获取所有权限
     *
     * @return
     */
    List<MerchantMenuVO> getMerchantAllPermissions();

    /**
     * app获取登录用户没有的权限
     * @param accountTypeId
     * @return
     */
    List<MerchantMenuVO> getMerchantPermissionsAbsence(Long accountTypeId);

    /**
     * 店铺添加默认角色
     * @param shopId
     * @return
     */
    Boolean addDefaultRole(Long shopId);


    /**
     * 判断员工能否登录相应的平台
     * @param employeeId
     * @param type
     * @return
     */
    Boolean checkIfHasPermissionLogin(Long employeeId, Integer type);


    /**
     * 橘色列表不分页
     * @param accountTypeId
     * @return
     */
    List<BriefRoleVO> getRoleList(Long accountTypeId);

    PermissionOfRoleVO getPermissionByRoleId(Long accountTypeId, Long roleId);

    /**
     * describe:
     * @author: fql
     * @date: 2020/10/14 14:10
     * @param  shopId 门店id
     * @return: {@link }
     * @version 1.5.0
     **/
    Integer selectByIds(Long shopId, List<Long> roleIds);
}
