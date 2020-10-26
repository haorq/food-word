package com.meiyuan.catering.job.api;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.job.service.merketing.JobGrouponService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luohuan
 * @date 2020/4/2
 **/
@RestController
@RequestMapping(value = "marketing/groupon")
public class JobGrouponController {

    @Autowired
    private JobGrouponService grouponService;

    @ApiOperation("定时下架已结束的团购活动")
    @GetMapping("/downTimedTask")
    public Result downTimedTask() {
        grouponService.grouponDownTimedTask();
        return Result.succ();
    }
}
