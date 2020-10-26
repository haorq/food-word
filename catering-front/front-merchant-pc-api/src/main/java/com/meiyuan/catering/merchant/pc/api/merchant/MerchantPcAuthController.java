package com.meiyuan.catering.merchant.pc.api.merchant;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountInfoDTO;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountPwdDTO;
import com.meiyuan.catering.merchant.dto.auth.MerchantPcUserDTO;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.pc.service.merchant.MerchantPcAuthService;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @Author MeiTao
 * @Description 商户pc端-登录相关接口
 * @Date 2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "商户后台-登录接口")
public class MerchantPcAuthController {
    @Resource
    MerchantPcAuthService authService;
    @Resource
    MerchantUtils merchantUtils;

    @PostMapping("/login")
    @ApiOperation("商户pc端登录")
    public Result<String> login(@Valid @RequestBody MerchantPcUserDTO dto) {
        return authService.login(dto);
    }

    @ApiOperation("获取登陆账户信息")
    @GetMapping("/info")
    public Result<MerchantAccountDTO> info(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO dto) {
        dto.setAccountTypeId(dto.getEmployeeId());
        return Result.succ(dto);
    }

    @ApiOperation("获取登陆账户信息【1.3.0】")
    @GetMapping("/infoV3")
    public Result<MerchantAccountInfoDTO> infoV3(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO dto) {
        return authService.infoV3(dto);
    }

    @ApiOperation("获取权限信息 : List<Map<String,Object>>")
    @GetMapping("/permission")
    public Result getPermissionInfo(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO) {
        if (Objects.equals(merchantAccountDTO.getAccountType(),AccountTypeEnum.SHOP.getStatus()) ||
                Objects.equals(merchantAccountDTO.getAccountType(),AccountTypeEnum.SHOP_PICKUP.getStatus())||
                Objects.equals(merchantAccountDTO.getAccountType(),AccountTypeEnum.MERCHANT.getStatus())){
            return Result.succ("*");
        }
        if (AccountTypeEnum.EMPLOYEE.getStatus().equals(merchantAccountDTO.getAccountType())) {
            merchantAccountDTO.setAccountTypeId(merchantAccountDTO.getEmployeeId());
        }
        return authService.getPermissionInfo(merchantAccountDTO);
    }

    @ApiOperation("新旧密码修改密码")
    @PostMapping("/updateAccountPwd")
    public Result updateAccountPwd(@RequestBody MerchantAccountPwdDTO dto) {
        return authService.updateAccountPwd(dto);
    }

    @ApiOperation("修改密码-通过短信验证码修改密码-1.3.0")
    @PostMapping("/updatePwdByPhone")
    public Result updatePwdByPhone(@RequestBody MerchantAccountPwdDTO dto) {
        return authService.updatePwdByPhone(dto);
    }

    @ApiOperation("发送短信验证码")
    @PostMapping("/sendSmsCodeCache")
    public Result<String> sendSmsCodeCache(@RequestBody MerchantAccountPwdDTO dto) {
        return authService.sendSmsCodeCache(dto);
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result logout(@RequestAttribute(TokenUtil.INFO) MerchantAccountDTO merchantAccountDTO) {
        if (Objects.equals(merchantAccountDTO.getAccountType(),AccountTypeEnum.EMPLOYEE.getStatus())){
            merchantUtils.removeMerchantPcToken(merchantAccountDTO.getEmployeeId().toString());
        }else{
            merchantUtils.removeMerchantPcToken(merchantAccountDTO.getAccountTypeId().toString());
        }
        return Result.succ();
    }
}
