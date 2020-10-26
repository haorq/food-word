package com.meiyuan.catering.wx.api.user;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.feedback.FeedbackAddDTO;
import com.meiyuan.catering.user.dto.user.UserSaveDTO;
import com.meiyuan.catering.user.dto.user.UserSessionInfoDTO;
import com.meiyuan.catering.user.query.user.UserCompanyBindDTO;
import com.meiyuan.catering.user.vo.user.*;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.user.SendPhoneCodeDTO;
import com.meiyuan.catering.wx.service.user.WxUserAuthService;
import com.meiyuan.catering.wx.vo.WxSignatureResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/3/23 11:31
 **/
@RestController
@RequestMapping("/api/user")
@Api(tags = "微信--用户授权相关接口--lhm")
public class WxUserAuthController {

    @Resource
    private WxUserAuthService wxUserAuthService;

    @ApiOperation("处理老系统信息")
    @GetMapping("/disposeOldSystemInfo/{phone}")
    public Result<Boolean> disposeOldSystemInfo(@LoginUser UserTokenDTO tokenDTO, @PathVariable String phone) {
        wxUserAuthService.disposeOldSystemInfo(tokenDTO, phone);
        return Result.succ();
    }

    @ApiOperation("发送验证码")
    @GetMapping("/sendCode")
    public Result<Boolean> sendCode(@LoginUser(required = false) UserTokenDTO tokenDTO,@RequestParam String phone) {
        return wxUserAuthService.sendCode(phone);
    }

    @ApiOperation("验证验证码")
    @PostMapping("/verifyCode")
    public Result<Boolean> verifyCode(@LoginUser UserTokenDTO tokenDTO, @RequestBody SendPhoneCodeDTO dto) {
        return wxUserAuthService.verifyCode(tokenDTO, dto);
    }

    @ApiOperation("我的--个人用户--个人资料")
    @GetMapping("/getUserDetail")
    public Result<UserDetailInfoVo> getUserDetail(@LoginUser UserTokenDTO tokenDTO) {
        return wxUserAuthService.getUserDetailInfo(tokenDTO);
    }

    @ApiOperation("我的--企业用户--企业资料")
    @GetMapping("/getUserCompanyDetail")
    public Result<UserCompanyDetailInfoVo> getUserCompanyDetail(@LoginUser UserTokenDTO tokenDTO) {
        return wxUserAuthService.getUserCompanyDetail(tokenDTO.getUserCompanyId());
    }

    @ApiOperation("我的--绑定企业用户")
    @PostMapping("/bindUserCompany")
    public Result<Boolean> bindUserCompany(@LoginUser UserTokenDTO tokenDTO, @RequestBody UserCompanyBindDTO dto) {
        return wxUserAuthService.bindUserCompany(tokenDTO.getToken(), dto);
    }

    @ApiOperation("我的--解除绑定企业用户")
    @GetMapping("/unBindUserCompany")
    public Result<Boolean> unBindUserCompany(@LoginUser UserTokenDTO tokenDTO) {
        return wxUserAuthService.unBindUserCompany(tokenDTO.getToken());
    }

    @ApiOperation("我的---建议反馈")
    @PostMapping("/userFeedback")
    public Result<Boolean> userFeedback(@LoginUser UserTokenDTO tokenDTO, @RequestBody FeedbackAddDTO dto) {
        return wxUserAuthService.userFeedback(tokenDTO, dto);
    }

    @ApiOperation("我的---首页")
    @GetMapping("/userIndex")
    public Result<UserIndexVo> userIndex(@LoginUser UserTokenDTO user) {
        return wxUserAuthService.userIndex(user);
    }

    @ApiOperation("我的---切换用户类型")
    @GetMapping("/switchUserType")
    public Result<UserTokenDTO> switchUserType(@LoginUser UserTokenDTO user) {
        return wxUserAuthService.switchUserType(user);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public Result<UserDetailInfoVo> getUserInfo(@LoginUser UserTokenDTO tokenDTO) {
        return wxUserAuthService.getUserInfo(tokenDTO);
    }

    @ApiOperation("立即领取")
    @GetMapping("/isNewUser")
    public Result<Boolean> isNewUser(@LoginUser(required = false) UserTokenDTO tokenDTO, @RequestParam Long groundPusherId) {
        return wxUserAuthService.isNewUser(tokenDTO, groundPusherId);
    }

    @ApiOperation("获取用户手机号 v1.3.0")
    @PostMapping("/getPhone")
    public Result<UserSessionInfoVO> getPhone(@RequestBody UserSessionInfoDTO dto) {
        return wxUserAuthService.getPhone(dto);
    }

    @ApiOperation("微信登录-手机号登录 v1.3.0")
    @PostMapping("/loginByPhone")
    public Result<UserDetailVO> loginByPhone(@RequestBody UserSaveDTO dto) {
        return wxUserAuthService.loginByPhone(dto);
    }

    @ApiOperation("微信退出登录 v1.3.0")
    @GetMapping("/signOut")
    public Result<Boolean> signOut(@LoginUser UserTokenDTO token) {
        return wxUserAuthService.signOut(token);
    }



    /**
     * 获取微信URL签名信息
     *
     * @param signUrl    需要签名的URL
     * @return URL信息
     */
    @ApiOperation(value = "获取微信URL签名信息")
    @GetMapping(value = "getWxUrlSignature")
    public Result<WxSignatureResultVO> getWxUrlSignature( @RequestParam String signUrl){
        return wxUserAuthService.getWxUrlSignature(signUrl);
    }
}