package com.meiyuan.catering.admin.web.user;

import com.meiyuan.catering.admin.service.user.AdminIntegralRuleService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.entity.CateringIntegralRuleEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/3/16
 * @description 积分规则
 **/
@RestController
@RequestMapping("/admin/integral/rule")
@Api(tags = "zzn-积分-积分规则")
public class AdminIntegralRuleController {

    @Resource
    private AdminIntegralRuleService integralRuleService;

    /**
     * 积分规则下拉
     *
     * @return
     */
    @ApiOperation("zzn-积分规则下拉(过滤)")
    @GetMapping("/all")
    public Result<List<CateringIntegralRuleEntity>> all() {
        return integralRuleService.all();
    }

    /**
     * 积分规则下拉
     *
     * @return
     */
    @ApiOperation("zzn-积分规则下拉(所有)")
    @GetMapping("/list")
    public Result<List<CateringIntegralRuleEntity>> list() {
        return integralRuleService.list();
    }


}
