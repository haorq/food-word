package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.adverting.JobAdvertingService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 描述:
 *
 * @author zengzhangni
 * @date 2020/6/1 16:17
 * @since v1.1.0
 */
@RestController
@RequestMapping("advertise")
@Slf4j
public class JobAdvertingController {

    @Autowired
    private JobAdvertingService advertingService;

    @ApiOperation("广告定时上下架")
    @GetMapping("/downAndUp")
    public void downAndUp() {
        log.debug("广告定时上下架 开始");
        advertingService.downAndUp();
        log.debug("广告定时上下架 结束");
    }

}
