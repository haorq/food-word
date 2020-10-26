package com.meiyuan.catering.wx.api.finance;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.dto.PayPasswordDTO;
import com.meiyuan.catering.finance.service.CateringUserBalanceAccountService;
import com.meiyuan.catering.finance.vo.account.UserAccountDetailVO;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.finance.WxUserBalanceAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Api(tags = "zzn-财务-账户")
@RestController
@RequestMapping(value = "/balance/account")
public class WxUserBalanceAccountController {

    @Resource
    private WxUserBalanceAccountService service;

    /**
     * 账单明细
     *
     * @return
     */
    @ApiOperation("zzn-账单明细")
    @PostMapping("/accountDetail")
    public Result<UserAccountDetailVO> accountDetail(@LoginUser UserTokenDTO user, @RequestBody BasePageDTO pageDTO) {
        return service.accountDetail(user, pageDTO);
    }

    /**
     * 设置/重置支付密码
     *
     * @return
     */
    @ApiOperation("zzn-设置/重置支付密码")
    @PostMapping("/setOrResetPayPassword")
    public Result setPayPassword(@LoginUser UserTokenDTO user, @RequestBody @Validated PayPasswordDTO dto) {
        return service.setPayPassword(user, dto);
    }

    @ApiOperation("zzn-获取余额")
    @GetMapping("/getBalance")
    public Result getBalance(@LoginUser UserTokenDTO user) {
        return service.getBalance(user);
    }


}
