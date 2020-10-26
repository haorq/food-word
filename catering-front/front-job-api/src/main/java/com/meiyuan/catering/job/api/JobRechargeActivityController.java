package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.finance.JobRechargeActivityService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lhm
 * @date 2020/4/3 10:52
 **/
@RestController
@RequestMapping(value = "recharge/activity")
@Slf4j
public class JobRechargeActivityController {
    @Resource
    private JobRechargeActivityService activityService;

    @ApiOperation("充值活动自动开始/过期")
    @GetMapping("/autoStartAndPast")
    public void autoStartAndPast() {
        log.debug("充值活动自动开始/过期 开始");

        activityService.autoStartAndPast();

        log.debug("充值活动自动开始/过期 结束");
    }

}
