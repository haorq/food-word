package com.meiyuan.catering.job.api;

import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.job.service.goods.JobGoodsMenuService;
import com.meiyuan.catering.job.service.sharebill.JobShareBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/30 18:29
 * @description 简单描述
 **/
@RestController
@RequestMapping("goods/menu")
@Slf4j
public class JobGoodsMenuController {
    @Resource
    JobGoodsMenuService menuService;
    @Autowired
    private JobShareBillService shareBillService;
    @RequestMapping("/timingUpDown")
    public void timingUpDown() {
        log.info("--------- start timingUpDown menu---------");
        Result<List<Long>> listResult = menuService.timingUpDown();
        if (listResult.success() && BaseUtil.judgeList(listResult.getData())) {
            List<Long> menuIds = listResult.getData();
            if (menuIds.size() > 0){
                shareBillService.autoCancelFormenuModel(menuIds);
            }
        }
        log.info("--------- end timingUpDown menu---------");
    }
}
