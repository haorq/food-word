package com.meiyuan.catering.admin.web.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.user.AdminUserService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.user.UserCompanyAddDTO;
import com.meiyuan.catering.user.dto.user.UserCompanyExitDTO;
import com.meiyuan.catering.user.query.user.RecycleQueryDTO;
import com.meiyuan.catering.user.query.user.UserCompanyQueryDTO;
import com.meiyuan.catering.user.query.user.UserQueryDTO;
import com.meiyuan.catering.user.vo.user.CompanyDetailVo;
import com.meiyuan.catering.user.vo.user.RecycleListVo;
import com.meiyuan.catering.user.vo.user.UserCompanyListVo;
import com.meiyuan.catering.user.vo.user.UserListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/3/19 10:45
 **/
@RestController
@RequestMapping("/admin/user")
@Api(tags = "用户管理相关接口")
public class AdminUserController {

    @Resource
    private AdminUserService adminUserService;

    @ApiOperation("用户管理--企业用户列表")
    @PostMapping("/companyList")
    public Result<IPage<UserCompanyListVo>> companyList(@RequestBody UserCompanyQueryDTO dto) {
        return adminUserService.companyList(dto);
    }

    @ApiOperation("用户管理--企业用户详情")
    @GetMapping("/companyDetailById/{id}")
    public Result<CompanyDetailVo> companyDetailById(@PathVariable String id) {
        return adminUserService.companyDetailById(id);
    }

    @LogOperation(value = "用户管理--添加企业用户")
    @ApiOperation("用户管理--添加企业用户")
    @PostMapping("/companyAdd")
    public Result companyAdd(@RequestBody UserCompanyAddDTO dto) {
        return adminUserService.companyAdd(dto);
    }

    @LogOperation(value = "用户管理--企业用户删除--禁用--编辑")
    @ApiOperation("用户管理--企业用户删除--禁用--编辑")
    @PostMapping("/updateUserCompany")
    public Result updateUserCompany(@RequestBody UserCompanyExitDTO dto) {
        return adminUserService.updateUserCompany(dto);
    }

    @ApiOperation("用户管理--个人用户列表")
    @PostMapping("/userList")
    public Result<IPage<UserListVo>> userList(@RequestBody UserQueryDTO dto) {
        return adminUserService.userList(dto);
    }


    @ApiOperation("用户管理--回收站")
    @PostMapping("recycleList")
    public Result<IPage<RecycleListVo>> recycleList(@RequestBody RecycleQueryDTO dto) {
        return adminUserService.recycleList(dto);
    }
}
