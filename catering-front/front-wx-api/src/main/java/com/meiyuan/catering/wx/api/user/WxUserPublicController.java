package com.meiyuan.catering.wx.api.user;

import com.meiyuan.catering.wx.service.user.WxUserPublicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/11 14:03
 */
@RestController
@RequestMapping("/weiXin/userPublic")
@Api(tags = "微信--公众号")
public class WxUserPublicController {
    @Resource
    private WxUserPublicService wxUserPublicService;

    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;

    @ApiOperation("公众号验证-关注事件接口 v1.4.0")
    @RequestMapping(value = "/checkToken", method = {RequestMethod.POST, RequestMethod.GET})
    public String checkToken() {
//        return wxUserPublicService.userFocusOn(request, response);
        return "";
    }
}
