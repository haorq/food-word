package com.meiyuan.catering.admin.web.admin;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.dto.role.AdminMenuDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleAuthDTO;
import com.meiyuan.catering.admin.dto.role.AdminRoleDTO;
import com.meiyuan.catering.admin.dto.role.AdminRolePageDTO;
import com.meiyuan.catering.admin.service.admin.AdminRoleService;
import com.meiyuan.catering.admin.vo.role.AdminMenuVO;
import com.meiyuan.catering.admin.vo.role.AdminRoleListVO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName AdminSystemSettingController
 * @Description 系统设置
 * @Author gz
 * @Date 2020/9/28 9:50
 * @Version 1.5.0
 */
@Api(tags = "系统设置-角色管理")
@RestController
@RequestMapping(value = "admin/role")
public class AdminRoleController {
    @Autowired
    private AdminRoleService roleService;


    @ApiOperation(value = "角色管理-角色列表 v1.5.0",notes = "角色管理-角色列表")
    @PostMapping(value = "page")
    public Result<PageData<AdminRoleListVO>> pageRole(@RequestBody AdminRolePageDTO dto){
        return roleService.pageRole(dto);
    }

    @LogOperation("角色管理-新增/编辑")
    @ApiOperation(value = "角色管理-新增/编辑 v1.5.0",notes = "角色管理-新增")
    @PostMapping(value = "saveOrEdit")
    public Result saveOrEdit(@Valid @RequestBody AdminRoleDTO dto){
        return roleService.saveOrEdit(dto);
    }

    @LogOperation("角色管理-删除")
    @ApiOperation(value = "角色管理-删除 v1.5.0",notes = "角色管理-删除")
    @GetMapping(value = "delete/{id}")
    public Result delete(@PathVariable(value = "id") Long id){
        return roleService.delete(id);
    }

    @LogOperation("角色管理-授权")
    @ApiOperation(value = "角色管理-授权 v1.5.0",notes = "角色管理-授权")
    @PostMapping(value = "authorization")
    public Result authorization(@Valid @RequestBody AdminRoleAuthDTO dto){
        return roleService.authorization(dto);
    }

    @ApiOperation(value = "角色管理-菜单详情 v1.5.0",notes = "角色管理-菜单详情")
    @GetMapping(value = "listMenu")
    public Result<List<AdminMenuVO>> listMenu(){
        return roleService.listMenu();
    }

    @ApiOperation(value = "角色管理-角色权限详情 v1.5.0",notes = "角色管理-角色权限详情")
    @GetMapping(value = "permission/{roleId}")
    public Result<List<AdminMenuVO>> rolePermission(@PathVariable(value = "roleId") Long id){
        return roleService.rolePermission(id);
    }

    @ApiOperation(value = "新增菜单",notes = "新增菜单")
    @PostMapping(value = "addMenu")
    public Result<List<AdminMenuDTO>> addMenu(@RequestBody List<AdminMenuDTO> menuList){
        return roleService.addMenu(menuList);
    }

}
