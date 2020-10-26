package com.meiyuan.catering.job.api;

import com.meiyuan.catering.job.service.shop.JobShopService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GongJunZheng
 * @date 2020/10/10 18:10
 * @description 商家定时任务
 **/

@Slf4j
@RestController
@RequestMapping(value = "/shop")
public class JobShopController {

    @Autowired
    private JobShopService jobShopService;

    @ApiOperation("每天晚上12点，为每个商家进行账户余额提现操作")
    @GetMapping("/allinPayWithdraw")
    public void allinPayWithdraw() {
        log.info("开始执行商家通联账户余额提现定时任务...");
        jobShopService.allinPayWithdraw();
    }


}
