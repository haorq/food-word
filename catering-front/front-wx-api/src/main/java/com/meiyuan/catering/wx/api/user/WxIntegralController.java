package com.meiyuan.catering.wx.api.user;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.vo.integral.IntegralDetailVo;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.service.user.WxIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/3/25
 **/
@RestController
@RequestMapping("/integral")
@Api(tags = "积分")
public class WxIntegralController {

    @Resource
    private WxIntegralService integralService;

    /**
     * 积分记录列表
     *
     * @param dto
     * @return
     */
    @ApiOperation("zzn-积分明细")
    @PostMapping("/integralDetail")
    public Result<IntegralDetailVo> integralDetail(@LoginUser UserTokenDTO dto, @RequestBody BasePageDTO pageDTO) {
        return integralService.integralDetail(dto, pageDTO);
    }


}
