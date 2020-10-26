package com.meiyuan.catering.wx.api.finance;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.entity.CateringRechargeRuleEntity;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.finance.WxRechargeActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author zengzhangni
 * @date 2020-03-25
 */
@Api(tags = "zzn-财务-充值活动")
@RestController
@RequestMapping(value = "/recharge/activity")
public class WxRechargeActivityController {

    @Resource
    private WxRechargeActivityService service;

    /**
     * 充值活动列表
     *
     * @return
     */
    @ApiOperation("zzn-充值活动列表")
    @PostMapping("/listByUserType")
    public Result<List<CateringRechargeRuleEntity>> listByUserType(@LoginUser UserTokenDTO userDTO) {
        return service.listByUserType(userDTO);
    }


}
