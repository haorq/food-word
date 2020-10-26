package com.meiyuan.catering.wx.api.goods;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.menu.WxMenuServiceTimeDTO;
import com.meiyuan.catering.goods.dto.menu.WxMenuServiceTimeQueryDTO;
import com.meiyuan.catering.wx.service.goods.WxMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wxf
 * @date 2020/4/1 9:34
 * @description 简单描述
 **/
@RestController
@RequestMapping("api/goods")
@Deprecated
public class WxMenuController {
    @Resource
    WxMenuService menuService;

    /**
     * 微信获取菜单对应的送达时间
     * 1、菜单模式 返回对应送达时间
     * 2、菜品模式 返回明后两天的时间
     *
     * @author: wxf
     * @date: 2020/3/31 18:13
     * @param dto 查询参数
     * @return: {@link WxMenuServiceTimeDTO}
     **/
    @PostMapping("wxMenuService")
    @ApiOperation("菜单对应的送达时间")
    public Result<WxMenuServiceTimeDTO> wxMenuService(@RequestBody WxMenuServiceTimeQueryDTO dto) {
        return menuService.wxMenuService(dto);
    }
}
