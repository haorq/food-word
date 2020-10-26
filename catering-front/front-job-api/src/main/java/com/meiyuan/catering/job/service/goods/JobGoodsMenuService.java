package com.meiyuan.catering.job.service.goods;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.feign.GoodsMenuClient;
import com.meiyuan.catering.goods.service.CateringGoodsMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/30 18:24
 * @description 简单描述
 **/
@Service
public class JobGoodsMenuService {
    @Resource
    GoodsMenuClient goodsMenuClient;

    /**
     * 定时上下架
     *
     * @author: wxf
     * @date: 2020/3/30 18:14
     * @return Result<List<Long>> 下架菜单id
     **/
    public Result<List<Long>> timingUpDown() {
        return goodsMenuClient.timingUpDown();
    }
}
