package com.meiyuan.catering.admin.service.admin;

import com.meiyuan.catering.admin.dto.role.AdminMenuDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleAuthDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleDTO;
import com.meiyuan.catering.admin.dto.role.AdminRolePageDTO;
import com.meiyuan.catering.admin.enums.base.PermissionTypeEnum;
import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.admin.vo.role.AdminMenuVO;
import com.meiyuan.catering.admin.vo.role.AdminRoleListVO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName AdminRoleService
 * @Description
 * @Author gz
 * @Date 2020/9/29 9:29
 * @Version 1.5.0
 */
@Service
public class AdminRoleService {
    @Autowired
    private RoleClient roleClient;


    /**
     * 方法描述: 角色管理-角色列表 <br>
     *
     * @author: gz
     * @date: 2020/9/29 9:30
     * @param dto
     * @return: {@link Result< PageData< AdminRoleListVO>>}
     * @version 1.5.0
     **/
    public Result<PageData<AdminRoleListVO>> pageRole(AdminRolePageDTO dto){
        return roleClient.pageRole(dto);
    }
    /**
     * 方法描述: 角色管理-新增<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:31
     * @param dto
     * @return: {@link Result}
     * @version 1.5.0
     **/
    public Result saveOrEdit(AdminRoleDTO dto){
        return roleClient.saveOrEdit(dto);
    }
    /**
     * 方法描述: 角色管理-删除<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:31
     * @param id
     * @return: {@link Result}
     * @version 1.5.0
     **/
    public Result delete(Long id){
        return roleClient.delete(id);
    }
    /**
     * 方法描述: 角色管理-授权<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:31
     * @param dto
     * @return: {@link Result}
     * @version 1.5.0
     **/
    public Result authorization(AdminRoleAuthDTO dto){
        return roleClient.authorization(dto);
    }

    /**
     * 方法描述: 角色管理-菜单详情<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:31
     * @param
     * @return: {@link Result< List< AdminMenuVO>>}
     * @version 1.5.0
     **/
    public Result<List<AdminMenuVO>> listMenu(){
        return roleClient.listMenu(PermissionTypeEnum.PLAT_FROM);
    }
    /**
     * 方法描述: 角色管理-角色权限详情<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:32
     * @param id
     * @return: {@link Result< List< AdminMenuVO>>}
     * @version 1.5.0
     **/
    public Result<List<AdminMenuVO>> rolePermission(Long id){
        return roleClient.rolePermission(id);
    }
    /**
     * 方法描述: 新增菜单<br>
     *
     * @author: gz
     * @date: 2020/9/29 9:32
     * @param menuList
     * @return: {@link Result< AdminMenuDTO>}
     * @version 1.5.0
     **/
    public Result<List<AdminMenuDTO>> addMenu(List<AdminMenuDTO> menuList){
        return roleClient.addMenu(menuList);
    }
}
