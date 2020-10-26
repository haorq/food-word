package com.meiyuan.catering.admin.web.finance;

import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.finance.AdminChargeActivityService;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.finance.dto.AddRechargeActivityDTO;
import com.meiyuan.catering.finance.query.recharge.RechargeActivityQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.RechargeActivityListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Api(tags = "zzn-财务-充值活动")
@RestController
@RequestMapping(value = "/admin/recharge/activity")
public class AdminRechargeActivityController {

    @Resource
    private AdminChargeActivityService service;


    /**
     * 积分活动添加/修改
     *
     * @return
     */
    @ApiOperation("zzn-充值活动列表")
    @PostMapping("/pageList")
    public Result<PageData<RechargeActivityListVO>> pageList(@RequestBody RechargeActivityQueryDTO dto) {
        return service.pageList(dto);
    }

    /**
     * 充值活动添加/修改
     *
     * @return
     */
    @ApiOperation("zzn-新增/修改")
    @PostMapping("/saveOrUpdate")
    @LogOperation("充值活动添加/修改")
    public Result saveOrUpdate(@RequestBody @Validated AddRechargeActivityDTO dto) {
        return service.saveOrUpdate(dto);
    }

    /**
     * 充值活动逻辑删除
     *
     * @return
     */
    @ApiOperation("zzn-逻辑删除")
    @DeleteMapping("/deleteById/{id}")
    @LogOperation("充值活动逻辑删除")
    public Result deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    /**
     * 充值活动详情
     *
     * @return
     */
    @ApiOperation("zzn-充值活动详情")
    @GetMapping("/detailById/{id}")
    public Result<AddRechargeActivityDTO> detailById(@PathVariable Long id) {
        return service.detailById(id);
    }


    @ApiOperation("zzn-充值活动下架")
    @PutMapping("/downByIdV110/{id}")
    @LogOperation("充值活动下架")
    public Result<Boolean> downByIdV110(@PathVariable Long id) {
        return service.downByIdV110(id);
    }


}
