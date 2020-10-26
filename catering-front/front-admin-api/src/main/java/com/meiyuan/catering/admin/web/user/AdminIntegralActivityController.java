package com.meiyuan.catering.admin.web.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.service.user.AdminIntegralActivityService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.integral.AddIntegralActivityDTO;
import com.meiyuan.catering.user.entity.CateringIntegralActivityEntity;
import com.meiyuan.catering.user.query.integral.IntegralRuleQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/3/18
 * @description 积分活动
 **/
@RestController
@RequestMapping("/admin/integral/activity")
@Api(tags = "zzn-积分-积分活动")
public class AdminIntegralActivityController {

    @Resource
    private AdminIntegralActivityService integralActivityService;

    /**
     * 积分活动列表
     *
     * @return
     */
    @ApiOperation("zzn-积分活动列表")
    @PostMapping("/pageList")
    public Result<IPage<CateringIntegralActivityEntity>> pageList(@RequestBody IntegralRuleQueryDTO dto) {
        return integralActivityService.pageList(dto);
    }

    /**
     * 积分活动添加/修改
     *
     * @return
     */
    @ApiOperation("zzn-新增/修改")
    @PostMapping("/saveOrUpdate")
    @LogOperation(value = "新增/修改积分活动")
    public Result saveOrUpdate(@RequestBody AddIntegralActivityDTO dto) {
        return integralActivityService.saveOrUpdate(dto);
    }

    /**
     * 逻辑删除积分活动
     *
     * @return
     */
    @ApiOperation("zzn-逻辑删除积分活动")
    @DeleteMapping("/deleteById/{id}")
    @LogOperation(value = "逻辑删除积分活动")
    public Result deleteById(@PathVariable Long id) {
        return integralActivityService.deleteById(id);
    }

    /**
     * 积分活动详情
     *
     * @return
     */
    @ApiOperation("zzn-积分活动详情")
    @GetMapping("/detailById/{id}")
    public Result<AddIntegralActivityDTO> detailById(@PathVariable Long id) {
        return integralActivityService.detailById(id);
    }






}
