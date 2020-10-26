package com.meiyuan.catering.wx.api.merchant;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.wx.annotation.LoginUser;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.merchant.WxMerchantIndexV13DTO;
import com.meiyuan.catering.wx.dto.merchant.v1.WxMerchantIndexV1RequestDTO;
import com.meiyuan.catering.wx.service.merchant.WxMerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zengzhangni
 * @date 2020/8/4 13:54
 * @since v1.3.0
 */
@RestController
@RequestMapping("/api/v1.3.0/merchant")
@Api(tags = "zzn-商家店铺v1.3.0")
@Slf4j
public class WxMerchantV130Controller {

    @Autowired
    private WxMerchantService wxMerchantService;

    @PostMapping("/index")
    @ApiOperation("首页")
    public Result<WxMerchantIndexV13DTO> index(@LoginUser(required = false) UserTokenDTO token, @RequestBody WxMerchantIndexV1RequestDTO dto) {
        return wxMerchantService.index(token, dto);
    }


}
