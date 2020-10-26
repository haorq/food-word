package com.meiyuan.catering.merchant.pc.api.employee;

import com.meiyuan.catering.admin.dto.admin.admin.CommonPermissionSaveParamsDTO;
import com.meiyuan.catering.admin.vo.role.AdminMenuVO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.pc.service.employee.PcMerchantPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 权限相关Controller
 */
@RestController
@Api(tags = "员工-权限")
@RequestMapping("/employee")
public class MerchantPcPermissionController {

    @Autowired
    private PcMerchantPermissionService pcMerchantPermissionService;


    @ApiOperation("根据类型获取权限树")
    @PostMapping("/permission/type/{type}")
    public Result<List<AdminMenuVO>> getPermissionsByRoleId(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO,
                                                            @ApiParam("权限类型，0:商户pc，1：app") @PathVariable("type") Integer type) {

        return pcMerchantPermissionService.getPermissionByRoleId(merchantAccountDTO, type);
    }

    @ApiOperation("保存权限")
    @PostMapping("/permission/{roleId}")
    public Result<Object> savePermission(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO,
                                         @PathVariable("roleId") Long roleId, @RequestBody List<CommonPermissionSaveParamsDTO> list) {

        return pcMerchantPermissionService.savePermission(roleId, list);
    }


}
