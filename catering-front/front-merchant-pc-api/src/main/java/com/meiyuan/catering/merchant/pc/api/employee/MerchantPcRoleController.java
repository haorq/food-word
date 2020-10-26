package com.meiyuan.catering.merchant.pc.api.employee;


import com.meiyuan.catering.admin.dto.admin.admin.CommonRolePageListParamsDTO;
import com.meiyuan.catering.admin.dto.admin.admin.CommonRoleParamsDTO;
import com.meiyuan.catering.admin.vo.admin.admin.BriefRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.CommonRoleVO;
import com.meiyuan.catering.admin.vo.admin.admin.PermissionOfRoleVO;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.pc.service.employee.PcMerchantRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限相关controller
 */
@RestController
@Api(tags = "员工-角色管理")
@RequestMapping("/employee")
public class MerchantPcRoleController {


    @Autowired
    private PcMerchantRoleService pcMerchantRoleService;


    @ApiOperation("角色列表-分页查询角色")
    @PostMapping("/role")
    public Result<PageData<CommonRoleVO>> getRolePageList(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO,
                                                          @RequestBody CommonRolePageListParamsDTO merchantRolePageListParamsDTO) {
        return pcMerchantRoleService.queryPageList(merchantAccountDTO, merchantRolePageListParamsDTO);
    }

    @ApiOperation("角色列表-根据角色id查询对应的权限id集合")
    @GetMapping("/permission/role/{roleId}")
    public Result<PermissionOfRoleVO> getPermissionByRoleId(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO,
                                                            @PathVariable("roleId") Long roleId) {
        return pcMerchantRoleService.getPermissionByRoleId(merchantAccountDTO, roleId);
    }

    @ApiOperation("/角色列表-添加员工时")
    @GetMapping("/create_emp/role")
    public Result<List<BriefRoleVO>> getRoleList(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO) {

        return pcMerchantRoleService.getRoleList(merchantAccountDTO);
    }

    @ApiOperation("角色列表-新增角色")
    @PostMapping("/role/create")
    public Result<Long> addRole(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO
            , @RequestBody CommonRoleParamsDTO commonRoleParamsDTO) {

        return pcMerchantRoleService.addRole(merchantAccountDTO, commonRoleParamsDTO);
    }

    @ApiOperation("角色列表-修改角色")
    @PutMapping("/role/{roleId}")
    public Result<Object> updateRole(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO
            , @RequestBody CommonRoleParamsDTO commonRoleParamsDTO, @PathVariable("roleId") Long roleId) {

        return pcMerchantRoleService.updateRole(merchantAccountDTO, commonRoleParamsDTO, roleId);
    }

    @ApiOperation("角色列表-删除角色")
    @DeleteMapping("/role/{roleId}")
    public Result<Object> deleteRole(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO,
                                     @PathVariable("roleId") Long roleId) {

        return pcMerchantRoleService.delRoleById(merchantAccountDTO, roleId);
    }

    @ApiOperation("角色列表-复制角色")
    @PostMapping("/role/copy/{roleId}")
    public Result<Object> copyRole(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO,
                                   @PathVariable("roleId") Long roleId) {

        return pcMerchantRoleService.copyRole(merchantAccountDTO, roleId);
    }


}



























