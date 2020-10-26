package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.merketing.JobExceptionGrouponService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述: 异常团购订单处理
 *
 * @author zengzhangni
 * @date 2020/9/10 16:54
 * @since v1.4.0
 */
@RestController
@RequestMapping("exception/groupon")
public class JobExceptionGrouponController {

    @Resource
    private JobExceptionGrouponService jobExceptionGrouponService;

    @GetMapping("/disposeExceptionGroupon")
    public void disposeExceptionGroupon() {
        jobExceptionGrouponService.disposeExceptionGroupon();
    }

}
