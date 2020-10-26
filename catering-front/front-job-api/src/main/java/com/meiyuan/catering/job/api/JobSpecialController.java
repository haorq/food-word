package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.merketing.JobSpecialService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GongJunZheng
 * @date 2020/09/07 17:09
 * @description 营销特价商品活动定时任务
 **/

@RestController
@RequestMapping(value = "marketing/special")
public class JobSpecialController {

    @Autowired
    private JobSpecialService jobSpecialService;

    @ApiOperation("延迟定时开始/结束营销特价商品活动")
    @GetMapping(value = "/beginOrEndTimedTask")
    public void beginOrEndTimedTask() {
        jobSpecialService.beginOrEndTimedTask();
    }

}
