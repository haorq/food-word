package com.meiyuan.catering.admin.web.order;

import com.meiyuan.catering.admin.service.order.AdminOrderAppraiseService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.admin.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author xie-xi-jie
 * @Description 后台-订单评价
 * @Date  2020/3/12 0012 15:29
 */
@RestController
@RequestMapping("/admin/order")
@Api(tags = "后台-订单评价")
public class AdminOrderAppraiseController {
    @Resource
    private AdminOrderAppraiseService adminOrderAppraiseService;

    /**
     * 功能描述: 后台——订单评价列表
     * @return: 订单评价列表
     */
    @PostMapping("/appraise/list")
    @ApiOperation("后台——订单评价列表")
    public Result<PageData<OrdersAppraiseAdminDTO>> appraiseListQuery(@RequestBody OrdersAppraiseParamAdminDTO param){
        return this.adminOrderAppraiseService.appraiseListQuery(param);
    }
    /**
     * 功能描述: 后台——订单评价详情
     * @return: 订单评价详情
     */
    @PostMapping("/appraise/detail")
    @ApiOperation("后台——订单评价详情")
    public Result<OrdersAppraiseDetailAdminDTO> appraiseDetailQuery(@Valid @RequestBody OrdersAppraiseDetailParamAdminDTO param){
        return this.adminOrderAppraiseService.appraiseDetailQuery(param);
    }
}
