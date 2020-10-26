package com.meiyuan.catering.wx.api.marketing;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.marketing.SeckillParamDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WxMarketingSeckillController
 * @Description
 * @Author gz
 * @Date 2020/3/26 14:34
 * @Version 1.1
 */
@Api(tags = "营销管理-秒杀")
@RestController
@Deprecated
public class WxMarketingSeckillController {

    /**
     * 功能描述: 执行秒杀操作<br>
     *
     * @Param: [token, seckillGoodsId, number]
     * @Return: com.meiyuan.catering.core.util.Result
     * @Author: gz
     * @Date: 2020/3/26 14:36
     */
    @ApiOperation(value = "执行秒杀操作", notes = "执行秒杀操作")
    @PostMapping(value = "execute")
    public Result execute(@LoginUser UserTokenDTO token,
                          @RequestBody SeckillParamDTO dto) {
        return Result.deprecated();
    }

}
