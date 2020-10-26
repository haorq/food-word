package com.meiyuan.catering.merchant.api.merchant;

import com.meiyuan.catering.admin.vo.role.MerchantMenuVO;
import com.meiyuan.catering.admin.vo.version.AppVersionInfoVo;
import com.meiyuan.catering.core.dto.base.merchant.ShopNoticeInfoDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.annotation.LoginMerchant;
import com.meiyuan.catering.merchant.dto.merchant.*;
import com.meiyuan.catering.merchant.dto.merchant.request.MerchantSendSmsCodeRequestDTO;
import com.meiyuan.catering.merchant.enums.AccountTypeEnum;
import com.meiyuan.catering.merchant.service.merchant.MerchantBaseService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yaoozu
 * @description 商户基本信息
 * @date 2020/3/2010:49
 * @since v1.0.0
 */
@RestController
@RequestMapping("/app/merchant")
@Api(tags = "商户-基本信息")
public class MerchantBaseController {

    @Autowired
    private MerchantBaseService merchantBaseService;

    @PostMapping("/login")
    @ApiOperation("商户登录")
    public Result<MerchantLoginResponseDTO> login(@Validated @RequestBody MerchantLoginRequestDTO dto){
        return merchantBaseService.login(dto);
    }

    @PostMapping("/sendSmsCode")
    @ApiOperation("发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", paramType = "query", required = true, dataType = "String"),
    })
    public Result<String> sendSmsCode(@RequestParam(required = false) String phone,@RequestBody(required = false) MerchantSendSmsCodeRequestDTO dto){
        if (StringUtils.isEmpty(phone)){
            phone = dto.getPhone();
        }
        return merchantBaseService.sendSmsCode(phone);
    }

    @PostMapping("/updatePassword")
    @ApiOperation("app-短信验证码修改密码")
    public Result updatePassword(@RequestBody MerchantPasswordRequestDTO dto){
        return merchantBaseService.updatePassword(dto);
    }

    @PostMapping("/updatePasswordByOld")
    @ApiOperation("原密码新密码修改密码")
    public Result updatePasswordByOld(@LoginMerchant MerchantTokenDTO token,@Valid @RequestBody MerchantPasswordRequestDTO dto){
        return merchantBaseService.updatePasswordByOld(token.getShopId(),dto);
    }

    @GetMapping("/permissions")
    @ApiOperation("获取所有权限")
    public Result<List<MerchantMenuVO>> getAllPermissions(@LoginMerchant MerchantTokenDTO token) {
        return merchantBaseService.getAllPermissions(token);
    }

    @GetMapping("/permissions/absence")
    @ApiOperation("获取缺少的权限")
    public Result<List<MerchantMenuVO>> getPermissionsAbsence(@LoginMerchant MerchantTokenDTO token) {
        if (token.getType().equals(AccountTypeEnum.MERCHANT.getStatus()) || token.getType().equals(AccountTypeEnum.PICKUP.getStatus())) {
            return Result.succ(new ArrayList<>());
        }
        return merchantBaseService.getPermissionsAbsence(token);
    }

//    @GetMapping("/logout/{deviceNumber}")
//    @ApiOperation("退出登录")
//    public Result logout(@LoginMerchant MerchantTokenDTO dto,
//                    @ApiParam("设备号") @PathVariable String deviceNumber){
//        return merchantBaseService.logout(dto,deviceNumber);
//    }

    @GetMapping("/logout/{deviceNumber}/{shopId}")
    @ApiOperation("退出登录【1.4.0】")
    public Result logout(@ApiParam("店铺id") @PathVariable String shopId,
                         @ApiParam("设备号") @PathVariable String deviceNumber){
        return merchantBaseService.logout(shopId,deviceNumber);
    }


    @PostMapping("/modifyBusinessStatus/{type}")
    @ApiOperation("改变营业（门店/自提点）状态：营业中 变为 打烊中/暂停营业，打样中/暂停营业 变为 【1.5.0】")
    public Result<ShopResponseDTO> modifyBusinessStatus(@LoginMerchant MerchantTokenDTO dto,
                               @ApiParam("操作类型：查询/修改：false：查询，true：修改")@PathVariable Boolean type){
        return merchantBaseService.modifyBusinessStatus(dto.getShopId(),type);
    }


    @PostMapping("/modifyBaseInfo")
    @ApiOperation("门店信息修改（除营业状态）")
    public Result<ShopResponseDTO> modifyBaseInfo(@LoginMerchant MerchantTokenDTO tokenDTO ,@Validated @RequestBody MerchantModifyInfoRequestDTO dto){

        return merchantBaseService.modifyBaseInfo(tokenDTO.getShopId(),dto);
    }

    @PostMapping("/exchangeIdentity/{shopRole}")
    @ApiOperation("店铺切换登录身份")
    public Result<MerchantLoginResponseDTO> exchangeIdentity(@LoginMerchant MerchantTokenDTO tokenDTO ,
                                                    @ApiParam("登录类型：1--店铺，2--自提点")@PathVariable Integer shopRole){

        return merchantBaseService.exchangeIdentity(tokenDTO.getShopId(),shopRole);
    }

    @GetMapping("/appUpdateVersion")
    @ApiOperation("app版本更新[1.3.0]")
    public Result<AppVersionInfoVo> appUpdateVersion(){

        return merchantBaseService.appUpdateVersion();
    }


    @GetMapping("/getDeviceNoticeCache/{deviceNumber}")
    @ApiOperation("获取设备新订单缓存信息")
    public ShopNoticeInfoDTO getDeviceNoticeCache(@LoginMerchant MerchantTokenDTO tokenDto,
                                             @PathVariable String deviceNumber){
        return merchantBaseService.getShopOrderNotice(deviceNumber + tokenDto.getShopId());
    }

}
