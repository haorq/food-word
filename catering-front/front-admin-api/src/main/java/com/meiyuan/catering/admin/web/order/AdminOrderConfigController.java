package com.meiyuan.catering.admin.web.order;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.order.AdminOrderConfigService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.admin.OrdersConfigDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author xie-xi-jie
 * @Description 后台-订单管理
 * @Date  2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/admin/order")
@Api(tags = "后台-订单设置")
public class AdminOrderConfigController {
    @Resource
    private AdminOrderConfigService adminOrderConfigService;
    /**
     * 功能描述: 后台——获取订单配置信息
     * @return: 后台订单详情
     */
    @PostMapping("/config/list")
    @ApiOperation("后台——获取订单配置信息")
    public Result<List<OrdersConfigDTO>> getOrdersConfigList(){
        return this.adminOrderConfigService.getOrdersConfigList();
    }
    /**
     * 功能描述: 后台——修改订单配置信息
     * @return: 后台订单详情
     */
    @PostMapping("/config/update")
    @ApiOperation("后台——修改订单配置信息")
    @LogOperation(value = "后台——修改订单配置信息")
    public Result<String> updateConfig(@Valid @RequestBody List<OrdersConfigDTO> configDtoList){
        return this.adminOrderConfigService.updateConfig(configDtoList);
    }
}
