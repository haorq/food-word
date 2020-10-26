package com.meiyuan.catering.admin.web.merchant;

import com.meiyuan.catering.admin.service.merchant.AdminOldDataHandleService;
import com.meiyuan.catering.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author MeiTao
 * @Date 2020/8/3 0003 16:15
 * @Description 简单描述 : 老数据处理
 * @Since version-1.3.0
 */
@RestController
@RequestMapping("/admin/merchant/data")
@Api(tags = "老数据处理")
public class AdminOldDataHandleController {

    @Autowired
    AdminOldDataHandleService dataHandleService;

    @ApiOperation("店铺老数据处理【1.5.0】")
    @PostMapping("/handleShopDataV5")
    public Result handleShopDataV5() {
        return dataHandleService.handleShopDataV5();
    }

    @ApiOperation("清除app登录token老数据处理【1.5.0】")
    @PostMapping("/handleAppTokenV5")
    public void handleAppTokenV5() {
        dataHandleService.handleAppTokenV5();
    }
}
