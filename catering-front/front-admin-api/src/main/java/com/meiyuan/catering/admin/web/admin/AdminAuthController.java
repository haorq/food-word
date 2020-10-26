package com.meiyuan.catering.admin.web.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.dto.admin.admin.AdminAddDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminPwdUpdateDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminQueryDTO;
import com.meiyuan.catering.admin.dto.admin.admin.AdminUpdateDTO;
import com.meiyuan.catering.admin.dto.admin.auth.UserLoginDto;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.service.admin.AdminAuthService;
import com.meiyuan.catering.admin.utils.AdminUtils;
import com.meiyuan.catering.admin.vo.admin.admin.AdminDetailsVo;
import com.meiyuan.catering.admin.vo.admin.admin.AdminListQueryVo;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lhm
 * @date 2020/3/17 11:11
 **/
@RestController
@RequestMapping("/admin/admin")
@Api(tags = "管理员相关接口")
public class AdminAuthController {
    @Resource
    private AdminAuthService adminAuthService;

    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public Result login(@Valid @RequestBody UserLoginDto vo) {
        return adminAuthService.login(vo);
    }


    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result info(@RequestAttribute("info") CateringAdmin admin) {
        Long adminId = admin.getId();
        Map<String, Object> data = new HashMap<>(4);
        data.put("id", adminId.toString());
        data.put("name", admin.getUsername());
        data.put("avatar", admin.getAvatar());
        if(AdminUtils.SUPER_ADMIN_ID.equals(adminId)){
            data.put("perms", Arrays.asList("*"));
        }else {
            data.put("perms",adminAuthService.loginAccountPermission(adminId));
        }
        return Result.succ(data);
    }


    @ApiOperation("添加管理员")
    @PostMapping("/create")
    @LogOperation("添加管理员")
    public Result<Object> create(@RequestBody AdminAddDTO dto) {
        return adminAuthService.create(dto);
    }


    @ApiOperation("操作员列表查询")
    @PostMapping("/querySelective")
    public Result<IPage<AdminListQueryVo>> querySelective(@RequestBody AdminQueryDTO dto) {
        return adminAuthService.querySelective(dto);
    }


    @ApiOperation("管理员编辑")
    @PostMapping("/updateAdmin")
    @LogOperation("管理员编辑")
    public Result<Integer> updateAdmin(@RequestBody AdminUpdateDTO dto) {
        return adminAuthService.updateAdmin(dto);
    }

    @ApiOperation("管理员详情")
    @GetMapping("/details")
    public Result<AdminDetailsVo> details(@RequestParam Long id) {
        return adminAuthService.details(id);
    }


    @ApiOperation("管理员--修改密码")
    @PostMapping("/updatePassword")
    @LogOperation("管理员--修改密码")
    public Result<Boolean> updatePassword(@RequestAttribute("info") CateringAdmin admin,@RequestBody AdminPwdUpdateDTO dto) {
        return adminAuthService.updatePassword(admin,dto);
    }


    @ApiOperation("发送验证码")
    @GetMapping("/sendCode")
    public Result sendCode(@RequestParam String phone) {
        return adminAuthService.sendCode(phone);
    }

    @ApiOperation("管理员--忘记密码")
    @PostMapping("/forgetPassword")
    public Result<Boolean> forgetPassword(@RequestBody AdminPwdUpdateDTO dto) {
        return adminAuthService.forgetPassword(dto);
    }


    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result logout() {
        return Result.succ();
    }
}
