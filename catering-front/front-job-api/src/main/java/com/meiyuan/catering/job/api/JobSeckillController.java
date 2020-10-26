package com.meiyuan.catering.job.api;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.job.service.merketing.JobSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName JobSeckillController
 * @Description
 * @Author gz
 * @Date 2020/4/1 16:03
 * @Version 1.1
 */
@RestController
@RequestMapping(value = "marketing/seckill")
public class JobSeckillController {
    @Autowired
    private JobSeckillService seckillService;

    /**
     * 每天23点执行一次  -- 查询第二天结束的活动加入延迟队列
     * @return
     */
    @GetMapping(value = "seckillTimedTask")
    public Result seckillTimedTask(){
        seckillService.seckillTimedTask();
        return Result.succ();
    }

    /**
     * 任务描述：刷新有效秒杀活动的第二天每个场次的商品库存
     * 执行时间：每天晚上00:00:30开始执行定时任务
     */
    @GetMapping(value = "seckillEventGoodsTask")
    public Result seckillEventGoodsTask() {
        seckillService.seckillEventGoodsTask();
        return Result.succ();
    }

}
